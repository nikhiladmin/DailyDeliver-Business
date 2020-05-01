package com.daytoday.business.dailydelivery.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.daytoday.business.dailydelivery.MainHomeScreen.HomeScreen;
import com.daytoday.business.dailydelivery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;

public class PhoneVerification extends AppCompatActivity {

    CountryCodePicker ccp;
    Button send_otp;
    EditText phoneNo;
    EditText first;
    EditText last;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();
        ccp=findViewById(R.id.ccp);
        send_otp=(Button) findViewById(R.id.send_otp);

        phoneNo=findViewById(R.id.phoneNumber);
        first =findViewById(R.id.firstName);
        last  =findViewById(R.id.lastName);

        mAuth = FirebaseAuth.getInstance();

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String completePhoneNo="+91"+phoneNo.getText().toString();
                Intent intent = new Intent(PhoneVerification.this, OtpVerification.class);
                intent.putExtra("phoneNo",completePhoneNo);
                PhoneVerification.this.startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent loginIntent=new Intent(PhoneVerification.this, HomeScreen.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            finish();
        }
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
