package com.daytoday.business.dailydelivery.MainHomeScreen.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daytoday.business.dailydelivery.LoginActivity.LoginPage;
import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Customers;
import com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel.CustomerViewModel;
import com.daytoday.business.dailydelivery.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CustomerActivity extends AppCompatActivity {
    RecyclerView customerlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        String bussID  = getIntent().getStringExtra("buisness-Id");
        String bussName = getIntent().getStringExtra("buisness-Name");

        customerlist = findViewById(R.id.customer_list);
        DividerItemDecoration itemDecor = new DividerItemDecoration(CustomerActivity.this, DividerItemDecoration.VERTICAL);
        customerlist.addItemDecoration(itemDecor);
        customerlist.setHasFixedSize(true);
        customerlist.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setTitle("My Customers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        CustomerViewModel viewModel = new CustomerViewModel(bussID);
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
