package com.daytoday.business.dailydelivery.LoginActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.daytoday.business.dailydelivery.MainHomeScreen.HomeScreen;
import com.daytoday.business.dailydelivery.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

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
    private String photoUrl;
    private String email;
    private String name;

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


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        phoneLogin = findViewById(R.id.btn1);
        googleLogin = findViewById(R.id.btn2);
        facebookLogin = findViewById(R.id.btn3);

        //dialog Box
        loading = findViewById(R.id.loading);
        heading =findViewById(R.id.heading);
        subheading =findViewById(R.id.subHeading);


        //==========================Phone Login==================================================
        phoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this, PhoneVerification.class);
                startActivity(intent);
            }
        });
        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBoxShow(v);
            }
        });
        //==============================Facebook Login============================================
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
        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Intent loginIntent = new Intent(LoginPage.this, HomeScreen.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
                finish();
            }
        };

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
        Log.w("GoogleLogin", "signInWithCredential:failure");
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        Log.w("GoogleLogin", "signInWithCredential:failure");
            startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("GoogleLogin", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                                Intent loginIntent = new Intent(LoginPage.this, HomeScreen.class);
                                loginIntent.putExtra("name",account.getDisplayName());
                                loginIntent.putExtra("photoUrl",account.getPhotoUrl().toString());

                                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(loginIntent);
                                finish();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            googleLogin.setEnabled(true);
                            alertDialog.dismiss();
                            loading.setVisibility(View.INVISIBLE);
                            Log.w("GoogleLogin", "signInWithCredential:failure", task.getException());
                            //Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });
    }

    //============================Facebook Login Method==============================

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("FacebookAccessToken", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("FacbookAuth", "signInWithCredential:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    name=user.getDisplayName();
                    photoUrl =user.getPhotoUrl().toString();
                    email =user.getEmail();
                    //String phone=user.getPhoneNumber();
                   // Log.i("dataUser", "onComplete: "+name+" "+photoUrl+" "+email+" "+phone);
                    Intent homeIntent = new Intent(LoginPage.this, HomeScreen.class);
                    homeIntent.putExtra("photoUrl",photoUrl);
                    startActivity(homeIntent);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("FacbookAuth", "signInWithCredential:failure", task.getException());
                    Toast.makeText(LoginPage.this, "User Already Exist with Google Account", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
        });

    }
    // -------------------------- Facebook Login Method------------------------------

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
                // Google Sign In failed, update UI appropriately
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
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent loginIntent = new Intent(LoginPage.this, HomeScreen.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            finish();
        }else{
            mAuth.addAuthStateListener(authStateListener);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}

