package com.example.android.studentbook;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by IOT on 3/19/2018.
 */

public class fetchJsonData extends AsyncTask<Object, Object, Void> {
    String data = "";
    String parsedData = "";
    String singlyParsed = "";
    @Override
    protected Void doInBackground(Object... params) {
        try {
            URL url = new URL("https://api.myjson.com/bins/v290j");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader((new InputStreamReader((inputStream))));
            String line = "";
            while (line != null){
                line = bufferedReader.readLine();
                data = data + line;

            }

            JSONArray jasonArray = new JSONArray(data);
            for(int i=0; i<jasonArray.length(); i++){
                JSONObject jsonObject = (JSONObject) jasonArray.get(i);
                singlyParsed = "Name: " + jsonObject.get("name") + "\n"
                        + "Roll: " + jsonObject.get("roll") + "\n"
                        + "Date of Birth: " + jsonObject.get("date of birth") + "\n"
                        + "Contact Number: " + jsonObject.get("contact number") + "\n"
                        + "Mail Id: " + jsonObject.get("mail id") + "\n\n\n";

                parsedData = parsedData + singlyParsed;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Info.jsonInfoTextView.setText(parsedData);

    }
}
