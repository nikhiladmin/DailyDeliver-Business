package com.daytoday.business.dailydelivery.MainHomeScreen.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.daytoday.business.dailydelivery.LargeImageView;
import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Bussiness;
import com.daytoday.business.dailydelivery.MainHomeScreen.UI.BusinessDetailActivity;
import com.daytoday.business.dailydelivery.R;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class SingleBusinessDetail extends AppCompatActivity {
    MaterialTextView earning,pending,cancelled,price,customers,payment,name,deliveryFrom,deliveryMode;
    Bussiness bussiness;
    ImageView bussImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_business_detail);
        name=findViewById(R.id.buss_detail_name);
        earning=findViewById(R.id.buss_detail_earning);
//        pending=findViewById(R.id.buss_detail_pending);
//        cancelled=findViewById(R.id.buss_detail_cancelled);
        price=findViewById(R.id.buss_detail_price);
        customers=findViewById(R.id.buss_detail_customer);
        deliveryFrom = findViewById(R.id.delivery_from);
        payment=findViewById(R.id.payment_mode);
        bussImg = findViewById(R.id.single_buss_img);
        deliveryMode = findViewById(R.id.delivery_mode);
        bussiness = (Bussiness) getIntent().getSerializableExtra("buisness-object");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(bussiness.getName().substring(0,1).toUpperCase()+bussiness.getName().substring(1));
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
      earning.setText("" +bussiness.getTotEarn());
        price.setText("" + bussiness.getPrice());
        customers.setText("" + bussiness.getNoOfCust());
        deliveryFrom.setText(bussiness.getAddress());
        deliveryMode.setText( bussiness.getdOrM().equals("M")? "Montly":"Daily");
        payment.setText(bussiness.getPayment().equals("Both") ? "Online and Cash on Delivery" :bussiness.getPayment());
//        pending.setText("" + bussiness.getTotPen());
//        cancelled.setText("" + bussiness.getTotCan());
        name.setText(bussiness.getName().substring(0,1).toUpperCase()+bussiness.getName().substring(1));
        if (bussiness.getImgurl()!=null) {
            Picasso.get()
                    .load(bussiness.getImgurl())
                    .resize(5000, 5000)
                    .centerCrop()
                    .into(bussImg);
            bussImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new LargeImageView(getApplicationContext(),v,bussiness.getImgurl(),null);
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Intent intent = new Intent(getBaseContext(), BusinessDetailActivity.class);
                intent.putExtra("buisness-object", bussiness);
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}