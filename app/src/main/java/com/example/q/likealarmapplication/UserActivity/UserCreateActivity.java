package com.example.q.likealarmapplication.UserActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.q.likealarmapplication.HttpInterface;
import com.example.q.likealarmapplication.MainActivity;
import com.example.q.likealarmapplication.MyApplication;
import com.example.q.likealarmapplication.R;
import com.facebook.AccessToken;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserCreateActivity extends AppCompatActivity{

    Button mButton;
    EditText mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_create);

        mButton = findViewById(R.id.button3);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEdit = findViewById(R.id.edittext);
                final String text = mEdit.getText().toString();
                if( text == "") {
                    Toast.makeText(getApplication(), "이름을 입력해주세요", Toast.LENGTH_LONG).show();
                    return;
                }

                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(HttpInterface.BaseURL)
                        .build();
                HttpInterface httpInterface = retrofit.create(HttpInterface.class);
                Call<JsonObject> postUserCall = httpInterface.createUser(AccessToken.getCurrentAccessToken().getUserId(), text, "0", "0", "0");
                postUserCall.enqueue(new Callback<JsonObject>(){
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject object = response.body();
                        if(object != null){
                            MyApplication.setNickname(text);
                            finish();
                        } else {
                            Toast.makeText(getApplication(), "등록실패!!", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });

                mEdit.getText().toString();
            }
        });

    }
}
