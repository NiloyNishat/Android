package com.example.android.contacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailedInfo extends AppCompatActivity {
    TextView tv_name, tv_phone;
    ImageView Im_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_info);

        initialize();
        setNewDetails();
    }

    private void setNewDetails() {
        Bitmap bmp = null;
//        Im_image.setImageResource(getIntent().getIntExtra("img_id",00));
        tv_name.setText(getIntent().getStringExtra("name"));
        tv_phone.setText(getIntent().getStringExtra("phone"));

        if(getIntent().getByteArrayExtra("image") != null) {
            byte[] byteArray = getIntent().getByteArrayExtra("image");
             bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            Im_image.setImageBitmap(bmp);
        }

//        Im_image.setImageBitmap("im_image");
    }

    private void initialize() {
        tv_name = findViewById(R.id.d_textView_username);
        tv_phone = findViewById(R.id.d_textView_userphone);
        Im_image = findViewById(R.id.d_user_image);
    }
}
