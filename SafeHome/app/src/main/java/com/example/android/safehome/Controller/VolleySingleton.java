package com.example.android.safehome.Controller;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Activity activity;

    private VolleySingleton(Activity activity) {
        this.activity = activity;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Activity activity) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(activity);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(activity.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
