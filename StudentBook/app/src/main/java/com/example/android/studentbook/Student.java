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

    public Student(String name, String dateofbirth, String gender, String emailAdress, String contact, String username, String password) {
        this.name = name;
        this.dateofbirth = dateofbirth;
        this.gender = gender;
        this.emailAdress = emailAdress;
        this.contact = contact;
        this.username = username;
        this.password = password;
    }
}
