package com.daytoday.business.dailydelivery.MainHomeScreen.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.daytoday.business.dailydelivery.R;
import com.daytoday.business.dailydelivery.Utilities.SaveOfflineManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QrCodeActivity extends AppCompatActivity {
    ImageView barCode;
    TextView personName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        getSupportActionBar().setTitle("Qr code");
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
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            barCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
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
}