package com.example.android.studentbook;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by IOT on 3/18/2018.
 */

public class MyListViewAdapter extends ArrayAdapter {
    List <String> name;
    List <String> images;
    List <String> roll;
    Activity context;

    public MyListViewAdapter(Activity context,List <String>  images, List <String>  name, List <String>  roll) {
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

        String picturePath = Environment.getExternalStorageDirectory().toString() + "/StudentBook/" + images.get(position) + ".png";
        File imgFile = new File(picturePath);
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            viewHolder.imageView.setImageBitmap(bitmap);
        }

        viewHolder.textviewName.setText(name.get(position));
        viewHolder.textViewRoll.setText(roll.get(position));


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
