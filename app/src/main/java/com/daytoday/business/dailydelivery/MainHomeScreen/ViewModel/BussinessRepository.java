package com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Bussiness;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class BussinessRepository {

    public MutableLiveData<List<Bussiness>> requestBussiness() {

        final MutableLiveData<List<Bussiness>> liveData = new MutableLiveData<>();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        //bussinesses.add(new Bussiness("Milk","Monthly","27","3000","https://ichef.bbci.co.uk/wwfeatures/live/976_549/images/live/p0/7v/vr/p07vvrpj.jpg"));
        //bussinesses.add(new Bussiness("Water","Monthly","30","3000","gooogle.com"));
        //bussinesses.add(new Bussiness("Newspaper","Half-Yearly","5","3000","gooogle.com"));
        //bussinesses.add(new Bussiness("Clothes","Weekly","10","3000","gooogle.com"));


        reference.child("User-Buss-Rel").child(currentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator iterator = dataSnapshot.getChildren().iterator();
                        Log.i("msg","asdf");
                        List<Bussiness> bussinesses = new ArrayList<>();
                        while (iterator.hasNext())
                        {
                            DataSnapshot currentSnapshot = (DataSnapshot) iterator.next();
                            Log.i("msgsnapshot",currentSnapshot.getKey());
                            firestore.collection("Buss-Info").document(currentSnapshot.getKey())
                                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                            Log.i("msgData",documentSnapshot.toString());
                                            String name = documentSnapshot.get("Name").toString();
                                            Log.i("msgData",name);
                                            String earning = documentSnapshot.get("Tot-Earn").toString();
                                            Log.i("msgData",earning);
                                            String price = documentSnapshot.get("Price").toString();
                                            Log.i("msgDta",price);
                                            String MD;
                                            MD = (String) documentSnapshot.get("M-Or-D");
                                            Log.i("msgDta",MD);
                                            String cust_cou = (String) documentSnapshot.get("No-Of-Cust");
                                            String pay_mode = (String) documentSnapshot.get("Pay-Mode");
                                            String bussId = currentSnapshot.getKey();
                                            if (name != null && earning != null && price != null && MD != null)
                                                bussinesses.add(new Bussiness(name,MD,price,earning,"gooogle.com",cust_cou,pay_mode,bussId));
                                            liveData.setValue(bussinesses);
                                        }
                                    });
                            Log.i("msgAns","NotDone");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return liveData;

    }
}
