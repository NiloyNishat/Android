package com.example.android.contacts;

import android.graphics.Bitmap;

/**
 * Created by IOT on 3/28/2018.
 */

public class Contact {
    String name, phone;
    Bitmap photo;

    public Contact( ) {
    }

    public Contact(String name, String phone, Bitmap photo) {
        this.name = name;
        this.phone = phone;
        this.photo = photo;
    }


}
