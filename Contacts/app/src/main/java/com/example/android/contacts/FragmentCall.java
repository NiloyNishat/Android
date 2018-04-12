package com.example.android.contacts;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by IOT on 3/28/2018.
 */

public class FragmentCall extends Fragment {
    View v;
    Button bt_getGoogle;
    private static boolean isGoogleContactAvailable = false;
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
        if(isGoogleContactAvailable){
            bt_getGoogle.setText("Reload from Google");
        }
        else{
            bt_getGoogle.setText("Get Google Contacts");
        }
    }


}
