package com.example.android.studentbook;

import android.app.Activity;
import android.content.Context;
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

public class InfoListViewAdapter extends ArrayAdapter {
    List<String> nameList,dateList, genderList, mailList, contactList, usernameList;
    Activity context;

    public InfoListViewAdapter(Activity context, List<String> nameList,
                               List<String> dateList, List<String> genderList, List<String> mailList,
                               List<String> contactList, List<String> usernameList) {
        super(context, R.layout.info_list,nameList);
        this.nameList = nameList;
        this.dateList = dateList;
        this.genderList = genderList;
        this.mailList = mailList;
        this.contactList = contactList;
        this.usernameList = usernameList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        InfoViewHolder viewHolder = null;

        if(view == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.info_list, null, true);
            viewHolder = new InfoViewHolder(view);
            view.setTag(viewHolder);
        }

        else{
            viewHolder = (InfoViewHolder) view.getTag();
        }

        viewHolder.textviewN.setText(nameList.get(position));
        viewHolder.textviewU.setText("Username: " + usernameList.get(position));
        viewHolder.textviewD.setText("Date of Birth: " + dateList.get(position));
        viewHolder.textviewG.setText("Gender: " + genderList.get(position));
        viewHolder.textviewE.setText("Email ID: " + mailList.get(position));
        viewHolder.textviewC.setText("Contact Number: " + contactList.get(position));

        if(genderList.get(position).equals("Male")){
            viewHolder.imageview.setImageResource(R.drawable.male);
        }
        else {
            viewHolder.imageview.setImageResource(R.drawable.female);
        }


        return view;
    }

    class InfoViewHolder {
        TextView textviewN,textviewD,textviewG,textviewE,textviewC,textviewU;
        ImageView imageview;
        InfoViewHolder(View view){
            textviewN = (TextView) view.findViewById(R.id.nm_textView);
            textviewD = (TextView) view.findViewById(R.id.dob_textView);
            textviewG = (TextView) view.findViewById(R.id.gn_textView);
            textviewE = (TextView) view.findViewById(R.id.ml_textView);
            textviewC = (TextView) view.findViewById(R.id.cn_textView);
            textviewU = (TextView) view.findViewById(R.id.un_textView);
            imageview = (ImageView) view.findViewById(R.id.new_im_ListView);
        }
    }
}
