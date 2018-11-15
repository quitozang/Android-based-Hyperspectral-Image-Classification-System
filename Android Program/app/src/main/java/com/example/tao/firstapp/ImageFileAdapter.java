package com.example.tao.firstapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class ImageFileAdapter extends ArrayAdapter {
    private int resourceId;

    public ImageFileAdapter(Context context, int textViewResourceId, List<ImageFile> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageFile fruit = (ImageFile) getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        ImageView fruitImage = (ImageView) view.findViewById(R.id.fruit_img);
        TextView fruitName = (TextView) view.findViewById(R.id.fruit_name);
        TextView fruitIntroduction = (TextView) view.findViewById(R.id.fruit_introduction);

        //Log.e("useForListVIew", fruit.getIntroduction());
        fruitImage.setImageResource(fruit.getImageId());
        fruitName.setText(fruit.getName());
        fruitIntroduction.setText(fruit.getIntroduction());
        return view;

    }

}
