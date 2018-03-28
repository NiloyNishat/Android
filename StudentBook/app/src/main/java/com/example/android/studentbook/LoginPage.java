package com.example.android.studentbook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends Activity {
    Button registerButton, loginButton;
    EditText usernameET, passwordET;
    String username, password;
    DatabaseHelper DBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        registerButton = (Button) findViewById(R.id.register_button);

        loginButton = (Button) findViewById(R.id.signIn_button);
        usernameET = (EditText) findViewById(R.id.username_login_editText);
        passwordET = (EditText) findViewById(R.id.password_login_editText);
         username = usernameET.getText().toString();
         password = passwordET.getText().toString();
        DBhelper = new DatabaseHelper(this);
        register();
        login();
    }

     @Override
    public void onBackPressed(){
         Intent a = new Intent(Intent.ACTION_MAIN);
         a.addCategory(Intent.CATEGORY_HOME);
         a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         startActivity(a);
         finish();
    }

    private void register() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Registration.class);
                startActivity(intent);
            }
        });
    }
//
    private void login() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usernameET.getText().toString().equals("") || passwordET.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter valid username and password", Toast.LENGTH_LONG).show();
                }

                else{
                    if(checkValidity(usernameET.getText().toString(), passwordET.getText().toString())) {

                        HomeFragement hp = new HomeFragement();
                        hp.username = usernameET.getText().toString();
                        Intent intent = new Intent(getApplicationContext(), HomePage.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "wrong username or password", Toast.LENGTH_LONG).show();

                    }
                }
            }

            private boolean checkValidity(String username, String password) {
                boolean isValid = false;
                String pw = DBhelper.searchPassword(username);

                if(pw.equals(password)) isValid = true;

                return  isValid;
            }
        });
    }


}
