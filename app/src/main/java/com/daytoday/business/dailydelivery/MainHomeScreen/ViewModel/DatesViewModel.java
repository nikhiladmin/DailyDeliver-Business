package com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Dates;
import com.daytoday.business.dailydelivery.Network.Response.DayWiseResponse;

import java.util.List;

public class DatesViewModel extends ViewModel {
    private DatesRepo datesRepo;
    private MutableLiveData<List<Dates>> pendinglivedata = new MutableLiveData<>();
    private MutableLiveData<List<Dates>> acceptedlivedata = new MutableLiveData<>();
    private MutableLiveData<List<Dates>> canceledlivedata = new MutableLiveData<>();
    String bussCustId;

    public DatesViewModel(String busscustId) {
        datesRepo = new DatesRepo();
        this.bussCustId = busscustId;
    }

    public MutableLiveData<List<Dates>> getPendingList()
    {
        pendinglivedata = datesRepo.requestPendingList(bussCustId);
        return pendinglivedata;
    }

    public MutableLiveData<List<Dates>> getAcceptedList()
    {
        acceptedlivedata = datesRepo.requestAcceptedList(bussCustId);
        return acceptedlivedata;
    }

    public MutableLiveData<List<Dates>> getCancelledList()
    {
        canceledlivedata = datesRepo.requestCancelledList(bussCustId);
        return canceledlivedata;
    }
}
