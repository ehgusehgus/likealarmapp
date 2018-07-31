package com.example.q.likealarmapplication;

import android.Manifest;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.q.likealarmapplication.ChatActivity.ChatActivity;
import com.example.q.likealarmapplication.FirstPageActivity.FirstPageActivity;
import com.example.q.likealarmapplication.SecondPageActivity.SecondPageActivity;
import com.example.q.likealarmapplication.ThirdPageActivity.Profile;
import com.example.q.likealarmapplication.ThirdPageActivity.ThirdPageActivity;
import com.example.q.likealarmapplication.UserActivity.LoginActivity;
import com.example.q.likealarmapplication.UserActivity.UserCreateActivity;
import com.facebook.AccessToken;
import com.google.gson.JsonObject;

import io.socket.emitter.Emitter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import io.socket.client.IO;
import io.socket.client.Socket;

public class MainActivity extends TabActivity {

    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.VIBRATE};
    AccessToken accessToken;
    Retrofit retrofit;
    HttpInterface httpInterface;
    Context mContext;
    String Username="";
    private Socket mSocket;
    private Boolean from_notification = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        MyApplication app = (MyApplication) getApplication();
//        mSocket = app.getSocket();
//        mSocket.on(Socket.EVENT_CONNECT, onConnect);
//        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
//        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
//        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//        mSocket.on("new location", onNewLocation);
//
//        mSocket.connect();
//
//
//        mSocket.emit("new location", "dddd");

        mContext = this;

        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(HttpInterface.BaseURL)
                .build();
        httpInterface = retrofit.create(HttpInterface.class);

        //check login
        accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null || accessToken.isExpired()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        Log.d("????","?????");
        //nickname
        if (MyApplication.nickname.equals("")) {
            retrofit2.Call<JsonObject> getUserCall = httpInterface.getUser(accessToken.getUserId());
            getUserCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject object = response.body().get("result").getAsJsonObject();
                    if (object != null) {
                        if (object.get("name") == null) {
                            Intent intent = new Intent(getApplication(), UserCreateActivity.class);
                            startActivity(intent);
                        } else {

                            Username = object.get("name").toString();
                            MyApplication.setIs_love(object.get("is_love").getAsInt() == 1);
                            MyApplication.setIs_boring(object.get("is_boring").getAsInt() == 1);
                            MyApplication.setIs_needs(object.get("is_need").getAsInt() == 1);
                        }
                    }

                    if (!hasPermissions(mContext, PERMISSIONS)) {
                        ActivityCompat.requestPermissions((Activity) mContext,
                                PERMISSIONS,
                                0);
                    } else {
                        doOncreate();
                    }

                    doOncreate();
                }

                @Override
                public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getApplication(), "FAILURE", Toast.LENGTH_LONG).show();

                    if (!hasPermissions(mContext, PERMISSIONS)) {
                        ActivityCompat.requestPermissions((Activity) mContext,
                                PERMISSIONS,
                                0);
                    } else {
                        doOncreate();
                    }

                    doOncreate();

                }
            });
        } else {
            if (!hasPermissions(mContext, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) mContext,
                        PERMISSIONS,
                        0);
            } else {
                doOncreate();
            }

            doOncreate();
        }

        String facebook_id ="";

        try{
            Intent i = getIntent();
            Bundle extras = i.getExtras();
            from_notification = extras.getBoolean("from_notification");
            MyApplication.is_loving = extras.getBoolean("is_loving");
            MyApplication.is_boringing = extras.getBoolean("is_boringing");
            MyApplication.is_needing = extras.getBoolean("is_needing");
            facebook_id = extras.getString("facebook_id");
            Log.d("boolbol", extras.getBoolean("is_loving")+ " " +extras.getBoolean("is_boringing")+" "+extras.getBoolean("is_needing"));
        }catch(Exception e){
        }

        final String facebook_id2= facebook_id;

        if(from_notification){
            LayoutInflater li = LayoutInflater.from(mContext);
            View promptsView = li.inflate(R.layout.prompts, null);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    mContext);

            // set prompts.xml to alertdialog builder

            alertDialogBuilder.setView(promptsView);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("참가",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int id) {
                                    Intent intent = new Intent(mContext, ChatActivity.class);
                                    intent.putExtra("name", Username);
                                    if(MyApplication.is_loving)
                                        intent.putExtra("type","is_loving");
                                    if(MyApplication.is_boringing)
                                        intent.putExtra("type","is_boringing");
                                    if(MyApplication.is_needing)
                                        intent.putExtra("type","is_needing");
                                    intent.putExtra("facebook_id", facebook_id2);
                                    startActivity(intent);
                                    finish();
                                    // get user input and set it to
                                    // result
                                    // edit text
                                }
                            })
                    .setNegativeButton("입구컷",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int id) {
                                    if(MyApplication.is_loving)
                                        MyApplication.is_loving = false;
                                    if(MyApplication.is_boringing)
                                        MyApplication.is_boringing = false;
                                    if(MyApplication.is_needing)
                                        MyApplication.is_needing = false;

                                    finish();
                                }
                            });

//            alertDialogBuilder.setNeutralButton("INSERT",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(
//                                DialogInterface dialog,
//                                int id) {
//                            // get user input and set it to
//                            // result
//                            // edit text
//
//                        }
//                    });


            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();


        }



    } // end of onCreate

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    //tab
    public void doOncreate() {
        final TabHost mTab = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent(this, FirstPageActivity.class);
        spec = mTab.newTabSpec("a").setIndicator("[  ON / OFF  ]").setContent(intent);
        mTab.addTab(spec);

        intent = new Intent(this, SecondPageActivity.class);
        spec = mTab.newTabSpec("b").setIndicator("[  MAP  ]").setContent(intent);
        mTab.addTab(spec);

        intent = new Intent(this, ThirdPageActivity.class);
        spec = mTab.newTabSpec("c").setIndicator("[  PROFILE  ]").setContent(intent);
        mTab.addTab(spec);


        setTabColor(mTab);
        mTab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String arg0) {
                setTabColor(mTab);
            }
        });

    }

    public static void setTabColor(TabHost tabhost) {

        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
//            tabhost.getTabWidget().getChildAt(i)
//                    .setBackgroundResource(R.drawable.round_tab); // unselected
            tabhost.getTabWidget().setStripEnabled(false);
        }
        tabhost.getTabWidget().setCurrentTab(0);
//        tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab())
//                .setBackgroundResource(R.drawable.round_tab_white); // selected
    }

//    private Emitter.Listener onConnect = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getApplicationContext(),
//                            "Connected", Toast.LENGTH_LONG).show();
//                    mSocket.emit("new location", "dddd");
//                }
//            });
//        }
//    };
//
//        private Emitter.Listener onDisconnect = new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(),
//                                "dis", Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//        };
//
//        private Emitter.Listener onConnectError = new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(),
//                                "Err", Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//        };
//
//    private Emitter.Listener onNewLocation = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            Log.d("???","??????");
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getApplicationContext(),
//                            "ENew", Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//    };
}


