package com.daytoday.business.dailydelivery.MainHomeScreen.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Customers;
import com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel.CustomerViewModel;
import com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel.CustomerViewModelFactory;
import com.daytoday.business.dailydelivery.R;

import java.util.List;

public class CustomerActivity extends AppCompatActivity {
    RecyclerView customerlist;
    CustomerViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        String bussID  = getIntent().getStringExtra("buisness-Id");
        String bussName = getIntent().getStringExtra("buisness-Name");
        customerlist = findViewById(R.id.customer_list);
        customerlist.setHasFixedSize(true);
        customerlist.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setTitle("My Customers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        viewModel = new ViewModelProvider(this,new CustomerViewModelFactory(bussID)).get(CustomerViewModel.class);
        viewModel.getCustomers().observe(this, new Observer<List<Customers>>() {
            @Override
            public void onChanged(List<Customers> customers) {
                CustomerAdapter adapter = new CustomerAdapter(CustomerActivity.this, customers, bussName, bussID );
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
