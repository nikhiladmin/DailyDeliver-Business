package com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Customers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomerRepo {
    public MutableLiveData<List<Customers>> requestCustomers(String bussId) {
        final MutableLiveData<List<Customers>> liveData = new MutableLiveData<>();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        /*-------------------------------          Initialise the list here   -----------------------------------------------------------*/

        reference.child("Buss_Cust_Rel").child(bussId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                List<Customers> result = new ArrayList<>();
                while (iterator.hasNext())
                {
                    DataSnapshot currentSnapShot = (DataSnapshot)iterator.next();
                    firestore.collection("Cust_User_Info").document(currentSnapShot.getKey()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            String name = documentSnapshot.get("Name").toString();
                            String address = documentSnapshot.get("Address").toString();
                            String PhoneNo = documentSnapshot.get("PhoneNo").toString();
                            result.add(new Customers(name,address,currentSnapShot.getKey(),"ImageUrl",PhoneNo));
                            liveData.setValue(result);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*-------------------------------------------------------------------------------------------------------------------------------*/
        return liveData;
    }
}
