package com.daytoday.business.dailydelivery.MainHomeScreen;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;

public class DatesRepo {
    public MutableLiveData<List<CalendarDay>> requestPendingList() {
        MutableLiveData<List<CalendarDay>> liveData = new MutableLiveData<>();
        List<CalendarDay> list = new ArrayList<>();
        list.add(CalendarDay.from(2020,5,21));
        list.add(CalendarDay.from(2020,5,23));
        list.add(CalendarDay.from(2020,5,04));
        list.add(CalendarDay.from(2020,5,03));

        liveData.setValue(list);
        return liveData;
    }

    public MutableLiveData<List<CalendarDay>> requestAcceptedList() {
        MutableLiveData<List<CalendarDay>> liveData = new MutableLiveData<>();
        List<CalendarDay> list = new ArrayList<>();
        list.add(CalendarDay.from(2020,5,18));
        list.add(CalendarDay.from(2020,5,17));
        list.add(CalendarDay.from(2020,5,16));
        list.add(CalendarDay.from(2020,5,15));
        Log.i("ans","done2");

        liveData.setValue(list);
        return liveData;
    }

    public MutableLiveData<List<CalendarDay>> requestCancelledList() {
        MutableLiveData<List<CalendarDay>> liveData = new MutableLiveData<>();
        List<CalendarDay> list = new ArrayList<>();
        list.add(CalendarDay.from(2020,5,11));
        list.add(CalendarDay.from(2020,5,13));
        list.add(CalendarDay.from(2020,5,01));
        list.add(CalendarDay.from(2020,5,02));


        liveData.setValue(list);
        return liveData;
    }
}
