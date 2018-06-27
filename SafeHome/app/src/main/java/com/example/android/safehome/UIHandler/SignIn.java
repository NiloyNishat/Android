package com.example.android.safehome.UIHandler;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.safehome.Controller.SharedPrefManager;
import com.example.android.safehome.Controller.User;
import com.example.android.safehome.Controller.VolleySingleton;
import com.example.android.safehome.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {

    private ImageView bg1_iv, bg2_iv;
    private Button signIn_bt;
    private EditText et_username, et_password;
    private TextInputLayout textInputLayout_username, textInputLayout_password;
    Activity activity;
    private String FILE_NAME = "text.txt";
    private String username = "a", password = "a";
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initiateAttributes();
        handleBackgrondAnimation();
        takePermissionForTransparentStatusBar();
        handleSubmitButton();
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
                password = inputString;
            }
            bReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        readFromFile();

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

//        new HttpAsyncTask().execute("http://192.168.2.85:8001/api/login/");


//        if(et_username.getText().toString().equals(username) && et_password.getText().toString().equals(password)){
//            Toast.makeText(getApplicationContext(), "Successful!", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            et_password.setText(null);
//            et_username.setText(null);
//            textInputLayout_password.setErrorEnabled(false);
//            textInputLayout_username.setErrorEnabled(false);
//
//        }
//
//        else {
//            Toast.makeText(activity, "Invalid credentials!!", Toast.LENGTH_LONG).show();
//        }

        handleVolley();

    }

    private void handleVolley() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.2.85:8001/api/login/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.d("response_", response);
                            Log.d("response_b", obj.getString("status_code"));

                            //if no error in response
                            if (obj.getString("status_code").equals("200")) {
                                Log.d("response_", response);

//                                Toast.makeText(activity, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                Log.d("response_i", "j ");
                                //getting the user from the response
//                                JSONObject userJson = obj.getJSONObject("user");
                                Intent intent = new Intent(activity, MainActivity.class);
                                Bundle b = new Bundle();

                                intent.putExtra("username", user.username);
                                intent.putExtra("password", user.password );
                                intent.putExtra("device_id", user.device_id);
                                intent.putExtra("device_type", user.device_type);

                                startActivity(intent);
                                et_password.setText(null);
                                et_username.setText(null);
                                textInputLayout_password.setErrorEnabled(false);
                                textInputLayout_username.setErrorEnabled(false);
                            }
                            else {
//                                Toast.makeText(activity, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                Log.d("response_e", "jjh");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String deviceID = "f50c162c6751126d";
                user = new User(et_username.getText().toString(), et_password.getText().toString(),deviceID, "1" );
                Map<String, String> params = new HashMap<>();
                params.put("username", user.username);
                params.put("password", user.password );
                params.put("device_id", user.device_id);
                params.put("device_type", user.device_type);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


//
//    private void sendRequestToAPI() {
//        String deviceID = "f50c162c6751126d";
//        JSONObject postData = new JSONObject();
//        try {
//            postData.put("username", et_username.getText().toString());
//            postData.put("password", et_password.getText().toString());
//            postData.put("device", deviceID);
//            postData.put("device_type", "1");
//
//
//            new SendDeviceDetails().execute("http://192.168.2.85:8001/api/login/", postData.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @SuppressLint("StaticFieldLeak")
//    private class SendDeviceDetails extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            String data = "";
//
//            HttpURLConnection httpURLConnection = null;
//            try {
//
//                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
//                httpURLConnection.setRequestMethod("POST");
//
//                httpURLConnection.setDoOutput(true);
//
//                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                wr.writeBytes("PostData=" + params[1]);
//                wr.flush();
//                wr.close();
//
//                InputStream in = httpURLConnection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(in);
//
//                int inputStreamData = inputStreamReader.read();
//                while (inputStreamData != -1) {
//                    char current = (char) inputStreamData;
//                    inputStreamData = inputStreamReader.read();
//                    data += current;
//                }
//                Log.d("data", data);
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (httpURLConnection != null) {
//                    httpURLConnection.disconnect();
//                }
//            }
//
//            return data;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            Log.e("result__", "dhukse"); // this is expecting a response code to be sent from your server upon receiving the POST data
//            Log.e("result__", "abcd"+result); // this is expecting a response code to be sent from your server upon receiving the POST data
//        }
//    }

//    public static String POST(String url, User user){
//        InputStream inputStream = null, inputStream1 = null;
//        String result = "";
//        try {
//
//            // 1. create HttpClient
//            HttpClient httpclient = new DefaultHttpClient();
//
//            // 2. make POST request to the given URL
//            HttpPost httpPost = new HttpPost(url);
//
//            String json = "";
//
//            // 3. build jsonObject
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("keyusername", user.username);
//            jsonObject.put("password", user.password);
//            jsonObject.put("device_id", user.device_id);
//            jsonObject.put("device_type", user.device_type);
//
//            // 4. convert JSONObject to JSON to String
//            json = jsonObject.toString();
//            Log.d("response_json", json);
//
//
//            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
//            // ObjectMapper mapper = new ObjectMapper();
//            // json = mapper.writeValueAsString(person);
//
//            // 5. set json to StringEntity
//            StringEntity se = new StringEntity(json);
//
//            // 6. set httpPost Entity
//            httpPost.setEntity(se);
//
//            // 7. Set some headers to inform server about the type of the content
//            httpPost.setHeader("Accept", "application/json");
//            httpPost.setHeader("Content-Type", "application/json");
//
//            // 8. Execute POST request to the given URL
//            HttpResponse httpResponse = httpclient.execute(httpPost);
//
//            // 9. receive response as inputStream
//            inputStream = httpResponse.getEntity().getContent();
//            String s = httpResponse.getParams().toString();
//
//            // 10. convert inputstream to string
//            if(inputStream != null)
//                result = convertInputStreamToString(inputStream);
//            else
//                result = "Did not work!";
//
//        } catch (Exception e) {
//            Log.d("InputStream", e.getLocalizedMessage());
//        }
//
//        // 11. return result
//        return result;
//    }

//    @SuppressLint("StaticFieldLeak")
//    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//            String deviceID = "f50c162c6751126d";
//            User user = new User(et_username.getText().toString(),  et_password.getText().toString(), deviceID, "1" );
//
//
//
//            return POST(urls[0],user);
//        }
//        // onPostExecute displays the results of the AsyncTask.
//        @Override
//        protected void onPostExecute(String result) {
//            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
//            Log.d("response_", result);
//        }
//    }
//    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
//        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
//        String line = "";
//        String result = "";
//        while((line = bufferedReader.readLine()) != null)
//            result += line;
//
//        inputStream.close();
//        return result;
//
//    }


    private boolean validateUsername() {
        if (et_username.getText().toString().trim().isEmpty()) {
            textInputLayout_username.setError(getString(R.string.err_msg_name));
            requestFocus(et_username);
            return false;
        }
        else {
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

