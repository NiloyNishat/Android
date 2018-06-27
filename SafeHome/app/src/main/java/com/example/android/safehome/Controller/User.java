package com.example.android.safehome.Controller;

public class User {
    public String username, password, device_id, device_type;

    public User(String username, String password, String device_id, String device_type) {
        this.username = username;
        this.password = password;
        this.device_id = device_id;
        this.device_type = device_type;
    }
}
