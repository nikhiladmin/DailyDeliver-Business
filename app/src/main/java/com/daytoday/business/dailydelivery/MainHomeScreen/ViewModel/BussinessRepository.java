package com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Bussiness;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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


        reference.child("User_Buss_Rel").child(currentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator iterator = dataSnapshot.getChildren().iterator();
                        List<Bussiness> bussinesses = new ArrayList<>();
                        while (iterator.hasNext())
                        {
                            DataSnapshot currentSnapshot = (DataSnapshot) iterator.next();
                            firestore.collection("Buss_Info").document(currentSnapshot.getKey()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    String name = documentSnapshot.get("Name").toString();
                                    String earning = documentSnapshot.get("Tot_Earn").toString();
                                    String price = documentSnapshot.get("Price").toString();
                                    String MD = (String) documentSnapshot.get("M_Or_D");
                                    String cust_cou = (String) documentSnapshot.get("No_Of_Cust");
                                    String pay_mode = (String) documentSnapshot.get("Pay_Mode");
                                    String address=(String)documentSnapshot.get("Address");
                                    Log.e("TAG", "onEvent: "+address );
                                    String bussId = currentSnapshot.getKey();
                                    String tot_pending=documentSnapshot.get("Tot_Pen").toString();
                                    String tot_cancelled=documentSnapshot.get("Tot_Can").toString();
                                    if (name != null && earning != null && price != null && MD != null)
                                        bussinesses.add(new Bussiness(name,MD,price,earning,"gooogle.com",cust_cou,pay_mode,bussId,address,tot_pending,tot_cancelled));
                                    liveData.setValue(bussinesses);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return liveData;

    }
}
