package com.daytoday.business.dailydelivery.MainHomeScreen.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.daytoday.business.dailydelivery.R;

public class EmpInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_info);
        getSupportActionBar().setTitle("Aditya Mishra");
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
