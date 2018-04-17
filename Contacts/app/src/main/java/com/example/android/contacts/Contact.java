package com.example.android.contacts;

import android.graphics.Bitmap;

/**
 * Created by IOT on 3/28/2018.
 */

public class Contact {
    String id, name, phone, photoURI,emailID, updateDate;
    Bitmap photo;
    Boolean fromGoogle = false;

    public Contact(String name, String phone, String photoURI) {
        this.name = name;
        this.phone = phone;
        this.photoURI = photoURI;
    }
    public Contact(String id, String name, String phone,String photoURI, String emailID) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.photoURI = photoURI;
        this.emailID = emailID;
    }
    public Contact(String name, String phone,String photoURI, String emailID) {
        this.name = name;
        this.phone = phone;
        this.photoURI = photoURI;
        this.emailID = emailID;
    }


}
