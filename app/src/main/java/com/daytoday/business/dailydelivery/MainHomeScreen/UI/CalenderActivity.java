package com.daytoday.business.dailydelivery.MainHomeScreen.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.NumberPicker;

import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Dates;
import com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel.DatesViewModel;
import com.daytoday.business.dailydelivery.R;
import com.daytoday.business.dailydelivery.Utilities.FirebaseUtils;
import com.daytoday.business.dailydelivery.Utilities.Request;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.HashMap;
import java.util.List;

public class CalenderActivity extends AppCompatActivity {
    MaterialCalendarView calendarView;
    String bussID,custID,bussCustId;
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

        datesViewModel.getAcceptedList().observe(this, new Observer<List<Dates>>() {
            @Override
            public void onChanged(List<Dates> dates) {
                CircleDecorator decorator = new CircleDecorator(CalenderActivity.this,R.drawable.accepted_color,dates);
                calendarView.addDecorators(decorator);
            }
        });

        datesViewModel.getCancelledList().observe(this, new Observer<List<Dates>>() {
            @Override
            public void onChanged(List<Dates> dates) {
                CircleDecorator decorator = new CircleDecorator(CalenderActivity.this,R.drawable.canceled_color,dates);
                calendarView.addDecorators(decorator);
            }
        });

        datesViewModel.getPendingList().observe(this, new Observer<List<Dates>>() {
            @Override
            public void onChanged(List<Dates> dates) {
                CircleDecorator decorator = new CircleDecorator(CalenderActivity.this,R.drawable.pending_color,dates);
                calendarView.addDecorators(decorator);
            }
        });
    }

    private void createPendingRequest(CalendarDay day ,String quantity) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Buss_Cust_DayWise").child(bussCustId);
        HashMap<String,String> value = FirebaseUtils.getValueMapOfRequest(day, quantity,Request.PENDING);
//        reference.child("Buss_Cust_DayWise").child(bussCustId).child("Pending")
//                .child("" + day.getYear() + day.getMonth() + day.getDay()).setValue(value);
//        reference.child("Buss_Cust_DayWise").child(bussCustId).child("Rejected")
//                .child("" + day.getYear() + day.getMonth() + day.getDay()).removeValue();
        reference.child(FirebaseUtils.getDatePath(day))
                .setValue(value);
        FirebaseUtils.incrementAccToReq(day, reference, Request.PENDING);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
