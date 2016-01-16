package com.example.ricardogarcia.topfreeapps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.apache.http.util.ByteArrayBuffer;
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
import java.net.URLConnection;


public class Splash extends AppCompatActivity {

    public final static String INFO_CONNECTION = "com.example.ricardogarcia.topfreeapps.CONNECTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(getResources().getBoolean(R.bool.portrait_mode)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        new FetchData().execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
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

    private class FetchData extends AsyncTask<Void, Void, Boolean> {

        private ProgressBar prog= (ProgressBar) findViewById(R.id.pb);
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
                        JSONObject category = entry.optJSONObject("category");
                        JSONObject attributes = category.optJSONObject("attributes");
                        String label_category = attributes.optString("label");

                        /*
                        ByteArrayOutputStream baos;
                        Bitmap bitmap;
                        byte[] logo;
                        switch (label_category) {
                            case "Games":
                                baos = new ByteArrayOutputStream();
                                if(android.os.Build.VERSION.SDK_INT <= 21)
                                    bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
                                else
                                    bitmap = ((BitmapDrawable) getDrawable(R.mipmap.ic_launcher)).getBitmap();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                logo = baos.toByteArray();
                                break;
                            case "Social Networking":
                                baos = new ByteArrayOutputStream();
                                if(android.os.Build.VERSION.SDK_INT <= 21)
                                    bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
                                else
                                    bitmap = ((BitmapDrawable) getDrawable(R.mipmap.ic_launcher)).getBitmap();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                logo = baos.toByteArray();
                                break;
                            case "Photo & Video":
                                baos = new ByteArrayOutputStream();
                                if(android.os.Build.VERSION.SDK_INT <= 21)
                                    bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
                                else
                                    bitmap = ((BitmapDrawable) getDrawable(R.mipmap.ic_launcher)).getBitmap();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                logo = baos.toByteArray();
                                break;
                            case "Education":
                                baos = new ByteArrayOutputStream();
                                if(android.os.Build.VERSION.SDK_INT <= 21)
                                    bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
                                else
                                    bitmap = ((BitmapDrawable) getDrawable(R.mipmap.ic_launcher)).getBitmap();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                logo = baos.toByteArray();
                                break;
                            case "Music":
                                baos = new ByteArrayOutputStream();
                                if(android.os.Build.VERSION.SDK_INT <= 21)
                                    bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
                                else
                                    bitmap = ((BitmapDrawable) getDrawable(R.mipmap.ic_launcher)).getBitmap();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                logo = baos.toByteArray();
                                break;
                            case "Entertainment":
                                baos = new ByteArrayOutputStream();
                                if(android.os.Build.VERSION.SDK_INT <= 21)
                                    bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
                                else
                                    bitmap = ((BitmapDrawable) getDrawable(R.mipmap.ic_launcher)).getBitmap();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                logo = baos.toByteArray();
                                break;
                            case "Lifestyle":
                                baos = new ByteArrayOutputStream();
                                if(android.os.Build.VERSION.SDK_INT <= 21)
                                    bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
                                else
                                    bitmap = ((BitmapDrawable) getDrawable(R.mipmap.ic_launcher)).getBitmap();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                logo = baos.toByteArray();
                                break;
                            case "Shopping":
                                baos = new ByteArrayOutputStream();
                                if(android.os.Build.VERSION.SDK_INT <= 21)
                                    bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
                                else
                                    bitmap = ((BitmapDrawable) getDrawable(R.mipmap.ic_launcher)).getBitmap();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                logo = baos.toByteArray();
                                break;
                            default:
                                baos = new ByteArrayOutputStream();
                                if(android.os.Build.VERSION.SDK_INT <= 21)
                                    bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
                                else
                                    bitmap = ((BitmapDrawable) getDrawable(R.mipmap.ic_launcher)).getBitmap();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                logo = baos.toByteArray();
                                break;
                        }
                        */

                        if (!db.isCategoryInserted(label_category))
                            db.addCategory(new Category(label_category,null));

                        /*
                        byte[] byteArray = null, byteArray1 = null, byteArray2 = null;
                        try {
                            InputStream in = new URL(dir_images[0]).openStream();
                            Bitmap bmp = BitmapFactory.decodeStream(in);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byteArray = stream.toByteArray();

                            InputStream in1 = new URL(dir_images[1]).openStream();
                            Bitmap bmp1 = BitmapFactory.decodeStream(in1);
                            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                            bmp1.compress(Bitmap.CompressFormat.PNG, 100, stream1);
                            byteArray1 = stream1.toByteArray();

                            InputStream in2 = new URL(dir_images[2]).openStream();
                            Bitmap bmp2 = BitmapFactory.decodeStream(in2);
                            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                            bmp2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
                            byteArray2 = stream2.toByteArray();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        */
                        if (!db.isAppInserted(label_name)) {
                            App app = new App(label_name, label_category, label_summary, getImageFromURL(dir_images[0]), getImageFromURL(dir_images[1]), getImageFromURL(dir_images[2]));
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

    private boolean isNetworkAvailable() {

        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
            return true;
        else
            return false;


        /*
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm!=null)
            Log.d("NOT NULL","NOT NULL");
        else
            Log.d("NULL", "NULL");

        if (cm.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                cm.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                cm.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                cm.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            // if connected with internet
            return true;

        } else if (
                cm.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        cm.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            return false;
        }
        return false;
        */
        /*
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;*/

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
