package com.daytoday.business.dailydelivery.MainHomeScreen.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Bussiness;
import com.daytoday.business.dailydelivery.MainHomeScreen.UI.BusinessDetailActivity;
import com.daytoday.business.dailydelivery.R;
import com.google.android.material.textview.MaterialTextView;

public class SingleBusinessDetail extends AppCompatActivity {
    MaterialTextView earning,pending,cancelled,price,customers,payment,name;
    Bussiness bussiness;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_business_detail);
        name=findViewById(R.id.buss_detail_name);
        earning=findViewById(R.id.buss_detail_earning);
        pending=findViewById(R.id.buss_detail_pending);
        cancelled=findViewById(R.id.buss_detail_cancelled);
        price=findViewById(R.id.buss_detail_price);
        customers=findViewById(R.id.buss_detail_customer);
        payment=findViewById(R.id.buss_detail_payment);
        bussiness = getIntent().getParcelableExtra("buisness-object");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(bussiness.getProductName()+" - Details");
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        earning.setText(bussiness.getEarning());
        price.setText(bussiness.getPrice());
        customers.setText(bussiness.getCust_cout());
        payment.setText(bussiness.getTarrif());
        pending.setText(bussiness.getPending());
        cancelled.setText(bussiness.getCancelled());
        name.setText(bussiness.getProductName());
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