package com.daytoday.business.dailydelivery.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.daytoday.business.dailydelivery.BlankActivity;
import com.daytoday.business.dailydelivery.R;

public class verification_activity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME = 3000; /*This is the Splash screen time which is 3 seconds*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_activity);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
         getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(verification_activity.this, BlankActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN_TIME);
    }
}
