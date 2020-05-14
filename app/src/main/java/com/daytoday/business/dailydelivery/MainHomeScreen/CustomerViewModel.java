package com.daytoday.business.dailydelivery.MainHomeScreen;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class CustomerViewModel extends ViewModel {
    private CustomerRepo customerRepo;
    private MutableLiveData<List<Customers>> mutableLiveData = new MutableLiveData<>();
    public CustomerViewModel()
    {
        customerRepo = new CustomerRepo();
    }
    public LiveData<List<Customers>> getCustomers()
    {
        mutableLiveData = customerRepo.requestCustomers();
        Log.i("msg","done2");
        return mutableLiveData;
    }
}
