package com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.daytoday.business.dailydelivery.Network.Response.Transaction;
import com.daytoday.business.dailydelivery.Utilities.FirebaseUtils;
import com.daytoday.business.dailydelivery.Utilities.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Iterator;
import java.util.List;

public class DatesViewModel extends ViewModel {
    private DatesRepo datesRepo;
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<List<Transaction>> totalLiveData = new MutableLiveData<>();
    public MutableLiveData<String> totalAcceptedMonthlyLiveData = new MutableLiveData<>();
    public MutableLiveData<String> totalRejectedMonthlyLiveData = new MutableLiveData<>();
    public MutableLiveData<String> totalPendingMonthlyLiveData = new MutableLiveData<>();
    public MutableLiveData<String> totalAcceptedYearlyLiveData = new MutableLiveData<>();
    public MutableLiveData<String> totalRejectedYearlyLiveData = new MutableLiveData<>();
    public MutableLiveData<String> totalPendingYearlyLiveData = new MutableLiveData<>();
    public MutableLiveData<String> currentYear = new MutableLiveData<>();
    String bussCustId;

    public DatesViewModel(String busscustId) {
        datesRepo = new DatesRepo();
        this.bussCustId = busscustId;
        isLoading.setValue(false);
    }

    public MutableLiveData<List<Transaction>> getTotalList(CalendarDay currentDate) {
        isLoading.setValue(true);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Buss_Cust_DayWise").child(bussCustId)
                .child(FirebaseUtils.getAllMonthPath("" + currentDate.getYear(), currentDate.getMonth() + ""));
        getCurrentMonthTotal(currentDate);
        datesRepo.requestTotalList(this, reference);
        return totalLiveData;
    }

    public MutableLiveData<List<Transaction>> getTotalLiveData() {
        return totalLiveData;
    }

    /**
     * @param selectedDay
     * @return Null if no data is present
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Transaction getTransaction(CalendarDay selectedDay) {
        return totalLiveData.getValue().stream()
                .filter(transaction -> transaction.getYear().equals("" + selectedDay.getYear())
                        && transaction.getMon().equals("" + selectedDay.getMonth())
                        && transaction.getDay().equals("" + selectedDay.getDay()))
                .findFirst()
                .orElse(null);
    }

    private void getCurrentMonthTotal(CalendarDay day) {
        totalPendingMonthlyLiveData.setValue("0");
        totalRejectedMonthlyLiveData.setValue("0");
        totalAcceptedMonthlyLiveData.setValue("0");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Buss_Cust_DayWise").child(bussCustId)
                .child(FirebaseUtils.getAllMonthPath(day.getYear() + "", "" + day.getMonth())).child("Monthly-Total");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()) {
                    DataSnapshot snapshot = ((DataSnapshot) iterator.next());
                    if (snapshot.getKey().equals(Request.REJECTED)) {
                        totalRejectedMonthlyLiveData.setValue(snapshot.getValue() + "");
                    } else if (snapshot.getKey().equals(Request.PENDING)) {
                        totalPendingMonthlyLiveData.setValue(snapshot.getValue() + "");
                    } else {
                        totalAcceptedMonthlyLiveData.setValue(snapshot.getValue() + "");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getCurrentYearTotal(CalendarDay day) {
        totalPendingYearlyLiveData.setValue("0");
        totalRejectedYearlyLiveData.setValue("0");
        totalAcceptedYearlyLiveData.setValue("0");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Buss_Cust_DayWise").child(bussCustId)
                .child(day.getYear() + "").child("Yearly-Total");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()) {
                    DataSnapshot snapshot = ((DataSnapshot) iterator.next());
                    if (snapshot.getKey().equals(Request.REJECTED)) {
                        totalRejectedYearlyLiveData.setValue("" + snapshot.getValue());
                    } else if (snapshot.getKey().equals(Request.PENDING)) {
                        totalPendingYearlyLiveData.setValue("" + snapshot.getValue());
                    } else {
                        totalAcceptedYearlyLiveData.setValue("" + snapshot.getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
