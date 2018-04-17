package com.example.android.contacts;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.PeopleScopes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by IOT on 3/28/2018.
 */

public class FragmentCall extends Fragment {
    View v;
    Button bt_getGoogle;
    static RecylcerViewAdapter recylcerViewAdapter;
    GoogleDB googleDB;
    RecyclerView myRecyclerView;


    public FragmentCall() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.call_fragment, container, false);
        bt_getGoogle = (Button) v.findViewById(R.id.button_get_google);
        googleDB = new GoogleDB(getContext());
        myRecyclerView = v.findViewById(R.id.call_recyclerview);
        checkGoogleButton();
        runGoogleButton();
        setContact();

        return v;
    }

    private void setContact() {
        if(googleDB.doesTableExist()){
            List<Contact> mylist = googleDB.getAllData();
            for(Contact c: mylist){
                c.fromGoogle = true;
                new DownloadImageTask(c).execute();
                Log.d("email", c.emailID);
            }
            handleRecyclerView(mylist);

        }


    }


    private void handleRecyclerView(List<Contact> listFromDB) {
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myRecyclerView.setAdapter(recylcerViewAdapter);

        recylcerViewAdapter = new RecylcerViewAdapter(getContext(), listFromDB);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        myRecyclerView.setLayoutManager(mLayoutManager);
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
        if(googleDB.doesTableExist()){
            bt_getGoogle.setText("Reload from Google");
        }
        else{
            bt_getGoogle.setText("Get Google Contacts");
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Void> {
        Contact contact;
        public DownloadImageTask(Contact contact) {
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

        OutputStream out = null;

        try{
            out = new FileOutputStream(file);
            contact.photo.compress(Bitmap.CompressFormat.PNG, 50, out);
            out.flush();
            out.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private boolean checkPErmission() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(getContext(), "Read contacts permission is required to function app correctly", Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                }
                return true;
            }
        }
        return false;

    }

}
