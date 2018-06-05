package com.example.android.safehome.UIHandler;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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

public class SignIn extends AppCompatActivity {

    private ImageView bg1_iv, bg2_iv;
    private Button signIn_bt;
    private EditText et_username, et_password;
    private TextInputLayout textInputLayout_username, textInputLayout_password;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initiateAttributes();
        handleBackgrondAnimation();
        takePermissionForTransparentStatusBar();
        handleSubmitButton();
    }

    private void handleSubmitButton() {
        signIn_bt.setOnClickListener(new View.OnClickListener() {
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
        signIn_bt = findViewById(R.id.button_signIn_submit);

        textInputLayout_username = findViewById(R.id.textInputLayout_signIn_username);
        textInputLayout_password = findViewById(R.id.textInputLayout_signIn_password);
        et_username = findViewById(R.id.editText_signIn_username);
        et_password = findViewById(R.id.editText_signIn_password);

        et_username.addTextChangedListener(new MyTextWatcher(et_username));
        et_password.addTextChangedListener(new MyTextWatcher(et_password));

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
        if (!validateUsername()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        Toast.makeText(getApplicationContext(), "Successful!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
    private boolean validatePassword() {
        if (et_password.getText().toString().trim().isEmpty()) {
            textInputLayout_password.setError(getString(R.string.err_msg_password));
            requestFocus(et_password);
            return false;
        } else {
            textInputLayout_password.setErrorEnabled(false);
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
                case R.id.editText_signIn_username:
                    validateUsername();
                    break;
                case R.id.editText_signIn_password:
                    validatePassword();
                    break;
            }
        }
    }
}

