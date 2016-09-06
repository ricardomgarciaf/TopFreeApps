package com.example.ricardogarcia.topfreeapps.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.example.ricardogarcia.topfreeapps.R;
import com.example.ricardogarcia.topfreeapps.db.DatabaseHandler;
import com.example.ricardogarcia.topfreeapps.model.App;
import com.example.ricardogarcia.topfreeapps.model.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
    Launcher activity that loads the information from the url into the database
 */

public class Splash extends AppCompatActivity {

    public final static String INFO_CONNECTION = "com.example.ricardogarcia.topfreeapps.CONNECTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getResources().getBoolean(R.bool.portrait_mode)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        new FetchData().execute();

    }
    /*
        Fetchs the data from the URL in case there is connection to the Internet, updating those records that
        are not in the DB
     */
    private class FetchData extends AsyncTask<Void, Void, Boolean> {

        private ProgressBar prog = (ProgressBar) findViewById(R.id.pb);
        HttpURLConnection urlConnection;
        StringBuilder json_result = new StringBuilder();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prog.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {


            if (!isNetworkAvailable())
                return false;


            try {
                URL url = new URL("https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json");
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream is = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    json_result.append(line);
                }


                DatabaseHandler db = DatabaseHandler.getInstance(Splash.this);
                String json_page = json_result.toString();
                try {
                    JSONObject jsonObject = new JSONObject(json_page);
                    JSONObject feed = jsonObject.optJSONObject("feed");
                    JSONArray entries = feed.optJSONArray("entry");

                    int length_entries = entries.length();

                    for (int i = 0; i < length_entries; i++) {

                        JSONObject entry = entries.optJSONObject(i);
                        JSONObject name = entry.optJSONObject("im:name");
                        //Label of the application
                        String label_name = name.optString("label");
                        JSONArray images = entry.optJSONArray("im:image");
                        int length_images = images.length();

                        String[] dir_images = new String[length_images];
                        for (int j = 0; j < length_images; j++) {
                            JSONObject image = images.optJSONObject(j);
                            dir_images[j] = image.optString("label");
                        }

                        JSONObject summary = entry.optJSONObject("summary");
                        String label_summary = summary.optString("label");

                        JSONObject artist=entry.optJSONObject("im:artist");
                        String label_artist=artist.optString("label");

                        JSONObject category = entry.optJSONObject("category");
                        JSONObject attributes = category.optJSONObject("attributes");
                        String label_category = attributes.optString("label");


                        if (!db.isCategoryInserted(label_category))
                            db.addCategory(new Category(label_category, null));

                        if (!db.isAppInserted(label_name)) {
                            App app = new App(label_name, label_category, label_summary,label_artist,getImageFromURL(dir_images[0]), getImageFromURL(dir_images[1]), getImageFromURL(dir_images[2]));
                            db.addApp(app);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            prog.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(Splash.this, Categories.class);
            Bundle b = new Bundle();

            if (result) {
                b.putSerializable(INFO_CONNECTION, "Update");

            } else {
                b.putSerializable(INFO_CONNECTION, "No Update");

            }

            intent.putExtras(b);
            startActivity(intent);
            finish();
        }

    }

    /*
        Checks if there is access to internet in order to load information from the url
     */
    private boolean isNetworkAvailable() {

        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
            return true;
        else
            return false;

    }

    private byte[] getImageFromURL(String url) {

        try {
            InputStream in = new URL(url).openStream();
            Bitmap bmp = BitmapFactory.decodeStream(in);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
