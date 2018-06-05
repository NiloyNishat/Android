package com.example.android.safehome.Controller;

import android.app.Activity;
import android.util.Log;
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

import static com.example.android.safehome.UIHandler.MainActivity.flag;

public class DoorControl {

    private MqttAndroidClient client;
    private String payload;
    private String topicStatus = "apartment/user_input/fair";
    private String topInitLogin = "apartment/login/fair";
    private String topInitStatus = "apartment/status/fair";
    private Activity activity;
    private Boolean firstTime;
    private int i=1, j = 1;
    public int fl = 9;


    public DoorControl(final Activity activity, Boolean firstTime) {
        this.activity = activity;
        this.firstTime = firstTime;
        establish();
        callBack();
    }

    private void callBack() {
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Toast.makeText(activity, "Lost Connection with the broker", Toast.LENGTH_SHORT).show();
                establish();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                MainActivity.toastFlag = true;

                String numberArray[];

                String subMsg;
                // Toast.makeText(HomeApplianceV2.this, top, Toast.LENGTH_SHORT).show();
                subMsg = new String(message.getPayload());

                if (topic.contains(topicStatus)) {
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
                if (topic.contains(topInitStatus)) {
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
//                            Toast.makeText(activity, "Locked", Toast.LENGTH_SHORT).show();
                            fl = 0;
                        }
                        else{
                            flag = 1;
                            Log.d("message flag", Integer.toString(flag));
//                            Toast.makeText(activity, "Unlocked", Toast.LENGTH_SHORT).show();
                            fl = 1;
                        }
                    }

                }
//                i++;
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

              Log.d("mess delivery", "deliveryComplete"+Integer.toString(j++));
            }
        });

    }

    public void handleDoorLock(){
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
            else{
                MainActivity.flag = -1;
                fl = -1;
            }
            MainActivity.toastFlag = false;
        }



    }


    private void establish() {
        String clientId = MqttClient.generateClientId();
        Log.d("clientId", clientId);
        String host = "tcp://182.163.112.207:1883";
        client = new MqttAndroidClient(activity, host, clientId);
        Log.d("client", client.toString());
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    try {
                        client.subscribe(topicStatus, 0);
                        client.publish(topInitLogin, "1".getBytes(), 0, false);

                        if(firstTime) Toast.makeText(activity, "Connected", Toast.LENGTH_SHORT).show();
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
