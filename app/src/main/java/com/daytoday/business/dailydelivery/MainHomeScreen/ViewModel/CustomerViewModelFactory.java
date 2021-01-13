package com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CustomerViewModelFactory implements ViewModelProvider.Factory {

    String bussid;
    public CustomerViewModelFactory(String bussid)
    {
        this.bussid = bussid;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CustomerViewModel(bussid);
    }
}
