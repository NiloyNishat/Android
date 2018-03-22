package com.example.android.sqliteapp;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDB;
    EditText editName, editSurname, editMarks, editID;
    Button addButton, viewButton, updateButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB = new DatabaseHelper(this);

        editName = (EditText) findViewById(R.id.name_et);
        editSurname = (EditText) findViewById(R.id.surname_et);
        editMarks = (EditText) findViewById(R.id.marks_et);
        editID = (EditText) findViewById(R.id.id_et);
        addButton = (Button) findViewById(R.id.addButton);
        viewButton = (Button) findViewById(R.id.viewButton);
        updateButton = (Button) findViewById(R.id.updateButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        addData();
        viewAllMethods();
        updateData();
        deleteData();
    }

    public void addData(){
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDB.insertData(editName.getText().toString(), editSurname.getText().toString(), editMarks.getText().toString());
                if(isInserted) {
                    Toast.makeText(MainActivity.this, "added", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "not added", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void viewAllMethods(){
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer buffer = new StringBuffer();
                Cursor result = myDB.getAllData();
                if(result.getCount() == 0){
                    showMessage("Error", "Nothing found!!");
                }
                else{
                    while(result.moveToNext()){
                        buffer.append("ID: " + result.getString(0)
                                + "\nName: " + result.getString(1)
                                + " " + result.getString(2)
                                + "\nMarks: " + result.getString(3) + "\n\n");
                    }
                }
                showMessage("Data", buffer.toString());
            }
        });
    }

    public  void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void updateData(){
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdated = myDB.updateData(editID.getText().toString(),
                        editName.getText().toString(), editSurname.getText().toString(),
                        editMarks.getText().toString());
                if(isUpdated) {
                    Toast.makeText(MainActivity.this, "updated", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "not updated", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void deleteData(){
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer isDeleted = myDB.delete(editID.getText().toString());
                if(isDeleted > 0) {
                    Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "not deleted", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
