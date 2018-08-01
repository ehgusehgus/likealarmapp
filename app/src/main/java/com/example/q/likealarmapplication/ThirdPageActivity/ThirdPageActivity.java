package com.example.q.likealarmapplication.ThirdPageActivity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.q.likealarmapplication.HttpInterface;
import com.example.q.likealarmapplication.MyApplication;
import com.example.q.likealarmapplication.ProfileActivity.ProfilecreateActivity;
import com.example.q.likealarmapplication.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ThirdPageActivity extends AppCompatActivity {
//    private LoginButton btn_custom_logout;
AccessToken accessToken;
    Retrofit retrofit;
    HttpInterface httpInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_page);

        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(HttpInterface.BaseURL)
                .build();
        httpInterface = retrofit.create(HttpInterface.class);

        accessToken = AccessToken.getCurrentAccessToken();

        retrofit2.Call<JsonObject> getUserProfile = httpInterface.getUserProfile(accessToken.getUserId());
        getUserProfile.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject object = response.body().get("result").getAsJsonObject();
                Log.d("???", object.toString());
                try {
                    String name = object.get("name").getAsString();
                    TextView tv1 = findViewById(R.id.getText1);
                    tv1.setText(name);

                    String sex = object.get("sex").getAsString();
                    TextView tv2 = findViewById(R.id.getText2);
                    tv2.setText(sex);

                    String age = object.get("age").getAsString();
                    TextView tv3 = findViewById(R.id.getText3);
                    tv3.setText(age);

                    String height = object.get("height").getAsString();
                    TextView tv4 = findViewById(R.id.getText4);
                    tv4.setText(height);

                    String personal = object.get("personal").getAsString();
                    TextView tv5 = findViewById(R.id.getText5);
                    tv5.setText(personal);

                    String alcohol = object.get("alcohol").getAsString();
                    TextView tv6 = findViewById(R.id.getText6);
                    tv6.setText(alcohol);
                }
                catch(Exception e){
                }
            }
            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplication(), "FAILURE", Toast.LENGTH_LONG).show();
            }
        });

//        retrofit2.Call<JsonObject> getUserIdeal = httpInterface.getUserIdeal(accessToken.getUserId());
//        getUserIdeal.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
//                JsonObject object = response.body().get("result").getAsJsonObject();
//                Log.d("???", object.toString());
//                try {
//
//                    String sex = object.get("sex").getAsString();
//                    TextView tv7 = findViewById(R.id.getText7);
//                    tv7.setText(sex);
//
//                    String age = object.get("age").getAsString();
//                    TextView tv8 = findViewById(R.id.getText8);
//                    tv8.setText(age);
//
//                    String height = object.get("height").getAsString();
//                    TextView tv9 = findViewById(R.id.getText9);
//                    tv9.setText(height);
//
//                    String personal = object.get("personal").getAsString();
//                    TextView tv10 = findViewById(R.id.getText10);
//                    tv10.setText(personal);
//
//                    String alcohol = object.get("alcohol").getAsString();
//                    TextView tv11 = findViewById(R.id.getText11);
//                    tv11.setText(alcohol);
//                }
//                catch(Exception e){
//                }
//            }
//            @Override
//            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
//                Toast.makeText(getApplication(), "FAILURE", Toast.LENGTH_LONG).show();
//            }
//        });


    }

    public void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }

}
