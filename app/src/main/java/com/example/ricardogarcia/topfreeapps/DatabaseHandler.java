package com.example.ricardogarcia.topfreeapps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Class that extends from SQLiteOpenHelper used to create, insert, update the tables from the database
 * Created by ricardogarcia on 1/14/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static DatabaseHandler sInstance;
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "freeAppsManager";

    // Contacts table name
    private static final String TABLE_CATEGORIES = "category";
    private static final String TABLE_APPS = "app";

    //Category Table Columns
    private static final String CATEGORY_NAME = "name";
    private static final String CATEGORY_LOGO = "logo";

    //App Table Columns
    private static final String APP_NAME = "name";
    private static final String APP_CATEGORY = "category";
    private static final String APP_SUMMARY = "summary";
    private static final String APP_IMAGE_SMALL = "small";
    private static final String APP_IMAGE_MEDIUM = "medium";
    private static final String APP_IMAGE_LARGE = "large";
    private Context ctx;


    public static synchronized DatabaseHandler getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHandler(context.getApplicationContext());
        }
        return sInstance;
    }

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
                + CATEGORY_NAME + " TEXT PRIMARY KEY," + CATEGORY_LOGO + " BLOB"
                + ")";
        sqLiteDatabase.execSQL(CREATE_CATEGORY_TABLE);

        String CREATE_APP_TABLE = "CREATE TABLE " + TABLE_APPS + "("
                + APP_NAME + " TEXT PRIMARY KEY," + APP_CATEGORY + " TEXT,"
                + APP_SUMMARY + " TEXT," + APP_IMAGE_SMALL + " BLOB," + APP_IMAGE_MEDIUM + " BLOB," + APP_IMAGE_LARGE
                + " BLOB," + " FOREIGN KEY (" + APP_CATEGORY + ") REFERENCES " + TABLE_CATEGORIES + "(" + CATEGORY_NAME + "))";
        sqLiteDatabase.execSQL(CREATE_APP_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_APPS);

        onCreate(sqLiteDatabase);
    }

    //CRUD Operations
    public void addCategory(Category category) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CATEGORY_NAME, category.getName());
        values.put(CATEGORY_LOGO, category.getLogo());

        // Inserting Row
        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }

    public void addApp(App app) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(APP_NAME, app.getName());
        values.put(APP_CATEGORY, app.getCategory());
        values.put(APP_SUMMARY, app.getSummary());
        values.put(APP_IMAGE_SMALL, app.getSmall());
        values.put(APP_IMAGE_MEDIUM, app.getMedium());
        values.put(APP_IMAGE_LARGE, app.getLarge());

        db.insert(TABLE_APPS, null, values);
        db.close();
    }

    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> categoryList = new ArrayList<Category>();

        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setName(cursor.getString(0));
                category.setLogo(cursor.getBlob(1));
                categoryList.add(category);
            } while (cursor.moveToNext());
        }

        return categoryList;
    }


    public App getApp(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_APPS, null, APP_NAME + "=?", new String[]{name}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        App app = new App(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getBlob(3), cursor.getBlob(4), cursor.getBlob(5));
        return app;
    }

    public ArrayList<App> getAppsByCategory(String category) {

        ArrayList<App> appList = new ArrayList<App>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_APPS, null, APP_CATEGORY + "=?", new String[]{category}, null, null, APP_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                App app = new App();
                app.setName(cursor.getString(0));
                app.setCategory(cursor.getString(1));
                app.setSummary(cursor.getString(2));
                app.setSmall(cursor.getBlob(3));
                app.setMedium(cursor.getBlob(4));
                app.setLarge(cursor.getBlob(5));
                appList.add(app);
            } while (cursor.moveToNext());
        }
        return appList;

    }

    public boolean isCategoryInserted(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORIES, null, CATEGORY_NAME + "=?", new String[]{category}, null, null, null);

        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    public boolean isAppInserted(String app) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_APPS, null, APP_NAME + "=?", new String[]{app}, null, null, null);

        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    public boolean databaseExist() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(String.valueOf(ctx.getDatabasePath(DATABASE_NAME)), null, SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
            return true;
        } catch (SQLiteException e) {
            return false;
        }
    }

}
