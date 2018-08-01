package com.example.q.likealarmapplication.SecondPageActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.q.likealarmapplication.MainActivity;
import com.example.q.likealarmapplication.R;
import com.facebook.internal.WebDialog;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import bolts.Task;

public class SecondPageActivity extends AppCompatActivity implements OnMapReadyCallback {

    ToggleButton tb;
    GoogleMap mMap;
    double longitude;
    double latitude;
    float accuracy;
    Boolean is_checked= false;
    Boolean is_near = false;

    public static final String BROADCAST_ACTION="com.truiton.broadcast.string";

    private final MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();

    class MyBroadCastReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {

            try
            {
                // uncomment this line if you had sent some data
                Bundle extras = intent.getExtras();
                Boolean data = extras.getBoolean("bool"); // data is a key specified to intent while sending broadcast
                Log.d("!!!!", data+"");
//                Log.e(TAG, "data=="+data);
                is_near = data;
            }
            catch (Exception ex)
            {
                Log.d("!!!!", ex+"");
                ex.printStackTrace();
            }
        }
    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        // make sure to unregister your receiver after finishing of this activity
//        unregisterReceiver(myBroadCastReceiver);
//    }

    private void registerMyReceiver() {
        try
        {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_ACTION);
            registerReceiver(myBroadCastReceiver, intentFilter);
            Log.d("!!!!!", "dfsfdfdsf");
        }
        catch (Exception ex)
        {
            Log.d("!!!!!", ex+"");

            ex.printStackTrace();
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        IntentFilter filter = new IntentFilter();
//        registerMyReceiver();
//    }

    @TargetApi(26)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        Log.d("???", mapFragment.toString());
        Log.d("???", this.toString());
        mapFragment.getMapAsync(this);

        //tb = (ToggleButton) findViewById(R.id.toggle1);


        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE); //Location 객체 얻어옴
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(R.drawable.heart_off);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!is_checked) {
                        fab.setImageResource(R.drawable.heart);
                        is_checked = true;
                        // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                                100, // 통지사이의 최소 시간간격 (miliSecond)
                                1, // 통지사이의 최소 변경거리 (m)
                                mLocationListener);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                                100, // 통지사이의 최소 시간간격 (miliSecond)
                                1, // 통지사이의 최소 변경거리 (m)
                                mLocationListener);
                        onMapReady(mMap);
                    } else {
                        is_checked = false;
                        fab.setImageResource(R.drawable.heart_off);
                        lm.removeUpdates(mLocationListener);  // 미수신할때는 반드시 자원해체를 해주어야 한다.
                    }
                } catch (SecurityException ex) {
                }
            }
        });
        registerMyReceiver();

    }

    private final LocationListener mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                //여기서 위치값이 갱신되면 이벤트가 발생한다.
                //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.
                Log.d("test", "onLocationChanged, location:" + location);
                longitude = location.getLongitude(); //경도
                latitude = location.getLatitude();   //위도
                double altitude = location.getAltitude();   //고도
                accuracy = location.getAccuracy();    //정확도
                String provider = location.getProvider();   //위치제공자
                //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
                //Network 위치제공자에 의한 위치변화. Network 위치는 Gps에 비해 정확도가 많이 떨어진다.

                onMapReady(mMap);

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

    
    @Override
    public void onMapReady(final GoogleMap map) {
        mMap = map;
//        LocationManager locationManager;

        this.mMap = map;

        //지도타입 - 일반
        this.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //기본위치(63빌딩)
        LatLng position = new LatLng(latitude , longitude);

        //화면중앙의 위치와 카메라 줌비율
        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14));

        onAddMarker();

        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            mMap.setMyLocationEnabled(true);
        }

//            map.addCircle(new CircleOptions()
//                    .center(new LatLng(Location.getLatitude(), Location.getLongitude()))
//                    .radius(Location.getAccuracy())
//                    .strokeColor(Color.parseColor("#ff0000"))
//                    .fillColor(Color.parseColor("#ff0000")));
    }

//        LatLng myPlace = new LatLng(longitude, latitude);
//        Log.d("?????", longitude +"  " +latitude);
//        map.addMarker(new MarkerOptions().position(myPlace).title("Marker in MyPlace"));
//        map.moveCamera(CameraUpdateFactory.newLatLng(myPlace));

//        LatLng SEOUL = new LatLng(37.56, 126.97);
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(myPlace);
//        markerOptions.title("서울");
//        markerOptions.snippet("한국의 수도");
//        map.addMarker(markerOptions);

//        map.moveCamera(CameraUpdateFactory.newLatLng(myPlace));
//        map.animateCamera(CameraUpdateFactory.zoomTo(10));

    @TargetApi(26)
    public void onAddMarker(){
        LatLng position = new LatLng(latitude , longitude);

        //나의위치 마커
        MarkerOptions mymarker = new MarkerOptions()
                .position(position);   //마커위치

        CircleOptions circle1KM;

        // 반경 1KM원
        if(is_near) {
            circle1KM = new CircleOptions().center(position) //원점
                    .radius(100)      //반지름 단위 : m
                    .fillColor(0x220000FF)
                    .strokeWidth(0f);
        }
        else{
            circle1KM = new CircleOptions().center(position) //원점
                    .radius(100)      //반지름 단위 : m
                    .fillColor(0x22FF0000)
                    .strokeWidth(0f);
        }

        //마커추가
        this.mMap.addMarker(mymarker);

        //원추가
        this.mMap.addCircle(circle1KM);
    }

}
