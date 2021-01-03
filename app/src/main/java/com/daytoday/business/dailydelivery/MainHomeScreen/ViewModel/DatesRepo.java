package com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.daytoday.business.dailydelivery.Network.Response.Transaction;
import com.daytoday.business.dailydelivery.Utilities.AppUtils;
import com.daytoday.business.dailydelivery.Utilities.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DatesRepo {

    public void requestTotalList(DatesViewModel datesViewModel, DatabaseReference reference) {
        reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Transaction> transactionsList = new ArrayList<>();
                        datesViewModel.isLoading.setValue(false);
                        Iterator iterator = dataSnapshot.getChildren().iterator();
                        while (iterator.hasNext()) {
                            DataSnapshot snapshot = ((DataSnapshot)iterator.next());
                            if (AppUtils.isNumerical(snapshot.getKey())) {
                                Transaction transaction = snapshot.getValue(Transaction.class);
                                transactionsList.add(transaction);
                            }
                            datesViewModel.getTotalLiveData().postValue(transactionsList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i("message",databaseError.getMessage());
                    }
                });
    }
}
