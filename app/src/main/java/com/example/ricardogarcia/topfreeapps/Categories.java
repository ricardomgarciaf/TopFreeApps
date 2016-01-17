package com.example.ricardogarcia.topfreeapps;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

import java.util.ArrayList;

/*
    Activity that shows the list/grid of categories, calling the adapter and retrieving the information from the database
 */
public class Categories extends AppCompatActivity {

    AbsListView viewCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        if (getResources().getBoolean(R.bool.portrait_mode)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        new RetrieveFromDatabase().execute(b.getString(Splash.INFO_CONNECTION));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categories, menu);
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
            ArrayList<Category> categories = new ArrayList<Category>();
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

            CategoryAdapter cAdapter = new CategoryAdapter(Categories.this, categories);
            viewCategories = (AbsListView) findViewById(R.id.view_categories);
            viewCategories.setAdapter(cAdapter);
            viewCategories.setEmptyView(findViewById(R.id.emptyView));

        }
    }
}
