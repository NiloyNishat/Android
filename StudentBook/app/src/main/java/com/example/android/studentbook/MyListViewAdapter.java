package com.example.android.studentbook;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by IOT on 3/18/2018.
 */

public class MyListViewAdapter extends ArrayAdapter {
    Integer[] images;
    String[] name;
    String[] roll;
    Activity context;

    public MyListViewAdapter(Activity context, Integer[] images, String[] name, String[] roll) {
        super(context, R.layout.image_list, name);
        this.context = context;
        this.name = name;
        this.roll = roll;
        this.images = images;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;

        if(view == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.image_list, null, true);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        else{
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.imageView.setImageResource(images[position]);
        viewHolder.textviewName.setText(name[position]);
        viewHolder.textViewRoll.setText(roll[position]);


        return view;
    }

    class ViewHolder{
        ImageView imageView;
        TextView textviewName, textViewRoll;
        ViewHolder(View view){
            imageView = (ImageView) view.findViewById(R.id.image_ListView);
            textviewName = (TextView) view.findViewById(R.id.name_textView);
            textViewRoll = (TextView) view.findViewById(R.id.roll_textView);
        }
    }
}
