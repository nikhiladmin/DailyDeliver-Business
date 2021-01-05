package com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.daytoday.business.dailydelivery.Network.Response.Transaction;
import com.daytoday.business.dailydelivery.Utilities.FirebaseUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

public class DatesViewModel extends ViewModel {
    private DatesRepo datesRepo;
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<List<Transaction>> totalLiveData = new MutableLiveData<>();
    String bussCustId;

    public DatesViewModel(String busscustId) {
        datesRepo = new DatesRepo();
        this.bussCustId = busscustId;
        isLoading.setValue(false);
    }

    public MutableLiveData<List<Transaction>> getTotalList(CalendarDay currentDate) {
        isLoading.setValue(true);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Buss_Cust_DayWise").child(bussCustId)
                .child(FirebaseUtils.getAllMonthPath("" + currentDate.getYear(),currentDate.getMonth() + ""));
        datesRepo.requestTotalList(this,reference);
        return totalLiveData;
    }

    public MutableLiveData<List<Transaction>> getTotalLiveData() {
        return totalLiveData;
    }

    /**
     *
     * @param selectedDay
     * @return Null if no data is present
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Transaction getTransaction(CalendarDay selectedDay){
        return totalLiveData.getValue().stream()
                .filter(transaction -> transaction.getYear().equals("" + selectedDay.getYear())
                        && transaction.getMon().equals("" + selectedDay.getMonth())
                        && transaction.getDay().equals("" + selectedDay.getDay()))
                .findFirst()
                .orElse(null);
    }
}
