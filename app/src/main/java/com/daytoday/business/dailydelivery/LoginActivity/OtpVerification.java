package com.daytoday.business.dailydelivery.LoginActivity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.daytoday.business.dailydelivery.AdditionalInfo;
import com.daytoday.business.dailydelivery.MainHomeScreen.Model.AuthUser;
import com.daytoday.business.dailydelivery.MainHomeScreen.View.HomeScreen;
import com.daytoday.business.dailydelivery.Network.ApiInterface;
import com.daytoday.business.dailydelivery.Network.Client;
import com.daytoday.business.dailydelivery.Network.Response.AuthUserResponse;
import com.daytoday.business.dailydelivery.Network.Response.YesNoResponse;
import com.daytoday.business.dailydelivery.R;
import com.daytoday.business.dailydelivery.Utilities.SaveOfflineManager;
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

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerification extends AppCompatActivity {
    static final String TAG = "verification_activity";
    private PinView pinView;
    private String phone;
    private FirebaseAuth mAuth;
    private TextView textTimer;
    private ApiInterface apiInterface;
    private int time = 60;  //Time OUT resend OTP
    private String code;
    private String verification;
    private Button resend;
    private boolean isPhoneAuth;
    private String user_address;


    private final int SPLASH_SCREEN_TIME = 10000; /*This is the Splash screen time which is 3 seconds*/
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {

            Log.i(TAG, "onVerificationCompleted: " + credential.getSmsCode());

            code = credential.getSmsCode();
            if (code != null && code.length() == 6) {
                pinView.setText(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Log.w("onVerifivcation", "onVerificationFailed", e);
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG);
                Log.i(TAG, "onVerificationFailed: " + e);
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG);
                Log.i(TAG, "onVerificationFailed: " + e);
            }
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            Log.i(TAG, "onCodeSent: " + verificationId);
            verification = verificationId;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_activity);

        phone = getIntent().getStringExtra("phoneNo");
        isPhoneAuth = getIntent().getBooleanExtra("isPhoneAuth", false);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();
        apiInterface = Client.getClient().create(ApiInterface.class);
        mAuth = FirebaseAuth.getInstance();
        pinView = findViewById(R.id.firstPinView);
        textTimer = findViewById(R.id.timer);
        resend = findViewById(R.id.resend);

        resend.setEnabled(false);
        apiInterface = Client.getClient().create(ApiInterface.class);

        //Authentication With phoneNumber--------------
        Log.i(TAG, "onCreate: " + isPhoneAuth);
        phoneAuthProvider();
        if (!isPhoneAuth) {
            user_address = getIntent().getStringExtra("address");
//            sendVerificationCode(mAuth.getCurrentUser().getUid(),phone);
        }
        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                code = s.toString();
                if ((code != null) && (code.length() == 6)) {
                        firebaseVerifyCode(code);
                }
            }
        });

        resend.setOnClickListener(v -> {
            time = 60;
            Snackbar.make(v, "We Just Send You OTP again .Please Try Again !", Snackbar.LENGTH_LONG).show();
            phoneAuthProvider();
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            if (task.getResult().getAdditionalUserInfo().isNewUser() == true || task.getResult().getUser().getDisplayName() == null) {

                                Intent additionalInfoIntent = new Intent(OtpVerification.this, AdditionalInfo.class);
                                startActivity(additionalInfoIntent);
                            } else {
                                FirebaseUser user = task.getResult().getUser();
                                if (user != null) {
                                    loginUser(user.getUid());

                                }
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

    public void saveOffline(FirebaseUser currentUser, String name, String adress) {
        SaveOfflineManager.setUserName(this, name);
        SaveOfflineManager.setUserId(this, currentUser.getUid());
        SaveOfflineManager.setUserAdress(this, adress);
        SaveOfflineManager.setUserPhoneNumber(this, currentUser.getPhoneNumber());
    }

    private void firebaseVerifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification, code);
        if(isPhoneAuth){
            signInWithPhoneAuthCredential(credential);
        }else{
            FirebaseUser firebaseUser= mAuth.getCurrentUser();
            if(firebaseUser!=null){
                updatePhoneNumber(firebaseUser,credential,user_address);
            }
        }

    }

    private void phoneAuthProvider() {
        Log.i(TAG, "phoneAuthProvider: " + phone);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                OtpVerification.this,               // Activity (for callback binding)
                mCallbacks);

        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                textTimer.setText("0:" + checkDigit(time));
                time--;
            }

            public void onFinish() {
                textTimer.setText("Change Mobile Number");
                resend.setEnabled(true);
            }
        }.start();

    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public void SendUserHomePage() {
        Intent loginIntent = new Intent(OtpVerification.this, HomeScreen.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(loginIntent);

    }

    private void loginUser(String uid) {
        Call<AuthUserResponse> loginUserDataCalling = apiInterface.loginUser(uid);
        loginUserDataCalling.enqueue(new Callback<AuthUserResponse>() {
            @Override
            public void onResponse(Call<AuthUserResponse> call, Response<AuthUserResponse> response) {

                AuthUser authUser = response.body().getUsers().get(0);
                Log.i("LOGIN", "onResponse: "+authUser.getName());
                saveOffline(mAuth.getCurrentUser(), authUser.getName(), authUser.getAddress());
                SendUserHomePage();
            }

            @Override
            public void onFailure(Call<AuthUserResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Somting went wrong", Toast.LENGTH_LONG);
            }
        });
    }



    private void updatePhoneNumber(FirebaseUser user,PhoneAuthCredential credential,String user_address){
        if(user!=null){
            user.updatePhoneNumber(credential)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {

                           createUserProfile(user.getDisplayName(),user,user_address);

                        } else {

                        }
                    });

        }
    }

    public void createUserProfile(String name, FirebaseUser user, String address) {
        FirebaseUser currentUser = user;
        Log.d("message", "createUserProfile: " + currentUser.getUid() + name + currentUser.getPhoneNumber() + address);
        Call<YesNoResponse> createusercall = apiInterface.addBussUserDetails(currentUser.getUid(), name, currentUser.getPhoneNumber(), address);

        createusercall.enqueue(new Callback<YesNoResponse>() {
            @Override
            public void onResponse(Call<YesNoResponse> call, Response<YesNoResponse> response) {
                Log.i("message", "Response Successful " + response.body().getMessage());
                saveOffline(currentUser, name, address);
                SendUserHomePage();
            }

            @Override
            public void onFailure(Call<YesNoResponse> call, Throwable t) {
                Log.e("MESSAGE_API", "onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Couldn't Login. Please Try Again", Toast.LENGTH_SHORT).show();
                //TODO logout user from here
            }
        });
    }

}
