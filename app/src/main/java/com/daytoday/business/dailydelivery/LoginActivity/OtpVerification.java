package com.daytoday.business.dailydelivery.LoginActivity;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.CountDownTimer;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.daytoday.business.dailydelivery.MainHomeScreen.View.HomeScreen;
import com.daytoday.business.dailydelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class OtpVerification extends AppCompatActivity {
    private PinView pinView;
    static final String TAG="verification_activity";
    private String phone;
    private FirebaseAuth mAuth;
    private TextView textTimer;
    private int time=60;  //Time OUT resend OTP
    private String code;
    private String name;
    private String verification;
    private Button resend;

    private int SPLASH_SCREEN_TIME = 10000; /*This is the Splash screen time which is 3 seconds*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_activity);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        pinView=findViewById(R.id.firstPinView);
        textTimer = findViewById(R.id.timer);
        resend=findViewById(R.id.resend);
        phone=getIntent().getStringExtra("phoneNo");
        name=getIntent().getStringExtra("Name");
        resend.setEnabled(false);
        phoneAuthProvider();

        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                code=s.toString();
                if((code!=null) && (code.length()==6)){
                    verifyCode(code);
                }
                Log.i(TAG, "beforeTextChanged: "+s);
            }});

        resend.setOnClickListener(v -> {
            time=60;
            Snackbar.make(v,"We Just Send You OTP again .Please Try Again !",Snackbar.LENGTH_LONG).show();
            phoneAuthProvider();
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //   Sign in success, update com.daytoday.business.dailydelivery.MainHomeScreen.UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            if (user != null)
                            {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User profile updated.");
                                                    createUserProfile(name);
                                                    SendUserHomePage();
                                                }
                                            }
                                        });
                            }
                        } else {
                            // Sign in failed, display a message and update the com.daytoday.business.dailydelivery.MainHomeScreen.UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void createUserProfile(String name) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        HashMap<String,String> data = new HashMap<>();
        data.put("Name",name);
        data.put("PhoneNo",currentUser.getPhoneNumber());
        data.put("Address","RB II 671 / D A Road");
        firestore.collection("Buss_User_Info").document(currentUser.getUid()).set(data);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update com.daytoday.business.dailydelivery.MainHomeScreen.UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            SendUserHomePage();
        }
    }

    private void verifyCode(String code){
        PhoneAuthCredential credential =PhoneAuthProvider.getCredential(verification,code);
        signInWithPhoneAuthCredential(credential);
    }

    private void phoneAuthProvider(){
        Log.i(TAG, "phoneAuthProvider: ");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                OtpVerification.this,               // Activity (for callback binding)
                mCallbacks);

        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                textTimer.setText("0:"+checkDigit(time));
                time--;
            }
            public void onFinish() {
                textTimer.setText("Change Mobile Number");
                resend.setEnabled(true);
            }
        }.start();

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            Log.i(TAG, "onVerificationCompleted: ");
            code=credential.getSmsCode();
            if(code!=null&&code.length()==6){
                pinView.setText(code);
            }
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {

            Log.w("onVerifivcation", "onVerificationFailed", e);
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG);
                Log.i(TAG, "onVerificationFailed: "+e);
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG);
                Log.i(TAG, "onVerificationFailed: "+e);
            }
        }
        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            verification=verificationId;
        }
    };

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public void SendUserHomePage(){
        Intent loginIntent=new Intent(OtpVerification.this, HomeScreen.class);
        loginIntent.putExtra("Name",name);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
        finish();
    }

}