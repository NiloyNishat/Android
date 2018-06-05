package com.example.android.safehome.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.safehome.UIHandler.MainActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class DoorControl {

    //    ImageView imvDoorStatus, imvDoorHandle;
    static String host = "tcp://182.163.112.207:1883";
    String clientId;
    MqttAndroidClient client;
    Boolean isConnected = false;
    String payload;
    String topicStatus = "apartment/user_input/fair";
    String topInitLogin = "apartment/login/fair";
    String topInitStatus = "apartment/status/fair";
    Activity activity;
    Boolean firstTime;
    int i=1, j = 1;
    int flag = -1;


    public DoorControl(final Activity activity, Boolean firstTime) {
        this.activity = activity;
        this.firstTime = firstTime;
        establish();
        callBack();
    }

    public void callBack() {
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Toast.makeText(activity, "Lost Connection with the broker", Toast.LENGTH_SHORT).show();
                establish();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                MainActivity.toastFlag = true;

                String numberArray[] = new String[3];

                String top = topic;
                String subMsg;
                // Toast.makeText(HomeApplianceV2.this, top, Toast.LENGTH_SHORT).show();
                subMsg = new String(message.getPayload());

                if (top.contains(topicStatus)) {
                    //////////locked--------------
                    if (subMsg.contains("1")) {
                        payload = "1";
                    }
                    //////////////////////Unlocked--------------
                    if (subMsg.contains("0")) {
                        payload = "0";

                    }
                }
                ////get inital state
                if (top.contains(topInitStatus)) {
                    numberArray = subMsg.split("");
                    if (numberArray[1].equals("0")) {
                        payload = "0";
                    } else if (numberArray[1].equals("1")) {
                        payload = "1";
                    }
                }


                if(i==1){
                    Log.d("message arrived", new String(message.getPayload())+" "+ Integer.toString(i++));
                    if(message.getPayload() != null){
                        if(new String(message.getPayload()).equals("100")){
                            flag = 0;
                            Log.d("message flag", Integer.toString(flag));
                            Toast.makeText(activity, "Locked", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            flag = 1;
                            Log.d("message flag", Integer.toString(flag));
                            Toast.makeText(activity, "Unlocked", Toast.LENGTH_SHORT).show();

                        }
                    }

                }
//                i++;
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

              Log.d("mess delivery", "deliveryComplete"+Integer.toString(j));
                try {
                    String s = new String(token.getMessage().getPayload());
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                j++;
            }
        });

    }

    public int handleDoorLock(){
        int temp = -2;
        if(client.isConnected()){
            if(payload != null){
                Log.d("payload", payload);

                if(payload.equals("0")){
                    payload = "1";
                    byte[] encodedPayload;
                    try {
                        encodedPayload = payload.getBytes("UTF-8");
                        MqttMessage message = new MqttMessage(encodedPayload);
                        if(client.publish(topicStatus, message).isComplete()){
//                            Log.d("publish", "1");
                        }
                    } catch (UnsupportedEncodingException | MqttException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    payload = "0";
                    byte[] encodedPayload;
                    try {
                        encodedPayload = payload.getBytes("UTF-8");
                        MqttMessage message = new MqttMessage(encodedPayload);
                        if(client.publish(topicStatus, message).isComplete()){
//                            Log.d("publish", "0");
                        }
                    } catch (UnsupportedEncodingException | MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
            temp = flag;
            MainActivity.toastFlag = false;
        }


//        flag = -1;
        return temp;
    }


    public void establish() {
        // ImportantSharedPreferences.doorStatusKey=
        clientId = MqttClient.generateClientId();
        Log.d("clientId", clientId);
        client = new MqttAndroidClient(activity, host, clientId);
        Log.d("client", client.toString());
        try {
            IMqttToken token = client.connect();
           if(token != null){
//               Log.d("token", "not null");
//               Log.d("token Topic", Boolean.toString(token.getClient().isConnected()));
//               Log.d("token wire message", token.getClient().getClientId());
           }
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    try {
                        client.subscribe(topicStatus, 0);
                        client.publish(topInitLogin, "1".getBytes(), 0, false);

                        if(firstTime) Toast.makeText(activity, "Connected", Toast.LENGTH_SHORT).show();
                        isConnected = true;
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                    try {
                        client.subscribe(topInitStatus, 0);

                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    establish();
                }
            });
        } catch (MqttException e) {
//            Log.d("connection", "exception");
            e.printStackTrace();
        }
    }

    public void destroyClient(){
        if(client != null){
            client.unregisterResources();
            client.close();
        }

    }
    public void reconnect() throws MqttException {
        client.connect();
    }


}
