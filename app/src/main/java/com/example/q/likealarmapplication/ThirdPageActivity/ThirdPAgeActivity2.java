package com.example.q.likealarmapplication.ThirdPageActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TabHost;

import com.example.q.likealarmapplication.R;
import com.facebook.AccessToken;
import com.facebook.CustomTabActivity;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

public class ThirdPAgeActivity2 extends TabActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_page2);

        TabHost mTabHost = getTabHost();

        mTabHost.addTab(mTabHost.newTabSpec("").setIndicator("< 프로필 >").setContent(new Intent(this,  ProfileAppear.class )));
        mTabHost.addTab(mTabHost.newTabSpec("").setIndicator("< 이상형 >").setContent(new Intent(this , IdealAppear.class )));
        mTabHost.setCurrentTab(0);

        ImageView iv = findViewById(R.id.imageView2);
        iv.setImageResource(R.drawable.profileimage);
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
