package com.example.android.contacts;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IOT on 3/28/2018.
 */

public class FragmentCall extends Fragment {
    View v;
    Button bt_getGoogle;
    static RecylcerViewAdapter recylcerViewAdapter;
    GoogleDB googleDB;
    RecyclerView myRecyclerView;
    Context context;
    Activity activity;
    EditText et_search;
    List <Contact> listFromDB;


    public FragmentCall() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.call_fragment, container, false);
        initializing();

        checkGoogleButton();
        runGoogleButton();
        setContact();
        applySearchOption();

        return v;
    }

    private void initializing() {
        context = getContext();
        activity = getActivity();

        bt_getGoogle = v.findViewById(R.id.button_get_google);
        googleDB = new GoogleDB(context);
        myRecyclerView = v.findViewById(R.id.call_recyclerview);
        et_search = v.findViewById(R.id.editText_search_cllFR);
    }

    @Override
    public void onResume() {
        super.onResume();
        setContact();
    }

    private void setContact() {
        if(googleDB.doesTableExist()){
            List<Contact> mylist = googleDB.getAllData();
            listFromDB = mylist;
            for(Contact c: mylist){
                c.fromGoogle = true;
                new DownloadImageTask(c).execute();
                Log.d("email", c.emailID);
            }
            handleRecyclerView(mylist);

        }


    }


    private void handleRecyclerView(List<Contact> listFromDB) {
        myRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        myRecyclerView.setAdapter(recylcerViewAdapter);

        recylcerViewAdapter = new RecylcerViewAdapter(context, listFromDB);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myRecyclerView.setAdapter(recylcerViewAdapter);
    }

    private void runGoogleButton() {
        bt_getGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),GoogleAuth.class);
                startActivity(intent);
            }
        });

    }

    private void checkGoogleButton() {
        if(listFromDB != null && listFromDB.size() == 0){
            bt_getGoogle.setText("Reload from Google");
        }
        else{
            bt_getGoogle.setText("Get Google Contacts");
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Void> {
        Contact contact;
        private DownloadImageTask(Contact contact) {
            this.contact = contact;
        }

        protected Void doInBackground(String... urls) {
            String urldisplay = contact.photoURI;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                contact.photo = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(checkPErmission())saveImage(contact);
        }
    }


    private void saveImage(Contact contact) {
        File path = Environment.getExternalStorageDirectory();
        Log.d("path", path.toString());
        File dir = new File(path + "/myContact");
        dir.mkdirs();

        File file = new File(dir, contact.name+contact.id+".png");

        OutputStream out;

        try{
            out = new FileOutputStream(file);
            if(contact.photo != null) contact.photo.compress(Bitmap.CompressFormat.PNG, 50, out);
            out.flush();
            out.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void applySearchOption() {
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }

            private void filter(String s) {
                s = s.toLowerCase();
                List<Contact> newList = new ArrayList<>();

                for(Contact contact : listFromDB){
                    String name = contact.name.toLowerCase();
                    if(name.contains(s)){
                        newList.add(contact);
                    }
                    FragmentCall.recylcerViewAdapter.setFilter(newList);

                }
            }
        });
    }

    private boolean checkPErmission() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(context, "Read contacts permission is required to function app correctly", Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                }
                return true;
            }
        }
        return false;

    }

}
