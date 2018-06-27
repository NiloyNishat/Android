package com.example.android.safehome.Controller;

import android.app.Activity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.safehome.R;
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

public class AppliancesController {

    private MqttAndroidClient client;
    private String payload, payloadLight, payloadAC;
    private String topicStatus = "apartment/user_input/fair";
    private String topInitLogin = "apartment/login/fair";
    private String topInitStatus = "apartment/status/fair";
    private Activity activity;
    private Boolean firstTime;
    public String acMsg = "-1";
    private int i=1, j = 1;

    private ImageView imageView_ac, imageView_light;
    private TextView textView_ac, textView_light;

    public AppliancesController(final Activity activity, Boolean firstTime) {
        this.activity = activity;
        this.firstTime = firstTime;

        initialize();
        establish();
        callBack();
    }

    private void initialize() {
        imageView_ac = activity.findViewById(R.id.im_airConditioner);
        imageView_light = activity.findViewById(R.id.im_light);
        textView_ac = activity.findViewById(R.id.tv_airConditioner);
        textView_light = activity.findViewById(R.id.tv_light);
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
                String subMsg  = new String(message.getPayload());
                String numberArray[] = subMsg.split("");

                if (topic.contains(topicStatus)) {
                    handleLaterRequests(subMsg, message);
                }

                if (topic.contains(topInitStatus)) {
                    handleInitialConditions(numberArray, message);
                }
                handleDoorMessages(numberArray, message);
                i++;
            }

            private void handleDoorMessages(String[] numberArray, MqttMessage message) {
                if(i==1){
                    Log.d("message arrived", new String(message.getPayload()));
                    if(message.getPayload() != null){
                        if(numberArray[1].equals("1")){
                            flag = 0;
                            Log.d("message flag", Integer.toString(flag));
                            Toast.makeText(activity, "Locked", Toast.LENGTH_SHORT).show();
                        }
                        if (numberArray[1].equals("0")){
                            flag = 1;
                            Log.d("message flag", Integer.toString(flag));
                            Toast.makeText(activity, "Unlocked", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            private void handleLaterRequests(String subMsg, MqttMessage message) {
                Log.d("message arrived", new String(message.getPayload()));
                Log.d("message arrived no", Integer.toString(i));
                if (subMsg.contains("1")) { //door locked
                    payload = "1";
                }
                if (subMsg.contains("0")) { //door unlocked
                    payload = "0";
                }
                if (subMsg.contains("4")) {
                    Log.d("ok", "ok");//light off
                    imageView_light.setImageDrawable(activity.getResources().getDrawable(R.drawable.my_apartment_light_bulb_off));
                    textView_light.setText("Light Off");
                    MainActivity.acFlag = 4;
                    payloadLight = "5";
                }
                if (subMsg.contains("5")) { //light on

                    imageView_light.setImageDrawable(activity.getResources().getDrawable(R.drawable.my_apartment_light_bulb_on));
                    textView_light.setText("Light On");
                    payloadLight = "4";
                    MainActivity.acFlag = 5;
                }
                if (subMsg.contains("3")) { //ac on
                    imageView_ac.setImageDrawable(activity.getResources().getDrawable(R.drawable.my_apartment_air_conditioner_on));
                    textView_ac.setText("Air Cond. On");
                    payloadAC = "2";
                }
                if (subMsg.contains("2")) {  //ac off
                    imageView_ac.setImageDrawable(activity.getResources().getDrawable(R.drawable.my_apartment_air_conditioner_off));
                    textView_ac.setText("Air Cond. Off");
                    payloadAC = "3";
                }

            }

            private void handleInitialConditions(String[] numberArray, MqttMessage message) {
                if(i==1)
                    Log.d("message arrived i", new String(message.getPayload()));
                Log.d("message arrived no i", Integer.toString(i));
                if (numberArray[1].equals("0")) { //door unlocked
                    payload = "0";
                }
                if (numberArray[1].equals("1")) {  //door locked
                    payload = "1";
                }

                if (numberArray[2].equals("1")) { //ac on
                    imageView_ac.setImageDrawable(activity.getResources().getDrawable(R.drawable.my_apartment_air_conditioner_on));
                    textView_ac.setText("Air Cond. On");
                    payloadAC = "2";
                }

                if (numberArray[2].equals("0")) { //ac off
                    imageView_ac.setImageDrawable(activity.getResources().getDrawable(R.drawable.my_apartment_air_conditioner_off));
                    textView_ac.setText("Air Cond. Off");
                    payloadAC = "3";
                }

                if (numberArray[3].equals("0")) { //light on
                    imageView_light.setImageDrawable(activity.getResources().getDrawable(R.drawable.my_apartment_light_bulb_on));
                    textView_light.setText("Light On");
                    payloadLight = "4";
                }

                if (numberArray[3].equals("1")) { //light off
                    imageView_light.setImageDrawable(activity.getResources().getDrawable(R.drawable.my_apartment_light_bulb_off));
                    textView_light.setText("Light Off");
                    payloadLight = "5";
                }
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
                        client.publish(topicStatus, message);
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
                        client.publish(topicStatus, message);
                    } catch (UnsupportedEncodingException | MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                MainActivity.flag = -1;
            }
            MainActivity.toastFlag = false;
        }
    }

    public void handleAC() {
        if(client != null){
            if(payloadAC != null) {
                if (payloadAC.equals("3")) {
                    imageView_ac.setImageDrawable(activity.getResources().getDrawable(R.drawable.my_apartment_air_conditioner_on));
                    textView_ac.setText("Air Cond. On");
                    try {
                        client.publish(topicStatus, payloadAC.getBytes("UTF-8"), 0, false);
                    } catch (MqttException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    payloadAC = "2";

                } else if (payloadAC.equals("2")) {
                    imageView_ac.setImageDrawable(activity.getResources().getDrawable(R.drawable.my_apartment_air_conditioner_off));
                    textView_ac.setText("Air Cond. Off");
                    try {
                        client.publish(topicStatus, payloadAC.getBytes("UTF-8"), 0, false);
                    } catch (MqttException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    payloadAC = "3";
                }
            }
            else Toast.makeText(activity, "Device unreachable", Toast.LENGTH_SHORT).show();

        }
    }

    public void handleLight() {
        if(client.isConnected()) {
            if (payloadLight != null) {
                Log.d("acMsg2", acMsg);
                Log.d("payloadLight", payloadLight);

                if (payloadLight.equals("4")) {
                    try {
                        client.publish(topicStatus, "4".getBytes(), 0, false);
                        payloadLight = "5";
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }
                else if (payloadLight.equals("5")) {
                    try {
                        client.publish(topicStatus, "5".getBytes(), 0, false);

                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    payloadLight = "4";
                }
            }
            else{
                MainActivity.acFlag = -1;
                Toast.makeText(activity, "Device unreachable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void establish() {
        String clientId = MqttClient.generateClientId();
        Log.d("clientId", clientId);
        String host = "tcp:// ";
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
