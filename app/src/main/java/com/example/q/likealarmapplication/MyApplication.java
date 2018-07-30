package com.example.q.likealarmapplication;

import android.app.Application;
import android.os.StrictMode;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MyApplication extends Application {

    public static final String CHAT_SERVER_URL = "http://52.231.70.150:8080";
    //public static final String LOCATION_SERVER_URL = "http://52.231.64.206:8080";
    public static String nickname = "";
    public static boolean is_love = false;
    public static boolean is_boring = false;
    public static boolean is_needs = false;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public static String getNickname() {
        return nickname;
    }


    public static void setNickname(String nickname) {
        MyApplication.nickname = nickname;
    }

    public static void setIs_love(Boolean a) {
        MyApplication.is_love = a;
    }
    public static void setIs_boring(Boolean a) {
        MyApplication.is_boring = a;
    }
    public static void setIs_needs(Boolean a) {
        MyApplication.is_needs = a;
    }

    public Socket getSocket() {
        return mSocket;
    }


}
