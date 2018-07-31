package com.example.q.likealarmapplication.FirstPageActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.q.likealarmapplication.HttpInterface;
import com.example.q.likealarmapplication.LocationService.LocationSer;
import com.example.q.likealarmapplication.MainActivity;
import com.example.q.likealarmapplication.MyApplication;
import com.example.q.likealarmapplication.R;
import com.example.q.likealarmapplication.UserActivity.UserCreateActivity;
import com.facebook.AccessToken;
import com.google.gson.JsonObject;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirstPageActivity extends AppCompatActivity {

    Context mContext;
    Retrofit retrofit;
    HttpInterface httpInterface;
    AccessToken accessToken;

    LocationSer mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        mContext = this;

        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(HttpInterface.BaseURL)
                .build();
        httpInterface = retrofit.create(HttpInterface.class);
        accessToken = AccessToken.getCurrentAccessToken();

        Switch simpleSwitch = (Switch) findViewById(R.id.switch4);

        simpleSwitch.setChecked(isServiceRunning());
        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getApplicationContext(),"Service 시작",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LocationSer.class);
                    startService(intent);
                    bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Service 끝",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),LocationSer.class);
                    stopService(intent);
                }
            }
        });

        Switch is_love_swi = (Switch) findViewById(R.id.switch1);
        Log.d("!!!", MyApplication.is_love+"");

        is_love_swi.setChecked(MyApplication.is_love);
        is_love_swi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MyApplication.is_love = isChecked;
                String a="";
                if(isChecked)
                    a="1";
                else
                    a="0";
                retrofit2.Call<JsonObject> postislove = httpInterface.isLove(accessToken.getUserId(), a);
                postislove.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject object = response.body().get("result").getAsJsonObject();
                    }
                    @Override
                    public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getApplication(), "FAILURE", Toast.LENGTH_LONG).show();

                    }
                });
            }
        });

        Switch is_boring_swi = (Switch) findViewById(R.id.switch2);
        is_boring_swi.setChecked(MyApplication.is_boring);
        is_boring_swi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MyApplication.is_boring = isChecked;
                String a="";
                if(isChecked)
                    a="1";
                else
                    a="0";
                retrofit2.Call<JsonObject> postisboring = httpInterface.isBoring(accessToken.getUserId(), a);
                postisboring.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject object = response.body().get("result").getAsJsonObject();
                    }
                    @Override
                    public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getApplication(), "FAILURE", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        Switch is_need_swi = (Switch) findViewById(R.id.switch3);
        is_need_swi.setChecked(MyApplication.is_needs);
        is_need_swi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MyApplication.is_needs = isChecked;
                String a="";
                if(isChecked)
                    a="1";
                else
                    a="0";
                retrofit2.Call<JsonObject> postisneeds = httpInterface.isNeeds(accessToken.getUserId(), a);
                postisneeds.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject object = response.body().get("result").getAsJsonObject();
                    }
                    @Override
                    public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getApplication(), "FAILURE", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public boolean isServiceRunning()
    {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if (LocationSer.class.getName().equals(service.service.getClassName()))
                return true;
        }
        return false;
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocationSer.LocalBinder binder = (LocationSer.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


}
