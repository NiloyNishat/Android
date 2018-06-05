package com.example.android.safehome.UIHandler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.safehome.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_layout);
//        splashImage = findViewById(R.id.splash_imageView);
//        Animation splashAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_transition);
//        splashImage.startAnimation(splashAnimation);
        final Intent intent = new Intent(this, EntryPage.class);

        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1500);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        myThread.start();
    }
}
