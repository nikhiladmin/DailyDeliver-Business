package com.daytoday.business.dailydelivery.BasicUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.daytoday.business.dailydelivery.MainHomeScreen.View.HomeScreenActivity;
import com.daytoday.business.dailydelivery.Network.ApiInterface;
import com.daytoday.business.dailydelivery.Network.Client;
import com.daytoday.business.dailydelivery.Network.Response.YesNoResponse;
import com.daytoday.business.dailydelivery.R;
import com.daytoday.business.dailydelivery.Utilities.SaveOfflineManager;
import com.daytoday.business.dailydelivery.WalkThrough.WalkThroughActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME = 3000; /*This is the Splash screen time which is 3 seconds*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //------------ Getting Firebase Token And Updating it ----------------------------------------------//
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                Log.i("Firebase","Token is " + task.getResult());
                SaveOfflineManager.setFireBaseToken(SplashScreenActivity.this,task.getResult());
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, WalkThroughActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN_TIME);  /*This is the Code For Holding the Screen For 3 Seconds */
    }
}
