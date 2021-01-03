package com.daytoday.business.dailydelivery.LoginActivity;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.daytoday.business.dailydelivery.AdditionalInfo;
import com.daytoday.business.dailydelivery.MainHomeScreen.Model.AuthUser;
import com.daytoday.business.dailydelivery.MainHomeScreen.View.HomeScreen;
import com.daytoday.business.dailydelivery.Network.ApiInterface;
import com.daytoday.business.dailydelivery.Network.Client;
import com.daytoday.business.dailydelivery.Network.Response.AuthUserResponse;
import com.daytoday.business.dailydelivery.R;
import com.daytoday.business.dailydelivery.Utilities.SaveOfflineManager;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPage extends AppCompatActivity {
    private int RC_SIGN_IN = 1;
    private Button phoneLogin;
    private Button googleLogin;
    private FirebaseAuth mAuth;
    private AlertDialog alertDialog;
    //for facebook Login
    private LoginButton facebookLogin;
    private CallbackManager mCallbackManager;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private  Button fb;

    //for Google Login
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    private boolean isGoogleSignIn=false;

    //Dialog box
    private ProgressBar loading;
    private TextView heading;
    private TextView subheading;
    private Object FirebaseAuthUserCollisionException;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);


        mAuth = FirebaseAuth.getInstance();


        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        phoneLogin = findViewById(R.id.btn1);
        googleLogin = findViewById(R.id.btn2);
        facebookLogin = findViewById(R.id.btn3);
        fb = findViewById(R.id.fb);
        //dialog Box
        loading = findViewById(R.id.loading);
        heading =findViewById(R.id.heading);
        subheading =findViewById(R.id.subHeading);

        //==========================Phone Login==================================================
        phoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginPage.this, PhoneVerification.class);
                intent.putExtra("isPhoneAuth",true);
                startActivity(intent);
            }
        });
        //==============================Facebook Login============================================
        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBoxShow(v);
            }
        });

        facebookLogin.setReadPermissions("email","public_profile");
        mCallbackManager = CallbackManager.Factory.create();

        facebookLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {


            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("facebookAuth", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("facebookAuth", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("facebookAuth", "facebook:onCancel");
            }
        });


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                boolean isLoggedIn = currentAccessToken != null && !currentAccessToken.isExpired();
                if (isLoggedIn) {
                   // mAuth.signOut();
                }
            }
        };

        //============================Google Login=======================================

         //================= configuration =================
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogBoxShow(v);
            googleLogin.setEnabled(false);
            isGoogleSignIn=true;
            GoogleSignIn();
        }
    });

    }

    private void GoogleSignIn() {
        Log.w("GoogleLogin", "signInWithCredentialIntent");
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        Log.w("GoogleLogin", "signInWithCredential");
            startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null){
                                if(task.getResult().getAdditionalUserInfo().isNewUser()&&task.getResult().getUser().getPhoneNumber()==null){
                                    Intent loginIntent = new Intent(LoginPage.this, PhoneVerification.class);
                                    loginIntent.putExtra("isPhoneAuth",false);
                                    startActivity(loginIntent);
                                }else{
                                    loginUser(user.getUid());
                                }
                            }


                        } else {
                            googleLogin.setEnabled(true);
                            alertDialog.dismiss();
                            loading.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"Login failed Please try again",Toast.LENGTH_LONG);
                            Log.w("GoogleLogin", "signInWithCredential:failure", task.getException());

                        }
                        Log.i("GoogleLogin", "onComplete: "+task.getResult());
                    }


                });
    }

    //============================Facebook Login Method==============================
    public void onClick(View v) {
        if (v == fb) {
            facebookLogin.performClick();
        }
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("FacebookAccessToken", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user!=null){
                        if(task.getResult().getAdditionalUserInfo().isNewUser()&&task.getResult().getUser().getPhoneNumber()==null){
                            Intent loginIntent = new Intent(LoginPage.this, PhoneVerification.class);
                            loginIntent.putExtra("isPhoneAuth",false);
                            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(loginIntent);
                            finish();
                        }else{
                            loginUser(user.getUid());
                        }
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("FacbookAuth", "signInWithCredential:failure", task.getException());
                    LoginManager.getInstance().logOut();
                    Toast.makeText(LoginPage.this, "User Already Exist with Google Account", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
        });

    }


//=====================show Custom Dialog Box================================

    void DialogBoxShow(View v){
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this,R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_dialog, viewGroup, false);
        builder.setView(dialogView);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==RC_SIGN_IN && isGoogleSignIn){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("GoogleLogin", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update com.daytoday.business.dailydelivery.MainHomeScreen.UI appropriately
                googleLogin.setEnabled(true);
                loading.setVisibility(View.INVISIBLE);
                Log.w("GoogleLogin", "Google sign in failed", e);
                // ...
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update com.daytoday.business.dailydelivery.MainHomeScreen.UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            if(currentUser.getPhoneNumber()!=null&&currentUser.getDisplayName()==null&&currentUser.isEmailVerified()==false) {
                Log.v("AUTHEN", "walkT: to =login to addi");
                Intent loginIntent = new Intent(LoginPage.this, AdditionalInfo.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
                finish();
            }else if(currentUser.getPhoneNumber()==null&&currentUser.isEmailVerified()==true&&currentUser.getDisplayName()!=null){
                Intent loginIntent = new Intent(LoginPage.this, PhoneVerification.class);
                loginIntent.putExtra("isPhoneAuth",false);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
                finish();
            }
            else{
                Log.v("AUTHEN", "walkT: to =login to Home");
                Intent loginIntent = new Intent(LoginPage.this, HomeScreen.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
                finish();
            }
        }else{
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    private void loginUser(String uid) {
        ApiInterface apiInterface= Client.getClient().create(ApiInterface.class);

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

    public void SendUserHomePage() {
        Intent loginIntent = new Intent(LoginPage.this, HomeScreen.class);
        loginIntent.putExtra("isPhoneAuth",false);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
        finish();
    }

    public void saveOffline(FirebaseUser currentUser, String name, String adress) {
        SaveOfflineManager.setUserName(this, name);
        SaveOfflineManager.setUserId(this, currentUser.getUid());
        SaveOfflineManager.setUserAdress(this, adress);
        SaveOfflineManager.setUserPhoneNumber(this, currentUser.getPhoneNumber());
    }
}

