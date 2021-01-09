package com.daytoday.business.dailydelivery.LoginActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.daytoday.business.dailydelivery.R;
import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;


public class PhoneVerification extends AppCompatActivity {

    CountryCodePicker ccp;
    Button send_otp;

    private TextInputEditText phoneNo;
    private boolean isPhoneAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        ccp = findViewById(R.id.ccp);
        send_otp = findViewById(R.id.send_otp);
        phoneNo = findViewById(R.id.editText_carrierNumber);


        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNo.getText().toString()!=null&&phoneNo.getText().toString().length() != 0) {
                    String completePhoneNo = ccp.getFullNumberWithPlus() + phoneNo.getText().toString();
                    Intent intent = new Intent(PhoneVerification.this, OtpVerificationActivity.class);
                    intent.putExtra("isPhoneAuth", true);
                    intent.putExtra("phoneNo", completePhoneNo);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();

                    startActivity(intent);
                }else{
                    phoneNo.setError("Enter phone number");
                }
            }
        });
    }
}
