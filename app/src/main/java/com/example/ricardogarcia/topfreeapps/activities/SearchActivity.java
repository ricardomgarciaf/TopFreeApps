package com.example.ricardogarcia.topfreeapps.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.example.ricardogarcia.topfreeapps.R;
import com.example.ricardogarcia.topfreeapps.adapter.AppAdapter;
import com.example.ricardogarcia.topfreeapps.db.DatabaseHandler;
import com.example.ricardogarcia.topfreeapps.model.App;

import java.util.ArrayList;

/**
 * Created by ricardogarcia on 9/3/16.
 */
public class SearchActivity extends AppCompatActivity {

    AbsListView viewApps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_activity);

        if(Build.VERSION.SDK_INT>=21) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        Intent intent=getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            setTitle(getResources().getString(R.string.search_resultstitle)+" "+query);

            if(!Categories.pref.getBoolean(Categories.STATUS_CONNECTION,false)){
                TextView textnoconexion= (TextView) findViewById(R.id.noconnectiontitle);
                textnoconexion.setVisibility(View.VISIBLE);
                if(Build.VERSION.SDK_INT<21) {
                    setTitle(getResources().getString(R.string.search_resultstitle)+" "+query+" - "+getResources().getString(R.string.conexionoffline));
                }
            }

            new RetrieveFromDatabase().execute(query);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    /*
        Retrieves the data of the categories from the DB, checking if there is data in the DB. In case there is non
        existing DB, means that when the app was launched at the first time, there was no internet connection.
        Not making possible to download the data in order to work online/offline.
     */
    private class RetrieveFromDatabase extends AsyncTask<String, Void, ArrayList<App>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Loading categories
        }

        @Override
        protected ArrayList<App> doInBackground(String... strings) {
            ArrayList<App> apps = new ArrayList<>();
            DatabaseHandler db = DatabaseHandler.getInstance(SearchActivity.this);
            String query = strings[0];

            apps=db.searchApps(query);

            return apps;
        }

        @Override
        protected void onPostExecute(ArrayList<App> apps) {
            super.onPostExecute(apps);

            AppAdapter aAdapter = new AppAdapter(SearchActivity.this, apps);

            viewApps = (AbsListView) findViewById(R.id.view_apps);
            viewApps.setAdapter(aAdapter);
            viewApps.setEmptyView(findViewById(R.id.emptyView));

        }
    }
}
