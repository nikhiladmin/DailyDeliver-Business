package com.daytoday.business.dailydelivery.MainHomeScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        customerlist = findViewById(R.id.customer_list);
        customerlist.setHasFixedSize(true);
        customerlist.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setTitle("My Customers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        CustomerViewModel viewModel = new CustomerViewModel();
        viewModel.getCustomers().observe(this, new Observer<List<Customers>>() {
            @Override
            public void onChanged(List<Customers> customers) {
                CustomerAdapter adapter = new CustomerAdapter(CustomerActivity.this,customers);
                customerlist.setAdapter(adapter);
                Log.i("msg", String.valueOf(customers.size()));
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
