package com.example.android.safehome.UIHandler;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.safehome.R;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class changePassword extends AppCompatActivity {
    private ImageView bg1_iv, bg2_iv, settings;
    private Button bt_submit;
    private EditText et_oldP, et_newP, et_conP;
    private TextInputLayout textInputLayout_oldP, textInputLayout_newP, textInputLayout_conP;
    Activity activity;
    private String FILE_NAME = "text.txt";

    String oldPassword = "a";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initiateAttributes();
        readFromFile();
        takePermissionForTransparentStatusBar();
        handleBackgroundAnimation();
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

    }
    private void initiateAttributes() {
        activity = this;
        activity.setTitle("");
        bg1_iv = findViewById(R.id.background_one);
        bg2_iv = findViewById(R.id.background_two);

        bt_submit = findViewById(R.id.button_cp_submit);

        textInputLayout_oldP = findViewById(R.id.textInputLayout_cp_old_password);
        textInputLayout_newP = findViewById(R.id.textInputLayout_cp_new_password);
        textInputLayout_conP = findViewById(R.id.textInputLayout_cp_con_password);
        et_oldP = findViewById(R.id.editText__cp_old_password);
        et_newP = findViewById(R.id.editText_cp_new_password);
        et_conP= findViewById(R.id.editText_cp_con_password);

        et_oldP.addTextChangedListener(new MyTextWatcher(et_oldP));
        et_newP.addTextChangedListener(new MyTextWatcher(et_newP));
        et_conP.addTextChangedListener(new MyTextWatcher(et_conP));
    }


    private void handleBackgroundAnimation() {
        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(40000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = bg1_iv.getWidth();
                final float translationX = width * progress;
                bg1_iv.setTranslationX(translationX - width);
                bg2_iv.setTranslationX(translationX);
                animator.setRepeatMode(ValueAnimator.REVERSE);
            }
        });
        animator.start();
    }

    private void takePermissionForTransparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private void submitForm() {
        if (!validateOldP()) {
            return;
        }

        if (!validateNewP()) {
            return;
        }

        if (!validateConP()) {
            return;
        }

        if(et_oldP.getText().toString().equals(oldPassword)){
            String newPassword = et_newP.getText().toString();
            String conPassword = et_conP.getText().toString();

            Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$");
            if(!PASSWORD_PATTERN.matcher(newPassword).matches()){
                Toast.makeText(activity, "your password should be at least 8 characters long and contain at least 1 number," +
                        "1 special character and 1 Capital letter.", Toast.LENGTH_LONG).show();
            }
            else {
                if (newPassword.equals(conPassword)) {
                    writeToFile(newPassword);
                } else {
                    Toast.makeText(activity, "confirm password does not match!!", Toast.LENGTH_LONG).show();
                }
            }
        }
        else {
            Toast.makeText(activity, "Wrong current password!! ", Toast.LENGTH_LONG).show();
        }

    }

    private void writeToFile(String newPassword) {
        try {
            FileOutputStream fos = activity.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(newPassword.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(activity, "Password has been changed!", Toast.LENGTH_LONG).show();
        finish();
    }

    private void readFromFile() {
        String inputString;
        try {
            BufferedReader bReader = new BufferedReader(new InputStreamReader(activity.openFileInput(FILE_NAME)));
            String line;
            StringBuilder text = new StringBuilder();
            while ((line = bReader.readLine()) != null) {
                text.append( line);
            }

            inputString = text.toString();
            if (inputString != null){
                oldPassword = inputString;
            }
            bReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateOldP() {
        if (et_oldP.getText().toString().trim().isEmpty()) {
            textInputLayout_oldP.setError("Enter current password");
            requestFocus(et_oldP);
            return false;
        }
        else {
            textInputLayout_newP.setErrorEnabled(false);

        }

        return true;
    }
    private boolean validateNewP() {
        if (et_newP.getText().toString().trim().isEmpty()) {
            textInputLayout_newP.setError("Enter new password");
            requestFocus(et_newP);
            return false;
        }
        else {
            textInputLayout_newP.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateConP() {
        if (et_conP.getText().toString().trim().isEmpty()) {
            textInputLayout_conP.setError("Enter new password again");
            requestFocus(et_conP);
            return false;
        }
        else {
            textInputLayout_conP.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editText__cp_old_password:
                    validateOldP();
                    break;
                case R.id.editText_cp_new_password:
                    validateNewP();
                    break;
                case R.id.editText_cp_con_password:
                    validateConP();
                    break;
            }
        }
    }
}
