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

import android.view.View;
import android.widget.ProgressBar;

import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Bussiness;
import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Customers;
import com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel.CustomerViewModel;
import com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel.CustomerViewModelFactory;
import com.daytoday.business.dailydelivery.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;


public class CustomerActivity extends AppCompatActivity {
    RecyclerView customerlist;
    CustomerViewModel viewModel;
    public static final String BUSINESS_OBJECT = "business-object";
    Bussiness currentBusiness;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        currentBusiness = getIntent().getParcelableExtra(BUSINESS_OBJECT);

        String bussID  = currentBusiness.getBussid();
        String bussName = currentBusiness.getName();

        customerlist = findViewById(R.id.customer_list);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        customerlist.setHasFixedSize(true);
        customerlist.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setTitle("My Customers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        viewModel = new ViewModelProvider(this,new CustomerViewModelFactory(bussID)).get(CustomerViewModel.class);
        viewModel.getCustomers().observe(this, new Observer<List<Customers>>() {
            @Override
            public void onChanged(List<Customers> customers) {
                CustomerAdapter adapter = new CustomerAdapter(CustomerActivity.this, customers, currentBusiness );
                customerlist.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }
        });



    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
