package com.example.android.studentbook;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class Images extends Fragment {
    ListView mylist;
    List <String> nameList,rollList, imageList;

    public Images() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_images, container, false);
        mylist = (ListView) view.findViewById(R.id.listView);
        createArray();
        MyListViewAdapter myListViewAdapter = new MyListViewAdapter(this.getActivity(), imageList, nameList, rollList);
        mylist.setAdapter(myListViewAdapter);
        return  view;
    }

    private void createArray() {
        DatabaseHelper db = new DatabaseHelper(getContext());
        Cursor cursor = db.getAllData();
        nameList = new ArrayList<>();
        rollList = new ArrayList<>();
        imageList = new ArrayList<>();

        while (cursor.moveToNext()){
            nameList.add(cursor.getString(1));
            rollList.add(cursor.getString(4));
            imageList.add(cursor.getString(6));
        }
    }
}
