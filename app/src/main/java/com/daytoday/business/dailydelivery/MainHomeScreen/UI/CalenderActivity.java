package com.daytoday.business.dailydelivery.MainHomeScreen.UI;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel.DatesViewModel;
import com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel.DatesViewModelFactory;
import com.daytoday.business.dailydelivery.Network.Response.RequestNotification;
import com.daytoday.business.dailydelivery.Network.Response.SendDataModel;
import com.daytoday.business.dailydelivery.Network.Response.Transaction;
import com.daytoday.business.dailydelivery.R;
import com.daytoday.business.dailydelivery.Utilities.AppUtils;
import com.daytoday.business.dailydelivery.Utilities.FirebaseUtils;
import com.daytoday.business.dailydelivery.Utilities.NotificationService;
import com.daytoday.business.dailydelivery.Utilities.Request;
import com.daytoday.business.dailydelivery.Utilities.SaveOfflineManager;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.HashMap;
import java.util.List;


public class CalenderActivity extends AppCompatActivity {
    private String productName;

    public static final String UNIQUE_ID = "unique_id";
    public static final String BUSINESS_ID = "business_id";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String MOrD = "MOrD";
    public static final String PRODUCT_PRICE = "price";
    public static final String CUSTOMER_NAME = "customer_name";
    public static final String PRODUCT_NAME = "Product_Name";
    public static final String CUSTOMER_TOKEN = "Customer_Token";

    MaterialCalendarView calendarView;
    String bussID, custID, bussCustId, bussType,custName,custToken;
    int bussPrice;
    ProgressBar progressBar;
    MaterialTextView totalAccepted, totalRejected, totalPending, totalPriceTextView, currentMonthPriceTextView;
    MaterialTextView totalMonthAccepted, totalMonthRejected, totalMonthPending;
    DatesViewModel datesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calender);
        bussID = getIntent().getStringExtra(BUSINESS_ID);
        bussCustId = getIntent().getStringExtra(UNIQUE_ID);
        custID = getIntent().getStringExtra(CUSTOMER_ID);
        bussType = getIntent().getStringExtra(MOrD);
        bussPrice = getIntent().getIntExtra(PRODUCT_PRICE, 0);
        custName = getIntent().getStringExtra(CUSTOMER_NAME);
        productName = getIntent().getStringExtra(PRODUCT_NAME);
        custToken = getIntent().getStringExtra(CUSTOMER_TOKEN);

        getSupportActionBar().setTitle(custName+" - Records");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        calendarView = findViewById(R.id.calendar);
        progressBar = findViewById(R.id.progress_bar);
        totalAccepted = findViewById(R.id.total_accepted);
        totalPending = findViewById(R.id.total_pending);
        totalRejected = findViewById(R.id.total_cancelled);
        totalPriceTextView = findViewById(R.id.total_price);
        totalMonthAccepted = findViewById(R.id.month_accepted);
        totalMonthPending = findViewById(R.id.month_pending);
        totalMonthRejected = findViewById(R.id.month_cancelled);
        currentMonthPriceTextView = findViewById(R.id.current_month_price);

        datesViewModel = new ViewModelProvider(this, new DatesViewModelFactory(bussCustId)).get(DatesViewModel.class);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                final NumberPicker numberPicker = new NumberPicker(CalenderActivity.this);
                numberPicker.setMaxValue(20);
                numberPicker.setMinValue(1);
                CalendarDay day = CalendarDay.from(date.getYear(), date.getMonth(), date.getDay());
                AlertDialog.Builder builder = new AlertDialog.Builder(CalenderActivity.this);
                builder.setTitle("Add to Pending").setMessage("Enter Quantity");
                builder.setView(numberPicker);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createPendingRequest(date, String.valueOf(numberPicker.getValue()));
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });

        datesViewModel.currentYear.setValue("" + calendarView.getCurrentDate().getYear());
        datesViewModel.getCurrentYearTotal(calendarView.getCurrentDate());

        datesViewModel.getTotalList(calendarView.getCurrentDate()).observe(this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                calendarView.removeDecorators();
                for (Transaction transaction : transactions) {
                    int drawableResourceId = AppUtils.getResourceIdDates(transaction.getStatus());
                    CircleDecorator decorator = new CircleDecorator(CalenderActivity.this, drawableResourceId, transaction);
                    calendarView.addDecorator(decorator);
                }
            }
        });

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                datesViewModel.getTotalList(date);
                if (!datesViewModel.currentYear.getValue().equals(date.getYear() + "")) {
                    datesViewModel.currentYear.setValue(date.getYear() + "");
                    datesViewModel.getCurrentYearTotal(date);
                }
            }
        });

        datesViewModel.isLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        datesViewModel.totalAcceptedMonthlyLiveData.observe(this, s -> {
            totalMonthAccepted.setText("" + s);
            if (bussType != null && !bussType.isEmpty() && bussType.equals("D")) {
                int thisMonthPrice = Integer.parseInt(s != null ? s : "0");
                currentMonthPriceTextView.setText("₹" + (thisMonthPrice * bussPrice));
            } else {
                currentMonthPriceTextView.setText("₹" + bussPrice);
            }
        });
        datesViewModel.totalRejectedMonthlyLiveData.observe(this, s -> totalMonthRejected.setText("" + s));
        datesViewModel.totalPendingMonthlyLiveData.observe(this, s -> totalMonthPending.setText("" + s));

        datesViewModel.totalAcceptedYearlyLiveData.observe(this, s -> {
            totalAccepted.setText("" + s);
            if (bussType != null && !bussType.isEmpty() && bussType.equals("D")) {
                int thisYearPrice = Integer.parseInt(s != null ? s : "0");
                totalPriceTextView.setText("₹" + (thisYearPrice * bussPrice));
            } else {
                totalPriceTextView.setText("₹" + bussPrice);
            }
        });
        datesViewModel.totalRejectedYearlyLiveData.observe(this, s -> totalRejected.setText("" + s));
        datesViewModel.totalPendingYearlyLiveData.observe(this, s -> totalPending.setText("" + s));

    }

    private void createPendingRequest(CalendarDay day ,String quantity) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Buss_Cust_DayWise").child(bussCustId);
        HashMap<String,String> value = FirebaseUtils.getValueMapOfRequest(day, quantity,Request.PENDING);
        reference.child(FirebaseUtils.getDatePath(day))
                .setValue(value);
        FirebaseUtils.incrementAccToReq(day, reference,quantity, Request.PENDING);
        RequestNotification requestNotification = new RequestNotification()
                .setToken(custToken)
                .setSendDataModel(new SendDataModel()
                        .setFromWhichPerson(SaveOfflineManager.getUserName(this))
                        .setFromWhichPersonID(SaveOfflineManager.getUserId(this))
                        .setNotificationStatus(Request.PENDING)
                        .setProductName(productName)
                        .setQuantity(quantity)
                        .setToWhichPerson(custName));
        NotificationService.sendNotification(requestNotification);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
