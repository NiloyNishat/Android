package com.example.android.studentbook;

/**
 * Created by IOT on 3/22/2018.
 */

public class Student {
    String name;
    String dateofbirth;
    String gender;
    String emailAdress;
    String contact;
    String username;
    String password;
    byte[] image;

    public Student(String name, String dateofbirth, String gender, String emailAdress, String contact, String username, String password, byte[] image) {
        this.name = name;
        this.dateofbirth = dateofbirth;
        this.gender = gender;
        this.emailAdress = emailAdress;
        this.contact = contact;
        this.username = username;
        this.password = password;
        this.image = image;
    }
}
