package com.example.ricardogarcia.topfreeapps.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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

import com.example.ricardogarcia.topfreeapps.R;
import com.example.ricardogarcia.topfreeapps.adapter.AppAdapter;
import com.example.ricardogarcia.topfreeapps.adapter.CategoryAdapter;
import com.example.ricardogarcia.topfreeapps.db.DatabaseHandler;
import com.example.ricardogarcia.topfreeapps.model.App;

import java.util.ArrayList;

/*
    Activity that shows the list/grid of apps, calling the adapter and extracting the information from the database
 */

public class Apps extends AppCompatActivity {

    AbsListView viewApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (getResources().getBoolean(R.bool.portrait_mode)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        setTitle(b.getString(CategoryAdapter.CATEGORY));
        new RetrieveFromDatabase().execute(b.getString(CategoryAdapter.CATEGORY));


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

    private class RetrieveFromDatabase extends AsyncTask<String, Void, ArrayList<App>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<App> doInBackground(String... strings) {
            ArrayList<App> apps;
            DatabaseHandler db = DatabaseHandler.getInstance(Apps.this);
            String category = strings[0];

            apps = db.getAppsByCategory(category);

            return apps;
        }

        @Override
        protected void onPostExecute(ArrayList<App> apps) {
            super.onPostExecute(apps);

            AppAdapter aAdapter = new AppAdapter(Apps.this, apps);
            viewApps = (AbsListView) findViewById(R.id.view_apps);
            viewApps.setAdapter(aAdapter);
            viewApps.setEmptyView(findViewById(R.id.emptyView));

        }
    }
}
