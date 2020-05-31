package com.daytoday.business.dailydelivery.MainHomeScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.daytoday.business.dailydelivery.R;

import java.util.List;

public class CustomerActivity extends AppCompatActivity {
    RecyclerView customerlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        String bussID  = getIntent().getStringExtra("buisness-Id");
        String bussName = getIntent().getStringExtra("buisness-Name");
        Log.i("msg",bussID);
        customerlist = findViewById(R.id.customer_list);
        customerlist.setHasFixedSize(true);
        customerlist.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setTitle("My Customers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        CustomerViewModel viewModel = new CustomerViewModel(bussID);
        viewModel.getCustomers().observe(this, new Observer<List<Customers>>() {
            @Override
            public void onChanged(List<Customers> customers) {
                CustomerAdapter adapter = new CustomerAdapter(CustomerActivity.this,customers,bussName,bussID);
                customerlist.setAdapter(adapter);
                Log.i("msg","done");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
