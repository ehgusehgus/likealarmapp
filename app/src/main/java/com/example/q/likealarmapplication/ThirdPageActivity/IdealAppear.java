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

public class IdealAppear extends AppCompatActivity {

    AccessToken accessToken;
    Retrofit retrofit;
    HttpInterface httpInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ideal_appear);

        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(HttpInterface.BaseURL)
                .build();
        httpInterface = retrofit.create(HttpInterface.class);

        accessToken = AccessToken.getCurrentAccessToken();

        retrofit2.Call<JsonObject> getUserIdeal = httpInterface.getUserIdeal(accessToken.getUserId());
        getUserIdeal.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject object = response.body().get("result").getAsJsonObject();
                Log.d("???", object.toString());
                try {
                    String sex = object.get("sex").getAsString();
                    TextView tv7 = findViewById(R.id.getText7);
                    tv7.setText(sex);

                    String age = object.get("age").getAsString();
                    TextView tv8 = findViewById(R.id.getText8);
                    tv8.setText(age);

                    String height = object.get("height").getAsString();
                    TextView tv9 = findViewById(R.id.getText9);
                    tv9.setText(height);

                    String personal = object.get("personal").getAsString();
                    TextView tv10 = findViewById(R.id.getText10);
                    tv10.setText(personal);

                    String alcohol = object.get("alcohol").getAsString();
                    TextView tv11 = findViewById(R.id.getText11);
                    tv11.setText(alcohol);
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplication(), "FAILURE", Toast.LENGTH_LONG).show();
            }
        });

    }
}

