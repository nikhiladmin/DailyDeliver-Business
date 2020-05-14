package com.daytoday.business.dailydelivery.MainHomeScreen;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

public class DatesViewModel extends ViewModel {
    private DatesRepo datesRepo;
    private MutableLiveData<List<CalendarDay>> pendinglivedata = new MutableLiveData<>();
    private MutableLiveData<List<CalendarDay>> acceptedlivedata = new MutableLiveData<>();
    private MutableLiveData<List<CalendarDay>> canceledlivedata = new MutableLiveData<>();

    public DatesViewModel() {
        datesRepo = new DatesRepo();
    }

    public MutableLiveData<List<CalendarDay>> getPendingList()
    {
        pendinglivedata = datesRepo.requestPendingList();
        return pendinglivedata;
    }

    public MutableLiveData<List<CalendarDay>> getAcceptedList()
    {
        acceptedlivedata = datesRepo.requestAcceptedList();
        return acceptedlivedata;
    }

    public MutableLiveData<List<CalendarDay>> getCancelledList()
    {
        canceledlivedata = datesRepo.requestCancelledList();
        return canceledlivedata;
    }
}
