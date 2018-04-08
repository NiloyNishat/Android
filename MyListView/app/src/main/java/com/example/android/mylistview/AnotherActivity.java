package com.example.android.mylistview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AnotherActivity extends AppCompatActivity {

    EditText et_name, et_phone;
    Button bt_save;
    String name,phone;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);

        et_name = (EditText) findViewById(R.id.name);
        et_phone = (EditText) findViewById(R.id.phoneNo);
        bt_save = (Button) findViewById(R.id.button_save);

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString();
                phone = et_phone.getText().toString();
                if(name.equals("") || phone.equals("")){
                    return;
                }
                Contact contact = new Contact(name,phone);
                MainActivity.contact = contact;

//                intent = new Intent();
//                intent.putExtra("name", name);
//                intent.putExtra("phone", phone);
//                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
////
//    @Override
//    public void onBackPressed() {
//
//    }
}
