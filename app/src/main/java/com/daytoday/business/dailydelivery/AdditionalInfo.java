package com.daytoday.business.dailydelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.daytoday.business.dailydelivery.LoginActivity.OtpVerification;
import com.daytoday.business.dailydelivery.MainHomeScreen.View.HomeScreen;
import com.daytoday.business.dailydelivery.Network.ApiInterface;
import com.daytoday.business.dailydelivery.Network.Client;
import com.daytoday.business.dailydelivery.Network.Response.GeocodingResponse;
import com.daytoday.business.dailydelivery.Network.Response.YesNoResponse;
import com.daytoday.business.dailydelivery.Utilities.SaveOfflineManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdditionalInfo extends AppCompatActivity {

    String ft;
    String lt;
    String userAddress;
    private TextInputEditText firstName;
    private TextInputEditText lastName;
    private TextInputEditText address;
    private Button goHome;
    private ImageButton getLocation;
    private FirebaseAuth mAuth;
    private ApiInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_info);

        //Action bar hide and color change ------------------------------------------
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();

        //Initialize views and Firebase-------------------------
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        address = findViewById(R.id.user_address);
        goHome = findViewById(R.id.go_home);
        getLocation = findViewById(R.id.getLocation);
        mAuth = FirebaseAuth.getInstance();
        apiInterface = Client.getClient().create(ApiInterface.class);
        goHome.setOnClickListener(view -> {
            ft = firstName.getText().toString().trim();
            lt = lastName.getText().toString().trim();
            String userAddress = address.getText().toString().trim();
            if (ft.isEmpty() || lt.isEmpty() || userAddress.isEmpty()) {
                firstName.setError("Please enter first name");
                lastName.setError("Please enter last name");
                address.setError("please enter your address");

            } else if (!(ft.matches("^[A-Za-z]+$") && lt.matches("^[a-zA-Z]+$"))) {
                firstName.setError("Invalid first name ");
                lastName.setError("Invalid last name");

            } else {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(ft + " " + lt)
                            .setPhotoUri(null)
                            .build();
                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("ADDITIONAL_INFO", "User profile updated.");

                                        createUserProfile(ft + " " + lt, user, userAddress);

                                    }
                                }
                            });
                }
            }
        });

        getLocation.setOnClickListener(view -> {
            getLocationPermission();
        });
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

    public void saveOffline(FirebaseUser currentUser, String name, String adress) {
        SaveOfflineManager.setUserName(this, name);
        SaveOfflineManager.setUserId(this, currentUser.getUid());
        SaveOfflineManager.setUserAdress(this, adress);
        SaveOfflineManager.setUserPhoneNumber(this, currentUser.getPhoneNumber());
    }

    public void SendUserHomePage() {
        Intent loginIntent = new Intent(AdditionalInfo.this, HomeScreen.class);
        loginIntent.putExtra("Name", ft + " " + lt);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
        finish();
    }

    private void getLocationPermission() {

        Dexter
                .withContext(getApplicationContext())
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            getCurrentLocation();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(getApplicationContext(), "Please allow external storage access to save QR code", Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


    private void getCurrentLocation() {
        Log.i("LOCATION_TRACK","GETL");
        LocationManager locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        boolean isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.i("LOCATION_TRACK",isGPSEnable+" "+isNetworkEnable);
        if (isGPSEnable) {
            Log.i("LOCATION_TRACK", "getCurrentLocation: "+(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.i("LOCATION_TRACK", "getLocation: " + location.getLatitude() + " " + location.getLongitude());
                getAddress(location.getLatitude(),location.getLongitude());
                return;
            } else {
                    Toast.makeText(getApplicationContext(),"Please allow permission to find your current location",Toast.LENGTH_LONG);
            }
        }

    }

    private  void getAddress(double lat ,double lon){
        ApiInterface  geocodingApiInterface = Client.getGeocodingClient().create(ApiInterface.class);
        Call<GeocodingResponse> geocodingResponseCall = geocodingApiInterface.getReverseGeocoding(lat,lon,18,0,"jsonv2");

        geocodingResponseCall.enqueue(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                Log.i("LOCATION_TACK", "onResponse: "+response.body().getDisplayName());
                if(response.body().getError()!=null&&response.body().getError() == true){
                    Toast.makeText(getApplicationContext(),"Location not fonund",Toast.LENGTH_LONG);
                }else{
                    address.setText(response.body().getDisplayName());
                }

            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG);
            }
        });
    }
}