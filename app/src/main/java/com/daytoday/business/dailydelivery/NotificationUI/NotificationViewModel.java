package com.daytoday.business.dailydelivery.NotificationUI;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NotificationViewModel extends AndroidViewModel {
    MutableLiveData<NotificationDataSource> notificationDataSourceMutableLiveData;
    NotificationDataFactory notificationDataFactory;
    Executor executor;
    public LiveData<PagedList<Notification>> pagedListLiveData;

    public NotificationViewModel(@NonNull Application application, String userid) {
        super(application);
        notificationDataFactory = new NotificationDataFactory(userid);
        notificationDataSourceMutableLiveData = notificationDataFactory.getNotificationLiveData();
        executor = Executors.newFixedThreadPool(5);
        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .setPrefetchDistance(4)
                .build();
        pagedListLiveData = (new LivePagedListBuilder<Integer, Notification>(notificationDataFactory, config))
                .setFetchExecutor(executor)
                .build();
    }

    public LiveData<PagedList<Notification>> getPagedListLiveData() {
        return pagedListLiveData;
    }

    public void refresh() {
        if (notificationDataSourceMutableLiveData.getValue() != null)
            notificationDataSourceMutableLiveData.getValue().invalidate();
    }
}
