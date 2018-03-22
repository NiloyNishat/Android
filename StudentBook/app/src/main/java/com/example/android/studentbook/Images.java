package com.example.android.studentbook;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class Images extends Fragment {
    ListView mylist;
    String[] namelIst = {"Mr. A", "Miss B", "Mr. C", "Miss D", "Mr. E", "Miss F"};
    String[] rollList = {"001", "002", "003", "004", "005", "006"};
    Integer[] imageIdList = {R.drawable.male, R.drawable.female, R.drawable.male, R.drawable.female, R.drawable.male, R.drawable.female};


    public Images() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_images, container, false);
        mylist = (ListView) view.findViewById(R.id.listView);
        MyListViewAdapter myListViewAdapter = new MyListViewAdapter(this.getActivity(), imageIdList, namelIst, rollList);
        mylist.setAdapter(myListViewAdapter);
        return  view;
    }
}
