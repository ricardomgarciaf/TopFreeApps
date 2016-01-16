package com.example.ricardogarcia.topfreeapps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ricardogarcia on 1/15/16.
 */
public class CategoryAdapter extends BaseAdapter {

    public static final String CATEGORY = "com.example.ricardogarcia.topfreeapps.CATEGORY";
    private LayoutInflater inflater;
    private Activity activity;
    private List<Category> categories;

    public CategoryAdapter(Activity activity, List<Category> categories) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vholder;
        View v = convertView;

        if (categories.size() > 0) {
            if (v == null) {
                v = inflater.inflate(R.layout.grid_row, parent, false);
                vholder = new ViewHolder();
                vholder.image = (ImageView) v.findViewById(R.id.imageView);
                vholder.text = (TextView) v.findViewById(R.id.textView);
                v.setTag(vholder);
            } else {
                vholder = (ViewHolder) v.getTag();
            }

            if (categories.get(position).getLogo() != null)
                vholder.image.setImageBitmap(BitmapFactory.decodeByteArray(categories.get(position).getLogo(), 0, categories.get(position).getLogo().length));
            else{
                Drawable myIcon = activity.getResources().getDrawable( R.drawable.category_effect);
                if (android.os.Build.VERSION.SDK_INT >= 16)
                    vholder.image.setBackground(myIcon);
                else
                    vholder.image.setBackgroundDrawable(myIcon);
            }


            vholder.text.setText(categories.get(position).getName());

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent= new Intent(activity,Apps.class);
                    intent.putExtra(CATEGORY, categories.get(position).getName());
                    activity.startActivity(intent);
                }
            });
        }
        return v;
    }
}
