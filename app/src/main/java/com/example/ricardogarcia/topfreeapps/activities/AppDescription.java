package com.example.ricardogarcia.topfreeapps.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ricardogarcia.topfreeapps.R;
import com.example.ricardogarcia.topfreeapps.adapter.AppAdapter;
import com.example.ricardogarcia.topfreeapps.model.App;

/*
    Activity that shows the total description of an application
 */
public class AppDescription extends AppCompatActivity {

    private App app;
    public final static String APP_NAME = "com.example.ricardogarcia.topfreeapps.APP_NAME";
    public final static String APP_CATEGORY = "com.example.ricardogarcia.topfreeapps.APP_CATEGORY";
    public final static String APP_OWNER = "com.example.ricardogarcia.topfreeapps.APP_OWNER";
    public final static String APP_IMAGE = "com.example.ricardogarcia.topfreeapps.APP_IMAGE";
    public final static String APP_SUMMARY = "com.example.ricardogarcia.topfreeapps.APP_SUMMARY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_description);

        if(Build.VERSION.SDK_INT>=21) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



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
        TextView ownerApp= (TextView) findViewById(R.id.ownerText);

        nameApp.setText(app.getName());
        categoryApp.setText(app.getCategory());
        ownerApp.setText(app.getOwner());

        if(!Categories.pref.getBoolean(Categories.STATUS_CONNECTION,false)){
            TextView textnoconexion= (TextView) findViewById(R.id.noconnectiontitle);
            textnoconexion.setVisibility(View.VISIBLE);
            if(Build.VERSION.SDK_INT<21) {
                setTitle(getResources().getString(R.string.app_name)+" - "+getResources().getString(R.string.conexionoffline));
            }
        }

        if (app.getLarge() != null) {
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(app.getLarge(), 0, app.getLarge().length));
        }
        summaryApp.setText(app.getSummary());

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
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
