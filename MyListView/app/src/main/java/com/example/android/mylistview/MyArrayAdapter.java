package com.example.android.mylistview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by IOT on 4/2/2018.
 */

public class MyArrayAdapter extends ArrayAdapter<Contact> {
    public MyArrayAdapter(@NonNull Context context, List<Contact> contactList) {
        super(context, R.layout.list_item, contactList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        String name = getItem(position).name;
        String phone = getItem(position).phoneNo;
        TextView tv_name = (TextView) view.findViewById(R.id.list_name);
        TextView tv_phone = (TextView) view.findViewById(R.id.list_phone);

        for(int i=0; i<getCount(); i++){

        }

        tv_name.setText(name);
        tv_phone.setText(phone);

        return view;


    }
}
