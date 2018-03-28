package com.example.android.studentbook;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URL;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragement extends Fragment {
    DatabaseHelper db;
    TextView nameTV, dateTV, genderTV, mailTV, contactTV, usernameTV, passwordTV;
    ImageView profilepicture;
    static String username;

    public HomeFragement() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        showInfo(view);
        showImage();

        return view;
    }

    private void changeHeader(View view) {
        NavigationView nv_view = (NavigationView) getActivity().findViewById(R.id.navigation_view);
        View headerView = nv_view.getHeaderView(0);
        ImageView header_pp = (ImageView) headerView.findViewById(R.id.header_pp);
        TextView header_un = (TextView) headerView.findViewById(R.id.header_un);
        TextView header_mail = (TextView) headerView.findViewById(R.id.header_mail);

        header_un.setText(username);
        header_mail.setText(mailTV.getText().toString());

        String picturePath = Environment.getExternalStorageDirectory().toString() + "/StudentBook/" + username + ".png";
        File imgFile = new File(picturePath);
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            header_pp.setImageBitmap(bitmap);
        }
    }

    private void showInfo(View view) {
        db = new DatabaseHelper(this.getContext());

        nameTV = (TextView) view.findViewById(R.id.fullname_textview);
        dateTV = (TextView) view.findViewById(R.id.dateOfbirth_textview);
        genderTV = (TextView) view.findViewById(R.id.gender_textview);
        mailTV = (TextView) view.findViewById(R.id.email_textview);
        contactTV = (TextView) view.findViewById(R.id.contact_textview);
        usernameTV = (TextView) view.findViewById(R.id.username_textview);
        passwordTV = (TextView) view.findViewById(R.id.password_textview);
        profilepicture  = (ImageView) view.findViewById(R.id.pp_imageView);

        String[] password = db.getInfo(username);



        nameTV.setText(password[0]);
        dateTV.setText(password[1]);
        genderTV.setText(password[2]);
        mailTV.setText(password[3]);
        contactTV.setText(password[4]);
        usernameTV.setText(username);
        passwordTV.setText(password[5]);
        changeHeader(view);

    }

    private void showImage() {
        String picturePath = Environment.getExternalStorageDirectory().toString() + "/StudentBook/" + username + ".png";
        File imgFile = new File(picturePath);
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            profilepicture.setImageBitmap(bitmap);
        }
        else {
                if (genderTV.getText().toString().equals("male")){
                profilepicture.setImageResource(R.drawable.male);
            }
            else{
                profilepicture.setImageResource(R.drawable.female);

            }
        }
    }
}
