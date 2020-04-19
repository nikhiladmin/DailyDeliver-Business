package com.daytoday.business.dailydelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME = 300; /*This is the Splash screen time which is 3 seconds*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this,BlankActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN_TIME);  /*This is the Code For Holding the Screen For 3 Seconds */
    }
}
