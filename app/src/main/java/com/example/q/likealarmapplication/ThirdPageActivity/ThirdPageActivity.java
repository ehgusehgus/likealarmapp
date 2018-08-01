package com.example.q.likealarmapplication.ThirdPageActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.q.likealarmapplication.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

public class ThirdPageActivity extends AppCompatActivity {
//    private LoginButton btn_custom_logout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_page);
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

//    btn_custom_logout = (Button) findViewById(R.id.btn_custom_logout);
//    btn_custom_logout.setOnClickListener(new View.OnClickListener() {
//
//        @Override
//        public void onClick(View view) {
//
//            LoginManager.getInstance().logOut();
//        }
//
//    });
}
