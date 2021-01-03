package com.daytoday.business.dailydelivery.LoginActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.daytoday.business.dailydelivery.Network.ApiInterface;
import com.daytoday.business.dailydelivery.Network.Client;
import com.daytoday.business.dailydelivery.Network.Response.GeocodingResponse;
import com.daytoday.business.dailydelivery.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;
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


public class PhoneVerification extends AppCompatActivity {

    CountryCodePicker ccp;
    Button send_otp;

    private TextInputEditText phoneNo;
    private TextView displayName;
    private TextInputEditText address;
    private TextInputLayout addresslayout;
    private boolean isPhoneAuth;
    private TextView phone_auth_title;
   private ImageButton gps_address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        isPhoneAuth = getIntent().getBooleanExtra("isPhoneAuth", false);
        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().hide();
        addresslayout = findViewById(R.id.editText_addresslayout);
        ccp = findViewById(R.id.ccp);
        phone_auth_title = findViewById(R.id.phone_verify_title);
        send_otp = findViewById(R.id.send_otp);
        phoneNo = findViewById(R.id.editText_carrierNumber);
        displayName = findViewById(R.id.display_name);
        address = findViewById(R.id.address_text);
      gps_address = findViewById(R.id.gps_address);
        if (isPhoneAuth == false) {
            phone_auth_title.setText("Verify Phone Number");
            phone_auth_title.setTextSize(35);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && user.getDisplayName() != null) {
                displayName.setText(user.getDisplayName());
                displayName.setVisibility(View.VISIBLE);
            }
            gps_address.setVisibility(View.VISIBLE);
            addresslayout.setVisibility(View.VISIBLE);
        }

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String completePhoneNo = ccp.getFullNumberWithPlus() + phoneNo.getText().toString();
                Intent intent = new Intent(PhoneVerification.this, OtpVerification.class);
                intent.putExtra("isPhoneAuth", isPhoneAuth);
                intent.putExtra("phoneNo", completePhoneNo);
                if (isPhoneAuth==false)
                    intent.putExtra("address", address.getText().toString());

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();


                startActivity(intent);
            }
        });
            gps_address.setOnClickListener(view -> {
                getLocationPermission();
            });
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
        ApiInterface geocodingApiInterface = Client.getGeocodingClient().create(ApiInterface.class);
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
