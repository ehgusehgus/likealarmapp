package com.example.q.likealarmapplication.ChatActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.q.likealarmapplication.R;
import com.facebook.AccessToken;

public class ChatActivity extends AppCompatActivity {

    String Username;
    String Type;
    AccessToken accessToken;
    String Facebook_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        Bundle ex = i.getExtras();
        Username = ex.getString("name");
        Type = ex.getString("type");
        Facebook_ID = ex.getString("facebook_id");
        setContentView(R.layout.activity_chat);

    }

    public String getUsername(){
        return Username;
    }

    public String getType(){
        return Type;
    }

    public String getFacebookid(){
        accessToken = AccessToken.getCurrentAccessToken();
        return accessToken.getUserId();
    }

    public String getOhterFacebookID(){
        return Facebook_ID;
    }
}
