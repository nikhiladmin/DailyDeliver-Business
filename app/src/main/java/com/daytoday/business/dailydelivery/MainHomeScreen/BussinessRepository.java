package com.daytoday.business.dailydelivery.MainHomeScreen;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class BussinessRepository {

    public MutableLiveData<List<Bussiness>> requestBussiness() {

        final MutableLiveData<List<Bussiness>> liveData = new MutableLiveData<>();


        List<Bussiness> bussinesses = new ArrayList<>();
        bussinesses.add(new Bussiness("Milk","Monthly","27","3000","https://ichef.bbci.co.uk/wwfeatures/live/976_549/images/live/p0/7v/vr/p07vvrpj.jpg"));
        bussinesses.add(new Bussiness("Water","Monthly","30","3000","gooogle.com"));
        bussinesses.add(new Bussiness("Newspaper","Half-Yearly","5","3000","gooogle.com"));
        bussinesses.add(new Bussiness("Clothes","Weekly","10","3000","gooogle.com"));
        bussinesses.add(new Bussiness("Tiffin","Monthly","60","3000","gooogle.com"));

        liveData.setValue(bussinesses);
        return liveData;

    }
}
