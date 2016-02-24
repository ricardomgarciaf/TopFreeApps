package com.example.ricardogarcia.topfreeapps;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.TextView;

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

        if (getResources().getBoolean(R.bool.portrait_mode)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        TextView title = (TextView) findViewById(R.id.titleCategory);
        title.setText(b.getString(CategoryAdapter.CATEGORY).toUpperCase());
        new RetrieveFromDatabase().execute(b.getString(CategoryAdapter.CATEGORY));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_apps, menu);
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
