package com.example.android.contacts;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by IOT on 3/28/2018.
 */

public class FragmentUser extends Fragment{
    View v;
    private TextView tv_name, tv_phone, tv_email;
    private EditText et_name, et_phone, et_email;
    private Button bt_submit;
    private FloatingActionButton fb_edit;
    private LinearLayout ll_regular, ll_edit;
    private String FILE_NAME="test.txt";


    public FragmentUser() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.user_fragment, container, false);

        initialize();
        readFromFile();
        editButtonClick();
        submitButtonClick();

        return v;
    }

    private void submitButtonClick() {
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_name.getText().toString().equals("") || et_phone.getText().toString().equals("") || et_email.getText().toString().equals("")){
                    Toast.makeText(v.getContext(), "Enter all the credentials", Toast.LENGTH_LONG).show();
                }
                else {
                    writeToFile();
                    ll_regular.setVisibility(View.VISIBLE);
                    ll_edit.setVisibility(View.GONE);
                }
            }

            private void writeToFile() {
                String input = et_name.getText().toString()+","+et_phone.getText().toString()+","+et_email.getText().toString();
                try {
                    FileOutputStream fos = getActivity().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                    fos.write(input.toString().getBytes());
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                readFromFile();
                Toast.makeText(getActivity(), "Entry is successful", Toast.LENGTH_LONG).show();
                et_name.setText("");
                et_phone.setText("");
                et_email.setText("");

            }
        });
    }

    private void editButtonClick() {
        fb_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_regular.setVisibility(View.GONE);
                ll_edit.setVisibility(View.VISIBLE);
            }
        });

    }

    private void readFromFile() {
        String inputString = "";
        try {
            BufferedReader bReader = new BufferedReader(new InputStreamReader(getActivity().openFileInput(FILE_NAME)));
            String line;
            StringBuilder text = new StringBuilder();
            while ((line = bReader.readLine()) != null) {
                text.append( line + "\n");
            }

            inputString = text.toString();
            String input[] = inputString.split(",");
            tv_name.setText(input[0]);
            tv_phone.setText(input[1]);
            tv_email.setText(input[2]);
            bReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            tv_name.setText("Your Name");
            tv_phone.setText("+880***********");
            tv_email.setText("example@gmail.com");
        }



    }

    private void initialize() {

        tv_name = (TextView) v.findViewById(R.id.textView_username);
        tv_phone = (TextView) v.findViewById(R.id.textView_userphone);
        tv_email = (TextView) v.findViewById(R.id.textView_useremail);
        et_name = (EditText) v.findViewById(R.id.editText_username);
        et_phone = (EditText) v.findViewById(R.id.editText_userphone);
        et_email = (EditText) v.findViewById(R.id.editText_useremail);
        fb_edit = (FloatingActionButton) v.findViewById(R.id.button_edit);
        bt_submit = (Button) v.findViewById(R.id.button_submit);
        ll_regular = (LinearLayout) v.findViewById(R.id.layout_reguler);
        ll_edit = (LinearLayout) v.findViewById(R.id.layout_edit);
    }

}
