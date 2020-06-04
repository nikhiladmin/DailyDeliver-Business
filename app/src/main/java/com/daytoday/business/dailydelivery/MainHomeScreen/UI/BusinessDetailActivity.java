package com.daytoday.business.dailydelivery.MainHomeScreen.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Bussiness;
import com.daytoday.business.dailydelivery.R;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class BusinessDetailActivity extends AppCompatActivity {
    TextView buisness_name,MOrD,PayMode,Price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buisness_detail);
        Bussiness bussiness = getIntent().getParcelableExtra("buisness-object");
        getSupportActionBar().setTitle("My Business");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        buisness_name = findViewById(R.id.BusinessName);
        MOrD = findViewById(R.id.BusinessMorD);
        PayMode = findViewById(R.id.BusinessPayMode);
        Price = findViewById(R.id.BusinessPrice);
        buisness_name.setText(bussiness.getProductName());
        Price.setText(bussiness.getPrice());
        MOrD.setText(bussiness.getTarrif());
        PayMode.setText(bussiness.getPay_mode());


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.edit:
                Toast.makeText(this, "Edit The Business", Toast.LENGTH_SHORT).show();
                /* Do the things Here for Edit the buisness */
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
