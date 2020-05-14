package com.daytoday.business.dailydelivery.MainHomeScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;

import com.daytoday.business.dailydelivery.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalenderActivity extends AppCompatActivity {
    MaterialCalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        getSupportActionBar().setTitle("Calender");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        calendarView = findViewById(R.id.calendar);
        DatesViewModel datesViewModel = new DatesViewModel();


        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Log.i("msg", String.valueOf(date.getMonth()));
                CalendarDay day = CalendarDay.from(2001,5,21);
                Log.i("msg", String.valueOf(day.toString()));
            }
        });

        datesViewModel.getAcceptedList().observe(this, new Observer<List<CalendarDay>>() {
            @Override
            public void onChanged(List<CalendarDay> calendarDays) {
                CircleDecorator decorator = new CircleDecorator(CalenderActivity.this,R.drawable.accepted_color,calendarDays);
                calendarView.addDecorators(decorator);
                Log.i("ans","done1");
            }
        });

        datesViewModel.getCancelledList().observe(this, new Observer<List<CalendarDay>>() {
            @Override
            public void onChanged(List<CalendarDay> calendarDays) {
                Log.i("ans","done3");
                CircleDecorator decorator = new CircleDecorator(CalenderActivity.this,R.drawable.canceled_color,calendarDays);
                calendarView.addDecorators(decorator);
            }
        });

        datesViewModel.getPendingList().observe(this, new Observer<List<CalendarDay>>() {
            @Override
            public void onChanged(List<CalendarDay> calendarDays) {
                CircleDecorator decorator = new CircleDecorator(CalenderActivity.this,R.drawable.pending_color,calendarDays);
                calendarView.addDecorators(decorator);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
