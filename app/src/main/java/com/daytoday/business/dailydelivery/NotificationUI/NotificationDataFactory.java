package com.daytoday.business.dailydelivery.NotificationUI;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class NotificationDataFactory extends DataSource.Factory {
    NotificationDataSource dataSource;
    MutableLiveData<NotificationDataSource> liveData;
    private String userid;

    public NotificationDataFactory(String userid) {
        this.userid = userid;
        liveData = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public DataSource create() {
        dataSource = new NotificationDataSource(userid);
        liveData.postValue(dataSource);
        return dataSource;
    }

    public MutableLiveData<NotificationDataSource> getNotificationLiveData() {
        return liveData;
    }
}
