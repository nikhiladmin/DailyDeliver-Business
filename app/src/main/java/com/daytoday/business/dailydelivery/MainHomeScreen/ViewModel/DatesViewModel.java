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
    private MutableLiveData<DayWiseResponse> alllistLiveData = new MutableLiveData<>();
    String bussId,custId,bussCustId;

    public DatesViewModel(String busscustId,String bussId, String custId) {
        datesRepo = new DatesRepo();
        this.bussCustId = busscustId;
        this.bussId = bussId;
        this.custId = custId;
    }

    public MutableLiveData<List<Dates>> getPendingList()
    {
        pendinglivedata = datesRepo.requestPendingList(bussId,custId);
        return pendinglivedata;
    }

    public MutableLiveData<List<Dates>> getAcceptedList()
    {
        acceptedlivedata = datesRepo.requestAcceptedList(bussId,custId);
        return acceptedlivedata;
    }

    public MutableLiveData<List<Dates>> getCancelledList()
    {
        canceledlivedata = datesRepo.requestCancelledList(bussId,custId);
        return canceledlivedata;
    }

    public MutableLiveData<DayWiseResponse> getListFromApi()
    {
        alllistLiveData = datesRepo.requestDateFromApi(bussCustId);
        return alllistLiveData;
    }
}
