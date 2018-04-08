package com.example.android.contacts;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IOT on 3/28/2018.
 */

public class FragmentContacts extends Fragment {
    View v;
    private RecyclerView myRecyclerView;
    static RecylcerViewAdapter recylcerViewAdapter;
    static List <Contact> contactList;
    public FragmentContacts() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.contact_fragment, container, false);
        contactList = new ArrayList<>();
        check();
        myRecyclerView = (RecyclerView) v.findViewById(R.id.contact_recyclerview);
//        RecylcerViewAdapter recylcerViewAdapter = new RecylcerViewAdapter(getContext(), contactList);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.setAdapter(recylcerViewAdapter);

        recylcerViewAdapter = new RecylcerViewAdapter(getContext(), contactList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        myRecyclerView.setLayoutManager(mLayoutManager);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myRecyclerView.setAdapter(recylcerViewAdapter);
        return v;
    }


    private void getContactList() {
        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        getActivity().startManagingCursor(cursor);

        if (cursor.moveToFirst()) {
            do {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String phoneNo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            String phoneNo = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.));
            String imageId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
            Bitmap bitmap = getImage(imageId);
            contactList.add(new Contact(name,phoneNo,bitmap));
            } while (cursor.moveToNext());
        }
    }

    private Bitmap getImage(String photoUri) {
        Bitmap photo = null;
        if (photoUri != null) {
            try {
                photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(photoUri));
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

                photo.compress(Bitmap.CompressFormat.PNG,50/100,byteArrayOutputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            photo= BitmapFactory.decodeResource(v.getContext().getResources(),
                    R.drawable.ic_user);

        }
        return photo;
    }

    private void check(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    getContactList();

                }
            };
            Thread thread = new Thread(r);
            thread.start();
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                    Toast.makeText(getActivity(), "Read contacts permission is required to function app correctly", Toast.LENGTH_LONG).show();
                }else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_CONTACTS},
                            1);                }

            }
        }
    }


}