package com.daytoday.business.dailydelivery.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.daytoday.business.dailydelivery.R;

public class WelcomePage extends AppCompatActivity {
    private Button phonelogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        getSupportActionBar().hide();

        phonelogin = findViewById(R.id.btn1);

        phonelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomePage.this, activity_login.class);
                startActivity(intent);
            }
        });
    }
}
