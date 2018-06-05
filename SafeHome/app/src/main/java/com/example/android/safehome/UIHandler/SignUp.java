package com.example.android.safehome.UIHandler;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.safehome.R;

import java.text.DateFormat;
import java.util.Calendar;

public class SignUp extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private ImageView bg1_iv, bg2_iv;
    private Button bt_request;
    private EditText et_firstName, et_LastName, et_email, et_occupation, et_contactNo, et_username, et_dateFrom, et_dateTo;
    private TextInputLayout textInputLayout_username, textInputLayout_firstName, textInputLayout_lastName,
        textInputLayout_email, textInputLayout_contactNo, textInputLayout_occupation, textInputLayout_dateFrom, textInputLayout_dateTo;
    Activity activity;
    private int dateFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initiateAttributes();
        handleBackgrondAnimation();
        takePermissionForTransparentStatusBar();
        handleSubmitButton();
        handleRentDate();
    }

    private void handleRentDate() {
        et_dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker =  new DatePickerFragment();
                datePicker.show(getFragmentManager(), "date picker");
                dateFlag = 0;
            }
        });

        et_dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker =  new DatePickerFragment();
                datePicker.show(getFragmentManager(), "date picker");
                dateFlag = 1;
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        if(dateFlag == 0) et_dateFrom.setText(currentDateString);
        if(dateFlag == 1) et_dateTo.setText(currentDateString);
    }

    private void handleSubmitButton() {
        bt_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }

    private void initiateAttributes() {
        activity = this;
        bg1_iv = findViewById(R.id.background_one);
        bg2_iv = findViewById(R.id.background_two);
        bg1_iv.setVisibility(View.VISIBLE);
        bt_request = findViewById(R.id.button_signUp_request);

        textInputLayout_firstName = findViewById(R.id.textInputLayout_signUp_firstname);
        textInputLayout_lastName = findViewById(R.id.textInputLayout_signUp_lastname);
        textInputLayout_email = findViewById(R.id.textInputLayout_signUp_email);
        textInputLayout_username = findViewById(R.id.textInputLayout_signUp_username);
        textInputLayout_contactNo = findViewById(R.id.textInputLayout_signUp_contact);
        textInputLayout_occupation = findViewById(R.id.textInputLayout_signUp_occupation);
        textInputLayout_dateFrom = findViewById(R.id.textInputLayout_signUp_rentFrom);
        textInputLayout_dateTo = findViewById(R.id.textInputLayout_signUp_rentTo);


        et_firstName = findViewById(R.id.editText_signUp_firstname);
        et_LastName = findViewById(R.id.editText_signUp_lastname);
        et_email = findViewById(R.id.editText_signUp_email);
        et_username = findViewById(R.id.editText_signUp_username);
        et_contactNo = findViewById(R.id.editText_signUp_contact);
        et_occupation = findViewById(R.id.editText_signUp_occupation);
        et_dateFrom = findViewById(R.id.editText_signUp_rentFrom);
        et_dateTo = findViewById(R.id.editText_signUp_rentTo);

        et_firstName.addTextChangedListener(new MyTextWatcher(et_firstName));
        et_username.addTextChangedListener(new MyTextWatcher(et_username));
        et_contactNo.addTextChangedListener(new MyTextWatcher(et_contactNo));
        et_email.addTextChangedListener(new MyTextWatcher(et_email));
        et_dateFrom.addTextChangedListener(new MyTextWatcher(et_dateFrom));
        et_dateTo.addTextChangedListener(new MyTextWatcher(et_dateTo));

    }

    private void handleBackgrondAnimation() {
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
        if (!validateFirstName()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }
        if (!validateUsername()) {
            return;
        }

        if (!validateContactNo()) {
            return;
        }

        if (!validateRentFrom()) {
            return;
        }

        if (!validateRentTo()) {
            return;
        }

        Toast.makeText(getApplicationContext(), "Successful!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, EntryPage.class);
        startActivity(intent);
    }
    private boolean validateRentFrom() {
        if (et_dateFrom.getText().toString().trim().isEmpty()) {
            textInputLayout_dateFrom.setError("Set the date");
            requestFocus(et_dateFrom);
            return false;
        } else {
            textInputLayout_dateFrom.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateRentTo() {
        if (et_dateTo.getText().toString().trim().isEmpty()) {
            textInputLayout_dateTo.setError("Set the date");
            requestFocus(et_dateTo);
            return false;
        } else {
            textInputLayout_dateTo.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateFirstName() {
        if (et_firstName.getText().toString().trim().isEmpty()) {
            textInputLayout_firstName.setError("Invalid first name");
            requestFocus(et_firstName);
            return false;
        } else {
            textInputLayout_firstName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateUsername() {
        if (et_username.getText().toString().trim().isEmpty()) {
            textInputLayout_username.setError(getString(R.string.err_msg_name));
            requestFocus(et_username);
            return false;
        } else {
            textInputLayout_username.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = et_email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            textInputLayout_email.setError("Invalid email address");
            requestFocus(et_email);
            return false;
        } else {
            textInputLayout_email.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateContactNo() {
        if (et_contactNo.getText().toString().trim().isEmpty()) {
            textInputLayout_contactNo.setError("Invalid Contact No.");
            requestFocus(et_contactNo);
            return false;
        } else {
            textInputLayout_contactNo.setErrorEnabled(false);
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
                case R.id.editText_signUp_firstname:
                    validateFirstName();
                    break;
                case R.id.editText_signUp_username:
                    validateUsername();
                    break;
                case R.id.editText_signUp_contact:
                    validateContactNo();
                    break;
                case R.id.editText_signUp_email:
                    validateEmail();
                    break;
                case R.id.editText_signUp_rentFrom:
                    validateRentFrom();
                    break;
                case R.id.editText_signUp_rentTo:
                    validateRentTo();
                    break;
            }
        }
    }
}

