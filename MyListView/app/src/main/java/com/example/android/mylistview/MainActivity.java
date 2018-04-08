package com.example.android.mylistview;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ArrayList<Contact>arrayList = new ArrayList<>();
    private ArrayAdapter arrayAdapter;
    private ListView listView;
    public static Contact contact;
    Button bt_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        buttonClick();
    }
    private void initialize() {
        bt_add =(Button) findViewById(R.id.button_add);

        listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new MyArrayAdapter(this, arrayList);
        listView.setAdapter(arrayAdapter);
    }


    private void buttonClick() {
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AnotherActivity.class);
//                startActivityForResult(intent,1);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
//                String name = data.getStringExtra("name");
//                String phone = data.getStringExtra("phone");

                arrayList.add(contact);
                arrayAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(contact != null){
            if(!arrayList.contains(contact)) {
                arrayList.add(contact);
                arrayAdapter.notifyDataSetChanged();
            }
        }
    }

}
