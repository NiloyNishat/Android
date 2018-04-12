package com.example.android.contacts;

import android.graphics.Bitmap;

/**
 * Created by IOT on 3/28/2018.
 */

public class Contact {
    String name, phone, photoURI,emailID, updateDate;
    Bitmap photo;

    public Contact(String name, String phone, Bitmap photo) {
        this.name = name;
        this.phone = phone;
        this.photo = photo;
    }
    public Contact(String name, String phone, String photoURI) {
        this.name = name;
        this.phone = phone;
        this.photoURI = photoURI;
    }
    public Contact(String name, String phone,String photoURI, String emailID) {
        this.name = name;
        this.phone = phone;
        this.emailID = emailID;
    }


}
