package com.daytoday.business.dailydelivery.MainHomeScreen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class EmployeeRepo {

    public MutableLiveData<List<EmployeeInfo>> requestEmployee(){
        final MutableLiveData<List<EmployeeInfo>> livedata = new MutableLiveData<>();
        /*-------------------------------          Initialise the list here   -----------------------------------------------------------*/


        List<EmployeeInfo> result = new ArrayList<>();
        result.add(new EmployeeInfo("Aditya","123456","+919890775442","google.com"));
        result.add(new EmployeeInfo("Aditya","123456","+919890775442","google.com"));
        result.add(new EmployeeInfo("Aditya","123456","+919890775442","google.com"));
        result.add(new EmployeeInfo("Aditya","123456","+919890775442","google.com"));


        /*-------------------------------------------------------------------------------------------------------------------------------*/
        livedata.setValue(result);
        return livedata;
    }
}
