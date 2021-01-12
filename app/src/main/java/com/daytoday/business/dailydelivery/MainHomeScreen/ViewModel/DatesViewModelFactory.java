package com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DatesViewModelFactory implements ViewModelProvider.Factory {

    private String bussCustID;

    public DatesViewModelFactory(String bussCustID)
    {
        this.bussCustID = bussCustID;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DatesViewModel(bussCustID);
    }
}
