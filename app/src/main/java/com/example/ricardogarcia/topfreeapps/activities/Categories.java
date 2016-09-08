package com.example.ricardogarcia.topfreeapps.activities;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.example.ricardogarcia.topfreeapps.R;
import com.example.ricardogarcia.topfreeapps.adapter.CategoryAdapter;
import com.example.ricardogarcia.topfreeapps.db.DatabaseHandler;
import com.example.ricardogarcia.topfreeapps.model.Category;

import java.util.ArrayList;

/*
    Activity that shows the list/grid of categories, calling the adapter and retrieving the information from the database
 */
public class Categories extends AppCompatActivity{

    public static final String PREF_NAME = "SessionPreferences";
    public static int PRIVATE_MODE = 0;
    AbsListView viewCategories;
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;
    public static String STATUS_CONNECTION="STATUS_CONNECTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        pref = getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();


        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(getResources().getString(R.string.title_categories));

        if (getResources().getBoolean(R.bool.portrait_mode)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b.getString(Splash.INFO_CONNECTION).equals("No Update")){
            TextView textnoconexion= (TextView) findViewById(R.id.noconnectiontitle);
            textnoconexion.setVisibility(View.VISIBLE);
            editor.putBoolean(STATUS_CONNECTION,false);
            editor.commit();
        }else{
            editor.putBoolean(STATUS_CONNECTION,true);
            editor.commit();
        }

        new RetrieveFromDatabase().execute(b.getString(Splash.INFO_CONNECTION));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
        Retrieves the data of the categories from the DB, checking if there is data in the DB. In case there is non
        existing DB, means that when the app was launched at the first time, there was no internet connection.
        Not making possible to download the data in order to work online/offline.
     */
    private class RetrieveFromDatabase extends AsyncTask<String, Void, ArrayList<Category>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Loading categories
        }

        @Override
        protected ArrayList<Category> doInBackground(String... strings) {
            ArrayList<Category> categories = new ArrayList<>();
            DatabaseHandler db = DatabaseHandler.getInstance(Categories.this);
            String status = strings[0];

            if (status.equals("Update") || (status.equals("No Update")) && db.databaseExist()) {
                categories = db.getAllCategories();
            }
            return categories;
        }

        @Override
        protected void onPostExecute(ArrayList<Category> categories) {
            super.onPostExecute(categories);



            if(categories.size()>0) {
                CategoryAdapter cAdapter = new CategoryAdapter(Categories.this, categories);
                viewCategories = (AbsListView) findViewById(R.id.view_categories);
                viewCategories.setAdapter(cAdapter);
            }
            else{

                AlertDialog.Builder builder= new AlertDialog.Builder(Categories.this);
                builder.setMessage(R.string.no_connection_text)
                        .setTitle(R.string.no_connection_title_text)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                System.exit(0);
                            }
                        });
                builder.setCancelable(false);
                builder.show();
            }

        }
    }
}
