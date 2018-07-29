package com.example.q.likealarmapplication;

import android.Manifest;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.q.likealarmapplication.FirstPageActivity.FirstPageActivity;
import com.example.q.likealarmapplication.UserActivity.LoginActivity;
import com.example.q.likealarmapplication.UserActivity.UserCreateActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.CustomTabActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;


import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends TabActivity {

    TextView tv;
    ToggleButton tb;
    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION};
    AccessToken accessToken;
    Retrofit retrofit;
    HttpInterface httpInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        //nickname
        if(MyApplication.nickname.equals("")) {
            retrofit2.Call<JsonObject> getUserCall = httpInterface.getUser(accessToken.getUserId());
            getUserCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject object = response.body().get("result").getAsJsonObject();
                    if (object != null) {
                        if(object.get("nickname") == null){
                            Intent intent = new Intent(getApplication(),UserCreateActivity.class);
                            startActivity(intent);
                        } else {
                            String res = object.get("nickname").toString();
                            Toast.makeText(getApplication(), res, Toast.LENGTH_LONG).show();
                            MyApplication.setNickname(object.get("nickname").toString());
                        }
                    }
                }
                @Override
                public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getApplication(), "FAILURE", Toast.LENGTH_LONG).show();
                }
            });
        }


        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this,
                    PERMISSIONS,
                    0);
        } else {
            doOncreate();
        }

        doOncreate();

        Intent intent = new Intent(MainActivity.this,Location.class);
        startService(intent);


        // GPS 위치정보 수신
        tv = (TextView) findViewById(R.id.textView2);
        tv.setText("위치정보 미수신중");
        tb = (ToggleButton) findViewById(R.id.toggle1);

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE); //Location 객체 얻어옴

        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (tb.isChecked()) {
                        tv.setText("수신중..");
                        // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                                100, // 통지사이의 최소 시간간격 (miliSecond)
                                1, // 통지사이의 최소 변경거리 (m)
                                mLocationListener);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                                100, // 통지사이의 최소 시간간격 (miliSecond)
                                1, // 통지사이의 최소 변경거리 (m)
                                mLocationListener);
                    } else {
                        tv.setText("위치정보 미수신중");
                        lm.removeUpdates(mLocationListener);  // 미수신할때는 반드시 자원해체를 해주어야 한다.
                    }
                } catch (SecurityException ex) {
                }
            }
        });
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

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("test", "onLocationChanged, location:" + location);
            double longitude = location.getLongitude(); //경도
            double latitude = location.getLatitude();   //위도
            double altitude = location.getAltitude();   //고도
            float accuracy = location.getAccuracy();    //정확도
            String provider = location.getProvider();   //위치제공자
            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화. Network 위치는 Gps에 비해 정확도가 많이 떨어진다.
            tv.setText("위치정보 : " + provider + "\n위도 : " + longitude + "\n경도 : " + latitude
                    + "\n고도 : " + altitude + "\n정확도 : " + accuracy);
        }

        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };

    //tab
    public void doOncreate() {
        final TabHost mTab = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent(this, FirstPageActivity.class);
        spec = mTab.newTabSpec("a").setIndicator("aaa").setContent(intent);
        mTab.addTab(spec);
        intent = new Intent(this, FirstPageActivity.class);
        spec = mTab.newTabSpec("b").setIndicator("bbb").setContent(intent);
        mTab.addTab(spec);
        intent = new Intent(this, FirstPageActivity.class);
        spec = mTab.newTabSpec("c").setIndicator("ccc").setContent(intent);
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

}
