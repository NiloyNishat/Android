package com.example.android.studentbook;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Info extends Fragment {
    Button fetchButton;
    Context context;
    public static TextView jsonInfoTextView;

    public Info() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        context = getActivity();
        jsonInfoTextView = (TextView) view.findViewById(R.id.json_textView);

        fetchJsonData datafetcher = new fetchJsonData();
        datafetcher.execute();


        return view;
    }



}
