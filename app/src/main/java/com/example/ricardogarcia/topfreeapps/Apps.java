package com.example.ricardogarcia.topfreeapps;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.TextView;

import java.util.ArrayList;


public class Apps extends AppCompatActivity {

    AbsListView viewApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);

        if(getResources().getBoolean(R.bool.portrait_mode)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        Intent intent= getIntent();
        Bundle b=intent.getExtras();

        TextView title= (TextView) findViewById(R.id.titleCategory);
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

    private class RetrieveFromDatabase extends AsyncTask<String,Void,ArrayList<App>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<App> doInBackground(String... strings) {
            ArrayList<App> apps= new ArrayList<App>();
            DatabaseHandler db = DatabaseHandler.getInstance(Apps.this);
            String category=strings[0];

            apps=db.getAppsByCategory(category);

            return apps;
        }

        @Override
        protected void onPostExecute(ArrayList<App> apps) {
            super.onPostExecute(apps);

            AppAdapter aAdapter= new AppAdapter(Apps.this,apps);
            viewApps= (AbsListView) findViewById(R.id.view_apps);
            viewApps.setAdapter(aAdapter);
            viewApps.setEmptyView(findViewById(R.id.emptyView));


            /*
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            unlockScreenOrientation();
            CompanyAdapter cAdapter = new CompanyAdapter(CompanySearchResults.this, companies,searchType);

            ListView list_companies = (ListView) findViewById(R.id.listResults);

            Button newSearchButton = new Button(CompanySearchResults.this);

            Drawable background = getResources().getDrawable(R.drawable.background_color);

            if (android.os.Build.VERSION.SDK_INT >= 16)
                newSearchButton.setBackground(background);
            else
                newSearchButton.setBackgroundDrawable(background);


            newSearchButton.setHeight(getResources().getDimensionPixelSize(R.dimen.button_height));
            newSearchButton.setWidth(getResources().getDimensionPixelSize(R.dimen.width_buttons));
            newSearchButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size));
            newSearchButton.setTextColor(Color.WHITE);
            newSearchButton.setTypeface(null, Typeface.BOLD);

            if(searchType.equals("Search"))
                newSearchButton.setText(getResources().getString(R.string.new_search_button).toUpperCase());
            else
                newSearchButton.setText(getResources().getString(R.string.backsearch_button).toUpperCase());


            newSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(CompanySearchResults.this,CompanySearch.class);
                    startActivity(intent);
                }
            });

            list_companies.addFooterView(newSearchButton);

            list_companies.setAdapter(cAdapter);
            list_companies.setEmptyView(findViewById(R.id.emptyView));
            */

        }
    }
}
