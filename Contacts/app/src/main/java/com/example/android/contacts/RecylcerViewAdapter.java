package com.example.android.contacts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        MyViewHolder holder = new MyViewHolder(v, context, contactList);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_name.setText(contactList.get(holder.getAdapterPosition()).name);
        holder.tv_phone.setText(contactList.get(holder.getAdapterPosition()).phone);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(contactList.get(position).photo, 0, contactList.get(position).photo.length);
        Bitmap bitmap = getImage(contactList.get(holder.getAdapterPosition()).photoURI);
        contactList.get(holder.getAdapterPosition()).photo = bitmap;
        if(bitmap == null){
            holder.image.setImageResource(R.drawable.ic_user);
        }
        else{
            holder.image.setImageBitmap(bitmap);
        }

    }

    private Bitmap getImage(String photoUri) {
        Bitmap photo = null;
        if (photoUri != null) {
            try {
                System.gc();
                photo = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(photoUri));
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG,50/100,byteArrayOutputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            photo= BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_user);

        }
        return photo;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_name;
        TextView tv_phone;
        ImageView image;
        List <Contact> contactList;
        Context context;

        public MyViewHolder(View itemView, Context context, List<Contact> contactList) {
            super(itemView);

            itemView.setOnClickListener(this);
            tv_name = (TextView) itemView.findViewById(R.id.icon_name);
            tv_phone = (TextView) itemView.findViewById(R.id.icon_phoneNo);
            image = (ImageView) itemView.findViewById(R.id.icon_image);
            this.contactList = contactList;
            this.context = context;

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Contact contact = this.contactList.get(position);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();


            Intent intent = new Intent(this.context, DetailedInfo.class);
            if(contact.photo != null) {
                contact.photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("image", byteArray);
            }
            intent.putExtra("name", contact.name);
            intent.putExtra("phone", contact.phone);
            this.context.startActivity(intent);

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


