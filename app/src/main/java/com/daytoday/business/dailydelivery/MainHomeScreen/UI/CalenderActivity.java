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
        DatesViewModel datesViewModel = new DatesViewModel(bussCustId,bussID,custID);

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
        /*HashMap<String,String> value = new HashMap<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        value.put("Year", String.valueOf(day.getYear()));
        value.put("Mon", String.valueOf(day.getMonth()));
        value.put("Day", String.valueOf(day.getDay()));
        value.put("quantity",quantity);
        reference.child("Buss_Cust_DayWise").child(bussID).child(custID).child("Pending")
                .child("" + day.getYear() + day.getMonth() + day.getDay()).setValue(value);
        reference.child("Buss_Cust_DayWise").child(bussID).child(custID).child("Rejected")
                .child("" + day.getYear() + day.getMonth() + day.getDay()).removeValue();*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
