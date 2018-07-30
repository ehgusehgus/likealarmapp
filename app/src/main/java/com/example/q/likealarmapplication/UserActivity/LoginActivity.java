package com.example.q.likealarmapplication.UserActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.q.likealarmapplication.MainActivity;
import com.example.q.likealarmapplication.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends AppCompatActivity{

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //facebook login
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(getApplication(),MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "LoginCancel", Toast.LENGTH_LONG).show();
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(), "LoginError", Toast.LENGTH_LONG).show();
                Log.d("LOGIN", exception.toString());
                // App code
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}


