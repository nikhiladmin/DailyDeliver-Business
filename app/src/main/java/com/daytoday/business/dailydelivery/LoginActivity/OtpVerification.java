package com.daytoday.business.dailydelivery.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.daytoday.business.dailydelivery.MainHomeScreen.HomeScreen;
import com.daytoday.business.dailydelivery.R;

public class OtpVerification extends AppCompatActivity {
    EditText e1, e2, e3, e4, e5, e6;
    private static int SPLASH_SCREEN_TIME = 10000; /*This is the Splash screen time which is 3 seconds*/
    static final String TAG="verification_activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_activity);
        e1 = findViewById(R.id.edittext1);
        e2 = findViewById(R.id.edittext2);
        e3 = findViewById(R.id.edittext3);
        e4 = findViewById(R.id.edittext4);
       // e5 = findViewById(R.id.edittext5);
        e5=findViewById(R.id.edittext5);
        e6 = findViewById(R.id.edittext6);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(verification_activity.this, HomeScreen.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN_TIME);*/
        e1.addTextChangedListener(new OTPTextWatcher(e1));
        e2.addTextChangedListener(new OTPTextWatcher(e2));
        e3.addTextChangedListener(new OTPTextWatcher(e3));
        e4.addTextChangedListener(new OTPTextWatcher(e4));
        e5.addTextChangedListener(new OTPTextWatcher(e5));
        e6.addTextChangedListener(new OTPTextWatcher(e6));
    }


    //-------------------------OTP WATCHER CLASS--------------------------------------//
    public class OTPTextWatcher implements TextWatcher {
        private View view;

        public OTPTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String otp = s.toString();
            switch (view.getId()) {
                case R.id.edittext1:
                    if (otp.length() == 1) {
                        e2.requestFocus();
                    } else if (otp.length() == 0)
                        e1.requestFocus();
                    break;
                case R.id.edittext2:
                    if (otp.length() == 1) {
                        e3.requestFocus();
                    } else if (otp.length() == 0)
                        e1.requestFocus();
                    break;
                case R.id.edittext3:
                    if (otp.length() == 1) {
                        e4.requestFocus();
                    } else if (otp.length() == 0)
                        e2.requestFocus();
                    break;
                case R.id.edittext4:
                    if (otp.length() == 1) {
                        Log.e(TAG, "afterTextChanged: 1");
                        //Intent intent = new Intent(verification_activity.this, HomeScreen.class);
                        //startActivity(intent);
                        //finish();
                        e5.requestFocus();
                    } else if (otp.length() == 0)
                        e3.requestFocus();
                    break;
                case R.id.edittext5:
                    if (otp.length() == 1) {
                        Log.e(TAG, "afterTextChanged: 3");
                        e6.requestFocus();
                    }
                    break;
                case R.id.edittext6:
                    if (otp.length() == 1) {
                        String tempotp = "1111";
                        Log.e(TAG, "afterTextChanged: "+ e3 );
                        Intent intent = new Intent(OtpVerification.this, HomeScreen.class);
                        startActivity(intent);
                        finish();
                    } else if (otp.length() == 0){
                        Log.e(TAG, "afterTextChanged:jdhjs " );
                        e3.requestFocus();
                    }
                    break;
                default:
                    Intent intent = new Intent(OtpVerification.this, HomeScreen.class);
                    startActivity(intent);
                    finish();
            }

        }
    }
    //-------------------------OTP watcher class ends here-------------------------------------------------------//
}
