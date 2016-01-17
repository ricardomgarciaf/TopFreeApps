package com.example.ricardogarcia.topfreeapps;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/*
    Activity that shows the total description of an application
 */
public class AppDescription extends AppCompatActivity {

    private App app;
    public final static String APP_NAME = "com.example.ricardogarcia.topfreeapps.APP_NAME";
    public final static String APP_CATEGORY = "com.example.ricardogarcia.topfreeapps.APP_CATEGORY";
    public final static String APP_IMAGE = "com.example.ricardogarcia.topfreeapps.APP_IMAGE";
    public final static String APP_SUMMARY = "com.example.ricardogarcia.topfreeapps.APP_SUMMARY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_description);

        if (getResources().getBoolean(R.bool.portrait_mode)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        Intent intent = getIntent();
        app = (App) intent.getSerializableExtra(AppAdapter.APP);

        TextView nameApp = (TextView) findViewById(R.id.nameApp);
        TextView categoryApp = (TextView) findViewById(R.id.categoryApp);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        TextView summaryApp = (TextView) findViewById(R.id.summaryApp);

        nameApp.setText(app.getName());
        categoryApp.setText(app.getCategory());

        if (app.getLarge() != null) {
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(app.getLarge(), 0, app.getLarge().length));
        }
        summaryApp.setText(app.getSummary());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_description, menu);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        TextView nameApp = (TextView) findViewById(R.id.nameApp);
        TextView categoryApp = (TextView) findViewById(R.id.categoryApp);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        TextView summaryApp = (TextView) findViewById(R.id.summaryApp);

        outState.putString(APP_NAME, nameApp.getText().toString());
        outState.putString(APP_CATEGORY, categoryApp.getText().toString());
        outState.putParcelable(APP_IMAGE, ((BitmapDrawable) imageView.getDrawable()).getBitmap());
        outState.putString(APP_SUMMARY, summaryApp.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Bitmap bitmap;
        TextView nameApp = (TextView) findViewById(R.id.nameApp);
        TextView categoryApp = (TextView) findViewById(R.id.categoryApp);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        TextView summaryApp = (TextView) findViewById(R.id.summaryApp);

        nameApp.setText(savedInstanceState.getString(APP_NAME));
        categoryApp.setText(savedInstanceState.getString(APP_CATEGORY));
        bitmap = savedInstanceState.getParcelable(APP_IMAGE);
        imageView.setImageBitmap(bitmap);
        summaryApp.setText(savedInstanceState.getString(APP_SUMMARY));
    }
}
