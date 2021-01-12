package com.daytoday.business.dailydelivery.MainHomeScreen.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.ProgressBar;

import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Dates;
import com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel.DatesViewModel;
import com.daytoday.business.dailydelivery.Network.Response.Transaction;
import com.daytoday.business.dailydelivery.R;
import com.daytoday.business.dailydelivery.Utilities.AppUtils;
import com.daytoday.business.dailydelivery.Utilities.FirebaseUtils;
import com.daytoday.business.dailydelivery.Utilities.Request;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.HashMap;
import java.util.List;

public class CalenderActivity extends AppCompatActivity {
    MaterialCalendarView calendarView;
    String bussID,custID,bussCustId;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        bussID  = getIntent().getStringExtra("buisness-Id");
        bussCustId  = getIntent().getStringExtra("Unique-Id");
        custID = getIntent().getStringExtra("Customer-Id");
        getSupportActionBar().setTitle("Calender");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        calendarView = findViewById(R.id.calendar);
        progressBar = findViewById(R.id.progress_bar);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        DatesViewModel datesViewModel = new DatesViewModel(bussCustId);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                final NumberPicker numberPicker = new NumberPicker(CalenderActivity.this);
                numberPicker.setMaxValue(20);
                numberPicker.setMinValue(1);
                CalendarDay day = CalendarDay.from(date.getYear(),date.getMonth(),date.getDay());
                AlertDialog.Builder builder = new AlertDialog.Builder(CalenderActivity.this);
                builder.setTitle("Add to Pending").setMessage("Enter Quantity");
                builder.setView(numberPicker);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createPendingRequest(date, String.valueOf(numberPicker.getValue()));
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        datesViewModel.getTotalList(calendarView.getCurrentDate()).observe(this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                calendarView.removeDecorators();
                for (Transaction transaction : transactions) {
                    int drawableResourceId = AppUtils.getResourceIdDates(transaction.getStatus());
                    CircleDecorator decorator = new CircleDecorator(CalenderActivity.this,drawableResourceId,transaction);
                    calendarView.addDecorator(decorator);
                }
            }
        });

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                datesViewModel.getTotalList(date);
            }
        });

        datesViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void createPendingRequest(CalendarDay day ,String quantity) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Buss_Cust_DayWise").child(bussCustId);
        HashMap<String,String> value = FirebaseUtils.getValueMapOfRequest(day, quantity,Request.PENDING);
        reference.child(FirebaseUtils.getDatePath(day))
                .setValue(value);
        FirebaseUtils.incrementAccToReq(day, reference,quantity, Request.PENDING);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
