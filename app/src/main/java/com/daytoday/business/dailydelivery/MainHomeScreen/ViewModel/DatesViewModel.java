package com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.daytoday.business.dailydelivery.Network.Response.Transaction;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

public class DatesViewModel extends ViewModel {
    private DatesRepo datesRepo;
    private MutableLiveData<List<Transaction>> totalLiveData = new MutableLiveData<>();
    String bussCustId;

    public DatesViewModel(String busscustId) {
        datesRepo = new DatesRepo();
        this.bussCustId = busscustId;
    }

    public MutableLiveData<List<Transaction>> getTotalList(CalendarDay currentDate) {
        datesRepo.requestTotalList(totalLiveData,bussCustId,currentDate.getYear() + "",currentDate.getMonth() + "");
        return totalLiveData;
    }
}
