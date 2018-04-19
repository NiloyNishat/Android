package com.example.android.contacts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
    Bitmap photo = null;

    public RecylcerViewAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
        Collections.sort(contactList, new CustomComparator());
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.icon_contact, parent, false);
        return new MyViewHolder(v, context, contactList);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final int viewPosition = position;
        holder.tv_name.setText(contactList.get(holder.getAdapterPosition()).name);
        if(contactList.get(holder.getAdapterPosition()).emailID == null){
            holder.tv_phone.setText(contactList.get(holder.getAdapterPosition()).phone);
        }

        if(contactList.get(holder.getAdapterPosition()).emailID != null){
            holder.tv_phone.setText(contactList.get(holder.getAdapterPosition()).phone + "\n"
                    + contactList.get(holder.getAdapterPosition()).emailID);
        }
        Bitmap bitmap = getImage(contactList.get(holder.getAdapterPosition()) );
        contactList.get(holder.getAdapterPosition()).photo = bitmap;
        if(contactList.get(holder.getAdapterPosition()).fromGoogle){
            new DownloadImageTask(contactList.get(holder.getAdapterPosition())).execute();
            bitmap = contactList.get(holder.getAdapterPosition()).photo;
            if(contactList.get(holder.getAdapterPosition()).photo == null){
                holder.image.setVisibility(View.VISIBLE);
                holder.image.setImageBitmap(null);
                Picasso.get().load(contactList.get(holder.getAdapterPosition()).photoURI).fit().into(holder.image);
                Log.d("photoURL", contactList.get(holder.getAdapterPosition()).photoURI );
            }

        }
        if(bitmap == null){
            holder.image.setImageResource(R.drawable.ic_user);
        }
        else{
            holder.image.setImageBitmap(bitmap);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.image.setBackground(null);
            }
        }


       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Contact contact = contactList.get(viewPosition);
               ByteArrayOutputStream stream = new ByteArrayOutputStream();


               Intent intent = new Intent(context, DetailedInfo.class);
//            intent.putExtra("fragement", "");
               if(contact.photo != null) {
                   contact.photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                   byte[] byteArray = stream.toByteArray();
                   intent.putExtra("image", byteArray);
               }
               intent.putExtra("name", contact.name);
               intent.putExtra("phone", contact.phone);
               if(contact.emailID != null){
                   intent.putExtra("email", contact.emailID);
               }
               if(contact.emailID == null || contact.emailID.equals("")){
                   intent.putExtra("email", "no email");
               }
               context.startActivity(intent);
           }
       });
    }

    private Bitmap getImage(Contact contact) {
        if (contact.photoURI != null){
            if(!contact.fromGoogle) {
                try {
                    photo = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(contact.photoURI));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(contact.fromGoogle){
                photo = showImage(contact.name+contact.id);
                contact.photo = photo;
            }
        }

        else {
            photo= BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_call);
        }
        return photo;
    }

    private Bitmap showImage(String photoUri) {
        Bitmap bitmap = null;
        String picturePath = Environment.getExternalStorageDirectory().toString() + "/myContact/" + photoUri + ".png";
        File imgFile = new File(picturePath);
        if(imgFile.exists()){
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return bitmap;
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

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name;
        TextView tv_phone;
        ImageView image;
        List <Contact> contactList;
        Context context;

        private MyViewHolder(View itemView, Context context, List<Contact> contactList) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.icon_name);
            tv_phone = itemView.findViewById(R.id.icon_phoneNo);
            image = itemView.findViewById(R.id.icon_image);
            this.contactList = contactList;
            this.context = context;

        }

    }

    public void setFilter (List<Contact> newList){
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

    private class DownloadImageTask extends AsyncTask<String, Void, Void> {
        Contact contact;
        Bitmap bitmap;

        private DownloadImageTask(Contact contact) {
            this.contact = contact;
        }

        protected Void doInBackground(String... urls) {
            String urldisplay = contact.photoURI;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
//                Thread.sleep(3000);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            contact.photo = bitmap;
        }
    }
}


