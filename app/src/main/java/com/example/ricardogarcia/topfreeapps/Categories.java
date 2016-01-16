package com.example.ricardogarcia.topfreeapps;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class Categories extends AppCompatActivity {

    AbsListView viewCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        if(getResources().getBoolean(R.bool.portrait_mode)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        Intent intent= getIntent();
        Bundle b=intent.getExtras();

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


    private class RetrieveFromDatabase extends AsyncTask<String,Void,ArrayList<Category>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Loading categories
        }

        @Override
        protected ArrayList<Category> doInBackground(String... strings) {
            ArrayList<Category> categories= new ArrayList<Category>();
            DatabaseHandler db = DatabaseHandler.getInstance(Categories.this);
            String status=strings[0];

            if(status.equals("Update") || (status.equals("No Update")) && db.databaseExist()){
                categories=db.getAllCategories();
            }
            return categories;
        }

        @Override
        protected void onPostExecute(ArrayList<Category> categories) {
            super.onPostExecute(categories);

            CategoryAdapter cAdapter= new CategoryAdapter(Categories.this,categories);
            viewCategories= (AbsListView) findViewById(R.id.view_categories);
            viewCategories.setAdapter(cAdapter);
            viewCategories.setEmptyView(findViewById(R.id.emptyView));


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
