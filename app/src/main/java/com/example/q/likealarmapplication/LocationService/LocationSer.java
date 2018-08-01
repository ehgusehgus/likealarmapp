package com.example.q.likealarmapplication.LocationService;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.q.likealarmapplication.HttpInterface;
import com.example.q.likealarmapplication.MainActivity;
import com.example.q.likealarmapplication.MyApplication;
import com.example.q.likealarmapplication.R;
import com.example.q.likealarmapplication.SecondPageActivity.SecondPageActivity;
import com.example.q.likealarmapplication.UserActivity.UserCreateActivity;
import com.facebook.AccessToken;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationSer extends Service {
    NotificationManager Notifi_M;
    ServiceThread thread;
    Notification Notifi ;
    Socket mSocket;
    LocationManager lm;
    AccessToken accessToken;
    Retrofit retrofit;
    HttpInterface httpInterface;
    ArrayList<String> NearUser_love= new ArrayList<String>();
    ArrayList<String> NearUser_boring= new ArrayList<String>();
    ArrayList<String> NearUser_need= new ArrayList<String>();
    private Location mLocation = new Location("a");

    public static final String LOCATION_SERVER_URL = "http://52.231.70.150:8080";

    @Override
    public void onCreate(){
        super.onCreate();
        try {
            mSocket = IO.socket(LOCATION_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        accessToken = AccessToken.getCurrentAccessToken();

        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(HttpInterface.BaseURL)
                .build();
        httpInterface = retrofit.create(HttpInterface.class);

        mLocation.setLatitude(0);
        mLocation.setLongitude(0);
        mLocation.setAltitude(0);

        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("new location", onNewLocation);

        mSocket.connect();
        //mSocket.emit("new location", "dddd");

    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(android.location.Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("test", "onLocationChanged, location:" + location);
            mLocation.setLongitude(location.getLongitude()); //경도
            mLocation.setLatitude(location.getLatitude());   //위도
            mLocation.setAltitude(location.getAltitude());   //고도
            float accuracy = location.getAccuracy();    //정확도
            String provider = location.getProvider();   //위치제공자

            mSocket.emit("new location", location.getLongitude(), location.getLatitude(), location.getAltitude(), accessToken.getUserId());
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

    public class LocalBinder extends Binder {
        public LocationSer getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocationSer.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @TargetApi(26)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startForeground(1,new Notification());

        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification;

        notification = new Notification.Builder(getApplicationContext())
                .setContentTitle("")
                .setContentText("")
                .setSmallIcon(R.drawable.heart)
                .setTicker("알림!!!")
                .setChannelId("my_channel_01")
                .build();

        nm.notify(startId, notification);

        nm.cancel(startId);

        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
        return START_STICKY;
    }

    //서비스가 종료될 때 할 작업
    @TargetApi(26)
    public void onDestroy() {

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("new location", onNewLocation);

        mSocket.disconnect();

        thread.stopForever();
        thread = null;//쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.

        lm.removeUpdates(mLocationListener);

        NotificationManager mNotificationManager =
                getSystemService(NotificationManager.class);
        mNotificationManager.cancel(777);
        mNotificationManager.cancel(778);
        mNotificationManager.cancel(779);
    }

    class myServiceHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE); //Location 객체 얻어옴

            try {
                // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                        10, // 통지사이의 최소 시간간격 (miliSecond)
                        1, // 통지사이의 최소 변경거리 (m)
                        mLocationListener);
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                        10, // 통지사이의 최소 시간간격 (miliSecond)
                        1, // 통지사이의 최소 변경거리 (m)
                        mLocationListener);
            } catch (SecurityException ex) {
            }
        }
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("connected", "connected");

        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("disconnected", "disconnected");

        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("connectederror", "connectederror");

        }
    };


    @TargetApi(26)
    private Emitter.Listener onNewLocation = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.d("????", data+"");

            Location getLoc = new Location("b");
            String facebook_id="";
            try {
                Log.d("location", data.get("longitude").toString() +" " + Double.parseDouble(data.get("latitude").toString())+" " + Double.parseDouble(data.get("altitude").toString())+" " );
                Log.d("facebook2", facebook_id);
                getLoc.setLongitude(Double.parseDouble(data.get("longitude").toString()));
                getLoc.setAltitude(Double.parseDouble(data.get("altitude").toString()));
                getLoc.setLatitude(Double.parseDouble(data.get("latitude").toString()));
                facebook_id = data.get("facebook_id").toString();
            }catch(JSONException e){
            }


            final String facebook2 = facebook_id;
            Log.d("?????",mLocation.distanceTo(getLoc)+"");
            if(mLocation.distanceTo(getLoc)<=100){

                sendMyBroadCast(true);

                NotificationManager mNotificationManager =
                        getSystemService(NotificationManager.class);

                String id = "my_channel_01";
                CharSequence name = "ddd";
                String description = "DDDD";

                int importance = NotificationManager.IMPORTANCE_HIGH;

                NotificationChannel mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mNotificationManager.createNotificationChannel(mChannel);

                retrofit2.Call<JsonObject> getUserCall = httpInterface.getUser(facebook_id);
                getUserCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject object = response.body().get("result").getAsJsonObject();
                        if(MyApplication.is_love && object.get("is_love").getAsInt() == 1 && !MyApplication.is_loving){

                            if(MatchIdealUsers(accessToken.getUserId(), facebook2) && MatchIdealUsers(facebook2,accessToken.getUserId())) {

                                if (!NearUser_love.contains(facebook2))
                                    NearUser_love.add(facebook2);

                                Intent intent = new Intent(LocationSer.this, MainActivity.class);
                                intent.putExtra("from_notification", true);
                                intent.putExtra("is_loving", true);
                                intent.putExtra("facebook_id", facebook2);
                                final PendingIntent pendingIntent = PendingIntent.getActivity(LocationSer.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                                Notifi = new Notification.Builder(getApplicationContext())
                                        .setContentTitle("연애")
                                        .setContentText("Content Text")
                                        .setSmallIcon(R.drawable.heart)
                                        .setTicker("알림!!!")
                                        .setContentIntent(pendingIntent)
                                        .setChannelId("my_channel_01")
                                        .setVibrate(new long[]{1000, 1000})
                                        .build();
                                //소리추가
                                Notifi.defaults |= Notification.DEFAULT_SOUND;
                                //알림 소리를 한번만 내도록
                                Notifi.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
                                //확인하면 자동으로 알림이 제거 되도록
                                Notifi.flags |= Notification.FLAG_AUTO_CANCEL;
                                Notifi_M.notify(777, Notifi);
                                //토스트 띄우기
                            }
                        }
                        if(MyApplication.is_boring && (object.get("is_boring").getAsInt() == 1 && !MyApplication.is_boringing)){

                            if(!NearUser_boring.contains(facebook2))
                                NearUser_boring.add(facebook2);

                            Intent intent = new Intent(LocationSer.this, MainActivity.class);
                            intent.putExtra("from_notification", true);
                            intent.putExtra("is_boringing", true);
                            intent.putExtra("facebook_id", facebook2);

                            final PendingIntent pendingIntent = PendingIntent.getActivity(LocationSer.this, 1, intent,PendingIntent.FLAG_UPDATE_CURRENT);

                            Notifi = new Notification.Builder(getApplicationContext())
                                    .setContentTitle("심심")
                                    .setContentText("Content Text")
                                    .setSmallIcon(R.drawable.heart)
                                    .setTicker("알림!!!")
                                    .setContentIntent(pendingIntent)
                                    .setChannelId("my_channel_01")
                                    .setVibrate(new long[]{1000, 1000})
                                    .build();

                            //소리추가
                            Notifi.defaults |= Notification.DEFAULT_SOUND;
                            //알림 소리를 한번만 내도록
                            Notifi.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
                            //확인하면 자동으로 알림이 제거 되도록
                            Notifi.flags |= Notification.FLAG_AUTO_CANCEL;
                            Notifi_M.notify( 778 , Notifi);
                            //토스트 띄우기
                        }
                        if(MyApplication.is_needs && (object.get("is_need").getAsInt() == 1 && !MyApplication.is_needing)){

                            if(!NearUser_need.contains(facebook2))
                                NearUser_need.add(facebook2);

                            Intent intent = new Intent(LocationSer.this, MainActivity.class);
                            intent.putExtra("from_notification", true);
                            intent.putExtra("is_needing", true);
                            intent.putExtra("facebook_id", facebook2);

                            final PendingIntent pendingIntent = PendingIntent.getActivity(LocationSer.this, 2, intent,PendingIntent.FLAG_UPDATE_CURRENT);

                            Notifi = new Notification.Builder(getApplicationContext())
                                    .setContentTitle("구인")
                                    .setContentText("Content Text")
                                    .setSmallIcon(R.drawable.heart)
                                    .setTicker("알림!!!")
                                    .setContentIntent(pendingIntent)
                                    .setChannelId("my_channel_01")
                                    .setVibrate(new long[]{1000, 1000})
                                    .build();

                            //소리추가
                            Notifi.defaults |= Notification.DEFAULT_SOUND;
                            //알림 소리를 한번만 내도록
                            Notifi.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
                            //확인하면 자동으로 알림이 제거 되도록
                            Notifi.flags |= Notification.FLAG_AUTO_CANCEL;

                            Notifi_M.notify( 779 , Notifi);
                            //토스트 띄우기
                        }
                    }
                    @Override
                    public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getApplication(), "FAILURE22", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else{
                if(NearUser_love.contains(facebook_id)){
                    Log.d("####", facebook_id);
                    NearUser_love.remove(facebook_id);
                }
                if(NearUser_boring.contains(facebook_id)){
                    NearUser_boring.remove(facebook_id);
                }
                if(NearUser_need.contains(facebook_id)){
                    NearUser_need.remove(facebook_id);
                }
                NotificationManager mNotificationManager =
                        getSystemService(NotificationManager.class);
                if(NearUser_love.size() == 0) {
                    mNotificationManager.cancel(777);
                }
                if(NearUser_boring.size() == 0)
                    mNotificationManager.cancel(778);
                if(NearUser_need.size() == 0)
                    mNotificationManager.cancel(779);
                if(NearUser_love.size() == 0 && NearUser_boring.size() == 0 && NearUser_need.size() == 0)
                    sendMyBroadCast(false);
            }
        }
    };

    public boolean MatchIdealUsers(String facebook_mine, final String facebook_other){

        Call<JsonObject> call = httpInterface.getUserProfile(facebook_mine);
        JsonObject mine= null;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        try {

            mine = call.execute().body().get("result").getAsJsonObject();
            //mine = getUserProfile.execute().body().get("result").getAsJsonObject();
        }catch (Exception e){
            Log.d("???", e+"");
            return false;
        }
//        mine = mine_2.get("result").getAsJsonObject();

        final String myname = mine.get("name").getAsString();
        final String mysex = mine.get("sex").getAsString();
        final String myage = mine.get("age").getAsString();
        final String myheight = mine.get("height").getAsString();
        String mypersonal = mine.get("personal").getAsString();
        ArrayList<String> mypersonallist= new ArrayList<String>();
        StringTokenizer s = new StringTokenizer(mypersonal);
        while(s.hasMoreTokens()) {
            mypersonallist.add(s.nextToken(","));
        }
        final String myalcohol = mine.get("alcohol").getAsString();


        retrofit2.Call<JsonObject> getUserIdeal = httpInterface.getUserIdeal(facebook_other);
        JsonObject your_a;
        JsonObject your;
        try {
            your = getUserIdeal.execute().body().get("result").getAsJsonObject();

        }catch (Exception e){

            return false;
        }
//        your = your_a.get("result").getAsJsonObject();

        String yoursex = your.get("sex").getAsString();
        ArrayList<String> yoursexlist= new ArrayList<String>();
        StringTokenizer s0 = new StringTokenizer(yoursex);
        while(s0.hasMoreTokens()) {
            yoursexlist.add(s0.nextToken(","));
        }

        String yourage = your.get("age").getAsString();
        ArrayList<String> youragelist= new ArrayList<String>();
        StringTokenizer s2 = new StringTokenizer(yourage);
        Log.d("?????", yourage);
        while(s2.hasMoreTokens()) {

            youragelist.add(s2.nextToken("~"));
        }

        String yourheight = your.get("height").getAsString();

        ArrayList<String> yourheightlist= new ArrayList<String>();
        StringTokenizer s3 = new StringTokenizer(yourheight);
        while(s3.hasMoreTokens()) {
            yourheightlist.add(s3.nextToken("~"));
        }

        String yourpersonal = your.get("personal").getAsString();

        ArrayList<String> yourpersoanllist= new ArrayList<String>();
        StringTokenizer s4 = new StringTokenizer(yourpersonal);
        while(s4.hasMoreTokens()) {
            yourpersoanllist.add(s4.nextToken("~"));
        }

        String youralcohol = your.get("alcohol").getAsString();
        ArrayList<String> youralcohollist= new ArrayList<String>();
        StringTokenizer s5 = new StringTokenizer(youralcohol);
        while(s5.hasMoreTokens()) {
            youralcohollist.add(s5.nextToken("~"));

        }

        if(yoursexlist.indexOf(mysex) == -1){
            Log.d("???", yoursexlist.toString());
            Log.d("???", "sex");
            return false;
        }
        if(!(Integer.parseInt(youragelist.get(0)) <=Integer.parseInt(myage) && Integer.parseInt(youragelist.get(1)) >=Integer.parseInt(myage))){
            Log.d("???", "age");
            return false;
        }
        if(!(Integer.parseInt(yourheightlist.get(0)) <=Integer.parseInt(myheight) && Integer.parseInt(yourheightlist.get(1)) >=Integer.parseInt(myheight))){
            Log.d("???", "height");
            return false;
        }

        if(yourpersoanllist.indexOf("상관없음") == -1) {
            int a=0;
            for(int i=0; i<mypersonallist.size(); i++) {
                if (yourpersoanllist.indexOf(mypersonallist.get(i)) != -1) {
                    a=a+1;
                }
            }
            if(a==0) {
                Log.d("???", "persoanl");
                return false;
            }
        }
        if(youralcohollist.indexOf("상관없음") == -1) {
            if (youralcohollist.indexOf(myalcohol) == -1) {
                return false;
            }
        }
        return true;
    }

    private void sendMyBroadCast(Boolean a)
    {
        try
        {
            Intent broadCastIntent = new Intent();
            broadCastIntent.putExtra("bool", a);
            broadCastIntent.setAction(SecondPageActivity.BROADCAST_ACTION);
            sendBroadcast(broadCastIntent);
            Log.d("!!!!", "send");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

