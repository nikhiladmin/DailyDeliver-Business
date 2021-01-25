package com.daytoday.business.dailydelivery.NotificationUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.daytoday.business.dailydelivery.NotificationUI.Notification;
import com.daytoday.business.dailydelivery.NotificationUI.NotificationAdapter;
import com.daytoday.business.dailydelivery.NotificationUI.NotificationViewModel;
import com.daytoday.business.dailydelivery.NotificationUI.NotificationViewModelFactory;
import com.daytoday.business.dailydelivery.R;
import com.daytoday.business.dailydelivery.Utilities.SaveOfflineManager;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    NotificationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recyclerView = findViewById(R.id.notification_recycler);
        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout = findViewById(R.id.pullToRefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final NotificationAdapter adapter = new NotificationAdapter(this);

        viewModel = new ViewModelProvider(this, new NotificationViewModelFactory(this.getApplication(), SaveOfflineManager.getUserId(this))).get(NotificationViewModel.class);
        viewModel.pagedListLiveData.observe(this, notifications -> adapter.submitList(notifications));
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            viewModel.refresh();
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}