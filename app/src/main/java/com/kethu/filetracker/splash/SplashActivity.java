package com.kethu.filetracker.splash;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.kethu.filetracker.R;
import com.kethu.filetracker.helpers.SharedPrefHelper;
import com.kethu.filetracker.home.HomeActivity;
import com.kethu.filetracker.login.LoginActivity;

/**
 * A login screen that offers login via email/password.
 */
public class SplashActivity extends AppCompatActivity/* implements View.OnClickListener */ {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                boolean isFirstTimeLogin = SharedPrefHelper.isFirstTime(SplashActivity.this);

                if (!isFirstTimeLogin)
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                else {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                }

                finish();
            }
        }, 2000);

    }
}