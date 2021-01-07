package com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Customers;

import java.util.List;

public class CustomerViewModel extends ViewModel {
    private CustomerRepo customerRepo;
    private MutableLiveData<List<Customers>> mutableLiveData;
    String bussId;
    public CustomerViewModel(String bussId)
    {
        customerRepo = new CustomerRepo();
        this .bussId = bussId;
    }
    public LiveData<List<Customers>> getCustomers()
    {
        if(mutableLiveData==null)
            mutableLiveData = customerRepo.requestCustomers(bussId);
        return mutableLiveData;
    }
}
