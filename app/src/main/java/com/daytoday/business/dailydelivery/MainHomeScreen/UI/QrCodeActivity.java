package com.daytoday.business.dailydelivery.MainHomeScreen.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daytoday.business.dailydelivery.R;
import com.daytoday.business.dailydelivery.Utilities.SaveOfflineManager;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class QrCodeActivity extends AppCompatActivity {
    ImageView barCode;
    TextView personName;

    Button shareQr;
    Button downloadQr;
    Bitmap QrBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        shareQr = findViewById(R.id.share_qr);
        downloadQr = findViewById(R.id.download_qr);
        getSupportActionBar().setTitle("My QR code");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        barCode = findViewById(R.id.bar_code);
        personName = findViewById(R.id.person_name);
        personName.setText(SaveOfflineManager.getUserName(this));
        String text = SaveOfflineManager.getUserId(this);
        String encryptedText = encrypt(text);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,400,400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();


            QrBitmap = barcodeEncoder.createBitmap(bitMatrix);

            barCode.setImageBitmap(QrBitmap);


        } catch (WriterException e) {
            e.printStackTrace();
        }
        pemission();
        shareQr.setOnClickListener(view -> {

            shareQrCode(QrBitmap);
        });

        downloadQr.setOnClickListener(view -> {
            Dexter.withContext(this)
                    .withPermissions(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {

                            if (report.areAllPermissionsGranted()) {

                                String filename = "dailyDeliveryuser_"+SaveOfflineManager.getUserName(getApplicationContext());
                                MediaStore.Images.Media.insertImage(getContentResolver(), QrBitmap, filename , "Qr code of "+filename);
                                Snackbar.make(view,"QR code save in your gallery", Snackbar.LENGTH_LONG).show();

                            }


                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // show alert dialog navigating to Settings

                                Toast.makeText(getApplicationContext(),"Please allow external storage access to save QR code", Toast.LENGTH_LONG).show();

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


        });
    }

    private String encrypt(String text) {
        //TODO Encryption Code Here
        return text;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {


        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void shareQrCode(Bitmap qrCode){
        Uri imageuri = getImageUri(this,qrCode);
        Log.i("IMAGE_URI",imageuri.toString());
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Welcome to DailyDelivery\n\n"+SaveOfflineManager.getUserName(this)+" want to connect with you .Scan and connect with QR code and use our daily product.\nDownload DailyDelivery app now googleplaylink.com");
        intent.putExtra(Intent.EXTRA_STREAM, imageuri);
        intent.setType("image/jpeg");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Log.i("IMAGE_URI", "shareQrCode: "+ex.getMessage());
            Toast.makeText(this,"Application not installed",Toast.LENGTH_LONG).show();
        }
    }



    private int getNavigationBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }
    private void setDynamicSize(){
        CardView qrCard = findViewById(R.id.qr_displaycard);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels + getNavigationBarHeight();

        int width = displayMetrics.widthPixels + getNavigationBarHeight();

        qrCard.setMinimumWidth((int)(width*0.9));
        qrCard.setMinimumHeight((int)(height*0.7));
        barCode.setMaxHeight((int)(height*0.5));
        barCode.setMaxWidth(((int)(width*0.5)));
    }
    private  void pemission(){
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {


                        }


                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                            Toast.makeText(getApplicationContext(),"Please allow external storage access to save QR code", Toast.LENGTH_LONG).show();

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

}