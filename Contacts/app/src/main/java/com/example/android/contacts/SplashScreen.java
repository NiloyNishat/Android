package com.example.android.contacts;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends Activity {
    ImageView splashImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        splashImage = (ImageView) findViewById(R.id.splash_imageView);
        Animation splashAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_transition);
        splashImage.startAnimation(splashAnimation);
        final Intent intent = new Intent(this, Homepage.class);

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