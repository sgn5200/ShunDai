package com.cqutprint.shundai.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.cqutprint.shundai.base.MainActivity;
import com.cqutprint.shundai.utils.ShareUtils;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(ShareUtils.getIsFirstLunch()){
                    startActivity(new Intent(WelcomeActivity.this,SplashActivity.class));
                }else {
                    startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                }
                finish();
            }
        },1000);
    }
}
