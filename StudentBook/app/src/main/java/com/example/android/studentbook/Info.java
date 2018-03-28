package com.example.android.studentbook;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Info extends Fragment {
    ListView mylist;
    List<String> nameList,dateList, genderList, mailList, contactList, usernameList;


    public Info() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_info, container, false);
        mylist = (ListView) view.findViewById(R.id.listView2);
        createArray();
        InfoListViewAdapter myListViewAdapter = new InfoListViewAdapter(this.getActivity(), nameList,dateList,
                genderList, mailList, contactList, usernameList);
        mylist.setAdapter(myListViewAdapter);
        return  view;
    }

    private void createArray() {
        DatabaseHelper db = new DatabaseHelper(getContext());
        Cursor cursor = db.getAllData();
        nameList = new ArrayList<>();
        dateList = new ArrayList<>();
        genderList = new ArrayList<>();
        mailList = new ArrayList<>();
        contactList = new ArrayList<>();
        usernameList = new ArrayList<>();

        while (cursor.moveToNext()){
            nameList.add(cursor.getString(1));
            dateList.add(cursor.getString(2));
            genderList.add(cursor.getString(3));
            mailList.add(cursor.getString(4));
            contactList.add(cursor.getString(5));
            usernameList.add(cursor.getString(6));
        }
    }
}

