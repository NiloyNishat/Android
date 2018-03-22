package com.example.android.studentbook;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Registration extends Activity implements AdapterView.OnItemSelectedListener {
    Context context;
    String din = "",mash = "",bochor="";
    String dateOfBirth;
    Spinner dateSpinner, monthSpinner, yearSpinner;

    Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    Bitmap uImage;
    ImageView profileImage, cameraButton;
    EditText nameTV, emailTV, contactTV, usernameTV, passwordTV, confirmPasswordTV;
    Button submitButton;

    DatabaseHelper DBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        DBhelper = new DatabaseHelper(this);

        initialize();
        processArrayAdapterToHandleDate();
        handleCameraButton();
        handleSubmitButton();

    }


    private void initialize() {
        dateSpinner = (Spinner) findViewById(R.id.date_spinner);
        monthSpinner = (Spinner) findViewById(R.id.month_spinner);
        yearSpinner = (Spinner) findViewById(R.id.year_spinner);
        profileImage = (ImageView) findViewById(R.id.userImage_ImageView);
        cameraButton = (ImageView) findViewById(R.id.userIcon_ImageView);

        nameTV = (EditText) findViewById(R.id.fullName_editText);
        emailTV = (EditText) findViewById(R.id.mailId_editText);
        contactTV = (EditText) findViewById(R.id.contact_editText);
        usernameTV = (EditText) findViewById(R.id.userName_editText);
        passwordTV = (EditText) findViewById(R.id.password_editText);
        confirmPasswordTV = (EditText) findViewById(R.id.confirmPassword_editText);
        submitButton = (Button) findViewById(R.id.regi_submit_button);
    }


    private void handleCameraButton() {
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void processArrayAdapterToHandleDate() {
        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dateArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(adapter);
        dateSpinner.setOnItemSelectedListener(this);

        ArrayAdapter <CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.monthArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter2);
        monthSpinner.setOnItemSelectedListener(this);

        ArrayAdapter <CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.yearArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter3);
        yearSpinner.setOnItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.date_spinner) {
            din = dateSpinner.getItemAtPosition(position).toString();
        }
        if(spinner.getId() == R.id.month_spinner) {
            mash = monthSpinner.getItemAtPosition(position).toString();
        }
        if(spinner.getId() == R.id.year_spinner) {
            bochor = yearSpinner.getItemAtPosition(position).toString();
        }

        dateOfBirth = din + "-" + mash + "-" + bochor;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        dateOfBirth = "1-January-1980";
    }

    private  void selectImage(){
        final Item[] items = {
//                new Item("Camera", android.R.drawable.ic_menu_camera),
//                new Item("Gallery", android.R.drawable.ic_menu_gallery)
                new Item("Camera", R.drawable.cam_intent_icon),
                new Item("Gallery", R.drawable.gallery_intent_icon)
        };

        ListAdapter adapter = new ArrayAdapter<Item>(
                this,
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                items){
            public View getView(int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView)v.findViewById(android.R.id.text1);

                //Put the image on the TextView
                tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);

                //Add margin between image and text (support various screen densities)
                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);

                return v;
            }
        };


//        final CharSequence[] items={"Camera","Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
        builder.setTitle("Add Image");

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (items[i].text.equals("Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                }
                else if (items[i].text.equals("Gallery")) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
//                    startActivityForResult(intent, SELECT_FILE);

                }
            }
        });
        builder.show();
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){
            cameraButton.setVisibility(View.GONE);
            profileImage.setVisibility(View.VISIBLE);

            if(requestCode==REQUEST_CAMERA){

                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                profileImage.setImageBitmap(bmp);

            }
            else if(requestCode==SELECT_FILE && data != null && data.getData() != null){
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    profileImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleSubmitButton() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameTV.getText().toString();
                String email = emailTV.getText().toString();
                String contact = contactTV.getText().toString();
                String username = usernameTV.getText().toString();
                String password = passwordTV.getText().toString();
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.gender_radioGroup);
                int radioOption = radioGroup.getCheckedRadioButtonId();
                RadioButton gender = (RadioButton) findViewById(radioOption);

                if(checkValidityOfTheForm(name, email, contact, username, password)){
//                    Toast.makeText(getApplicationContext(), "successful!", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "your username: " + username + "\nyour password: " + password, Toast.LENGTH_LONG).show();
                    Student student = new Student(name, dateOfBirth, gender.getText().toString(), email, contact, username, password, imageViewToByte(profileImage));
                    DBhelper.insert(student);
                    setNull();
                    Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                    startActivity(intent);
                }
            }

            private void setNull() {
                nameTV.setText("");
                emailTV.setText("");
                contactTV.setText("");
                usernameTV.setText("");
                passwordTV.setText("");
                confirmPasswordTV.setText("");
                profileImage.setVisibility(View.GONE);
                profileImage.setImageBitmap(null);
                cameraButton.setVisibility(View.VISIBLE);
            }

            private byte[] imageViewToByte(ImageView profileImage) {
                Bitmap bitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                return stream.toByteArray();
            }

            private boolean checkValidityOfTheForm(String name, String email, String contact, String username, String password) {
                if(name.equals("") ||
                        email.equals("") ||
                        contact.equals("") ||
                        username.equals("") ||
                        password.equals("") ||
                        confirmPasswordTV.getText().toString().equals("")||
                        profileImage.getDrawable() == null){
                    Toast.makeText(Registration.this, "Complete all the fields!", Toast.LENGTH_LONG).show();
                    return false;
                }
                else{
                    if(!passwordTV.getText().toString().equals(confirmPasswordTV.getText().toString())){
                        Toast.makeText(Registration.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    else return true;
                }
            }
        });
    }

    public static class Item{
        public final String text;
        public final int icon;
        public Item(String text, Integer icon) {
            this.text = text;
            this.icon = icon;
        }
        @Override
        public String toString() {
            return text;
        }
    }


}

