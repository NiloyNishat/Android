package com.example.android.safehome.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD= "password";
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_DEVICE_TYPE = "device_type";

    private static SharedPrefManager mInstance;
    private static Activity activity;

    private SharedPrefManager(Activity activity) {
        this.activity = activity;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(activity);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, user.username);
        editor.putString(KEY_PASSWORD, user.password);
        editor.putString(KEY_DEVICE_ID, user.device_id);
        editor.putString(KEY_DEVICE_TYPE, user.device_type);
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_PASSWORD, null),
                sharedPreferences.getString(KEY_DEVICE_TYPE, null),
                sharedPreferences.getString(KEY_DEVICE_ID, null)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        activity.finish();
    }

}
