package com.daytoday.business.dailydelivery.MainHomeScreen;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepo {
    public MutableLiveData<List<Customers>> requestCustomers() {
        final MutableLiveData<List<Customers>> liveData = new MutableLiveData<>();
        /*-------------------------------          Initialise the list here   -----------------------------------------------------------*/


        List<Customers> result = new ArrayList<>();
        result.add(new Customers("shuam Kumar","flat 507 Prabhatam heights","Milk","google.com","+919359270125"));
        result.add(new Customers("Divyanshu Verma","flat 507 Prabhatam heights","Egg","google.com","+919359270125"));
        result.add(new Customers("Aditya Mishra","flat 507 Prabhatam heights","NewsPaper","google.com","+919359270125"));
        result.add(new Customers("Anshansh Suman","flat 507 Prabhatam heights","Water","google.com","+919359270125"));
        result.add(new Customers("Nikhil Kumar","flat 507 Prabhatam heights","Food","google.com","+919359270125"));
        result.add(new Customers("Haria Jhaduwala","flat 507 Prabhatam heights","Essentials","google.com","+919359270125"));


        Log.i("msg","done1");
        /*-------------------------------------------------------------------------------------------------------------------------------*/
        liveData.setValue(result);
        return liveData;
    }
}
