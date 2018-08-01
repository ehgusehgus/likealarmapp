package com.example.q.likealarmapplication.ThirdPageActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.q.likealarmapplication.HttpInterface;
import com.example.q.likealarmapplication.R;
import com.facebook.AccessToken;
import com.google.gson.JsonObject;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileAppear extends AppCompatActivity {

    AccessToken accessToken;
    Retrofit retrofit;
    HttpInterface httpInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_appear);

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
    }
}
