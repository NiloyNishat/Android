package com.example.android.contacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by IOT on 3/28/2018.
 */

public class RecylcerViewAdapter extends RecyclerView.Adapter<RecylcerViewAdapter.MyViewHolder> {
    Context context;
    List <Contact> contactList;

    public RecylcerViewAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
        Collections.sort(contactList, new CustomComparator());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.icon_contact, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_name.setText(contactList.get(position).name);
        holder.tv_phone.setText(contactList.get(position).phone);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(contactList.get(position).photo, 0, contactList.get(position).photo.length);
        Bitmap bitmap = contactList.get(position).photo;
        if(bitmap == null){
            holder.image.setImageResource(R.drawable.ic_user);
        }
        else{
            holder.image.setImageBitmap(bitmap);
        }

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name;
        TextView tv_phone;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.icon_name);
            tv_phone = (TextView) itemView.findViewById(R.id.icon_phoneNo);
            image = (ImageView) itemView.findViewById(R.id.icon_image);

        }
    }

    public void setFilter (ArrayList<Contact> newList){
        contactList = new ArrayList<>();
        contactList.addAll(newList);
        Collections.sort(contactList, new CustomComparator());
        notifyDataSetChanged();
    }

    public class CustomComparator implements Comparator<Contact> {

        @Override
        public int compare(Contact o1, Contact o2) {
            return o1.name.compareTo(o2.name);
        }
    }


}


