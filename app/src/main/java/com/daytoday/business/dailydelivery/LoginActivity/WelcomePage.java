package com.daytoday.business.dailydelivery.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.daytoday.business.dailydelivery.R;

public class WelcomePage extends AppCompatActivity {
    private Button phoneLogin;
    private Button googleLogin;
    private Button facebookLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        phoneLogin = findViewById(R.id.btn1);
        googleLogin = findViewById(R.id.btn2);
        facebookLogin = findViewById(R.id.btn3);


        phoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomePage.this, activity_login.class);
                startActivity(intent);
            }
        });
    }
}
