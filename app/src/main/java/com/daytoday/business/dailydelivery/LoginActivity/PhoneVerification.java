package com.daytoday.business.dailydelivery.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.daytoday.business.dailydelivery.R;
import com.hbb20.CountryCodePicker;

public class PhoneVerification extends AppCompatActivity {

    CountryCodePicker ccp;
    Button send_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();
        ccp=findViewById(R.id.ccp);
        send_otp=(Button) findViewById(R.id.send_otp);


        //for working of send otp button
        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneVerification.this, OtpVerification.class);
                PhoneVerification.this.startActivity(intent);
            }
        });
    }


    //country code picker function starts
    public void onCountryPickerClick(View view) {

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                String selected_country_code = ccp.getSelectedCountryCode();
            }
        });

    }
    //country code picker function ends

}
