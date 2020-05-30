package com.daytoday.business.dailydelivery.MainHomeScreen;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

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



        //result.add(new Customers("shuam Kumar","flat 507 Prabhatam heights","Milk","google.com","+919359270125"));
        //result.add(new Customers("Divyanshu Verma","flat 507 Prabhatam heights","Egg","google.com","+919359270125"));
        //result.add(new Customers("Aditya Mishra","flat 507 Prabhatam heights","NewsPaper","google.com","+919359270125"));
        //result.add(new Customers("Anshansh Suman","flat 507 Prabhatam heights","Water","google.com","+919359270125"));
        //result.add(new Customers("Nikhil Kumar","flat 507 Prabhatam heights","Food","google.com","+919359270125"));
        //result.add(new Customers("Haria Jhaduwala","flat 507 Prabhatam heights","Essentials","google.com","+919359270125"));

        reference.child("Buss-Cust-Rel").child(bussId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                List<Customers> result = new ArrayList<>();
                while (iterator.hasNext())
                {
                    DataSnapshot currentSnapShot = (DataSnapshot)iterator.next();
                    Log.i("msgresult",currentSnapShot.getKey());
                    firestore.collection("Cust-User-Info").document(currentSnapShot.getKey())
                            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
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
