package com.daytoday.business.dailydelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.daytoday.business.dailydelivery.LoginActivity.LoginPage;
import com.daytoday.business.dailydelivery.LoginActivity.OtpVerification;
import com.daytoday.business.dailydelivery.MainHomeScreen.View.HomeScreen;
import com.daytoday.business.dailydelivery.Network.ApiInterface;
import com.daytoday.business.dailydelivery.Network.Client;
import com.daytoday.business.dailydelivery.Network.Response.AuthUserCheckResponse;
import com.daytoday.business.dailydelivery.Utilities.SaveOfflineManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailSignup extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button  signupBtn;
    private CheckBox privacyPolicyCheck;
    private Button privacyPolicyBtn;
    private Button signinNow;
    private AlertDialog alertDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_signup);

        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        emailEditText = findViewById(R.id.signup_email);
        passwordEditText = findViewById(R.id.signup_password);
        confirmPasswordEditText = findViewById(R.id.signup_confirm_password);
        signupBtn = findViewById(R.id.signup_btn);
        privacyPolicyCheck = findViewById(R.id.privacy_policy_checkBox);
        privacyPolicyBtn = findViewById(R.id.privacy_policy);
        signinNow = findViewById(R.id.signin_now);
        mAuth = FirebaseAuth.getInstance();

        signinNow.setOnClickListener(view -> {
            Intent signin = new Intent(EmailSignup.this, LoginPage.class);
            startActivity(signin);
        });

        signupBtn.setOnClickListener(view -> {

            if(isEmailValid(emailEditText.getText().toString())){
                isUserExist(emailEditText.getText().toString());
            }

        });
    }

    private boolean isEmailValid(String e) {
        if(e==null||e.length()==0){
            emailEditText.setError("Please enter email");
            return false;
        }else {
            return true;
        }
    }

    private void signupWithEmail(String email,String password,String confirmPassword){

        if(passwordMatch(password, confirmPassword)){
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isComplete()&&task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();

                    }else{
                        Toast.makeText(EmailSignup.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
    private  boolean passwordMatch(String password,String confirmPassword){
        return password.equals(confirmPassword);
    }

    public void SendUserHomePage() {
        Intent loginIntent = new Intent(EmailSignup.this, HomeScreen.class);
        loginIntent.putExtra("isPhoneAuth",false);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
        finish();
    }

    public void saveOffline(FirebaseUser currentUser, String name, String adress) {
        SaveOfflineManager.setUserName(this, name);
        SaveOfflineManager.setUserId(this, currentUser.getUid());
        SaveOfflineManager.setUserAddress(this, adress);
        SaveOfflineManager.setUserPhoneNumber(this, currentUser.getPhoneNumber());
    }

    private void  isUserExist(String email){
        Call<AuthUserCheckResponse> authUserCheckResponseCall = Client.getClient().create(ApiInterface.class).isRegisteredUser(email);
            authUserCheckResponseCall.enqueue(new Callback<AuthUserCheckResponse>() {
                @Override
                public void onResponse(Call<AuthUserCheckResponse> call, Response<AuthUserCheckResponse> response) {
                    if(response.body().getError()){
                        String pass = passwordEditText.getText().toString();
                        String em = emailEditText.getText().toString();
                        String con_pass = confirmPasswordEditText.getText().toString();
                        if(passwordMatch(pass,con_pass)){
                            Intent signup = new Intent(EmailSignup.this, OtpVerification.class);
                            signup.putExtra("email",em);
                            signup.putExtra("password",pass);
                            signup.putExtra("isPhoneAuth",false);
                            startActivity(signup);
                        }else{
                            confirmPasswordEditText.setError("Password not match");
                        }
                    }else{
                        if(response.body().getProvider().equals("1")){
                            emailEditText.setError("User already exist Please! login");
                        }else if(response.body().getProvider().equals("2")){
                            emailEditText.setError("User already exist With google Sign-in");
                        }else{
                            emailEditText.setError("User already exist Please! login");
                        }
                    }
                }

                @Override
                public void onFailure(Call<AuthUserCheckResponse> call, Throwable t) {

                }
            });
    }

    void DialogBoxShow(View v){
        final AlertDialog.Builder builder = new AlertDialog.Builder(EmailSignup.this,R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_dialog, viewGroup, false);
        builder.setView(dialogView);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.show();
    }
}