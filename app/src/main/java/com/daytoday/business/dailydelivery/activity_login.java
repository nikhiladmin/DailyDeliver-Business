package com.daytoday.business.dailydelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hbb20.CountryCodePicker;

public class activity_login extends AppCompatActivity {

    CountryCodePicker ccp;
    Button send_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ccp=findViewById(R.id.ccp);
        send_otp=(Button) findViewById(R.id.send_otp);


        //for working of send otp button
        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_login.this,verification_activity.class);
                activity_login.this.startActivity(intent);
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
