package com.example.ricardogarcia.topfreeapps.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ricardogarcia.topfreeapps.R;
import com.example.ricardogarcia.topfreeapps.activities.AppDescription;
import com.example.ricardogarcia.topfreeapps.model.App;
import com.example.ricardogarcia.topfreeapps.model.ViewHolder;

import java.util.List;

/**
 * Adapter used to show the grid/list of applications with its name and image
 * Created by ricardogarcia on 1/15/16.
 */
public class AppAdapter extends BaseAdapter {

    public static final String APP = "com.example.ricardogarcia.topfreeapps.APP";
    private LayoutInflater inflater;
    private Activity activity;
    private List<App> apps;

    public AppAdapter(Activity activity, List<App> apps) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
        this.apps = apps;
    }

    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public Object getItem(int i) {
        return apps.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vholder;
        View v = convertView;

        if (apps.size() > 0) {
            if (v == null) {
                v = inflater.inflate(R.layout.grid_row, parent, false);
                vholder = new ViewHolder();
                vholder.image = (ImageView) v.findViewById(R.id.imageView);
                vholder.text = (TextView) v.findViewById(R.id.textView);
                v.setTag(vholder);
            } else {
                vholder = (ViewHolder) v.getTag();
            }

            if ((activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
                if (apps.get(position).getSmall() != null)
                    vholder.image.setImageBitmap(BitmapFactory.decodeByteArray(apps.get(position).getSmall(), 0, apps.get(position).getSmall().length));
            } else if ((activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                if (apps.get(position).getMedium() != null)
                    vholder.image.setImageBitmap(BitmapFactory.decodeByteArray(apps.get(position).getMedium(), 0, apps.get(position).getMedium().length));
            } else {
                if (apps.get(position).getLarge() != null)
                    vholder.image.setImageBitmap(BitmapFactory.decodeByteArray(apps.get(position).getLarge(), 0, apps.get(position).getLarge().length));

            }
            vholder.text.setText(apps.get(position).getName());


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, AppDescription.class);
                    intent.putExtra(APP, apps.get(position));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(activity, (View) v, "imageApp");
                        activity.startActivity(intent, options.toBundle());
                    } else {
                        activity.startActivity(intent);
                    }
                }
            });
        }
        return v;
    }
}
