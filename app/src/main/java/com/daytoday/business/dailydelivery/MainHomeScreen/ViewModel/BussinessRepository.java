package com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Bussiness;
import com.daytoday.business.dailydelivery.Network.ApiInterface;
import com.daytoday.business.dailydelivery.Network.Client;
import com.daytoday.business.dailydelivery.Network.Response.BussDetailsResponse;
import com.daytoday.business.dailydelivery.Utilities.AppConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BussinessRepository {

    public MutableLiveData<List<Bussiness>> requestBussiness() {

        final MutableLiveData<List<Bussiness>> liveData = new MutableLiveData<>();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        ApiInterface apiInterface = Client.getClient().create(ApiInterface.class);

        /*reference.child("User_Buss_Rel").child(currentUser.getUid())
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
                                    String ImageUrl = documentSnapshot.get("productImg").toString();
                                    Log.e("TAG", "onEvent: "+address );
                                    String bussId = currentSnapshot.getKey();
                                    String tot_pending=documentSnapshot.get("Tot_Pen").toString();
                                    String tot_cancelled=documentSnapshot.get("Tot_Can").toString();
                                    if (name != null && earning != null && price != null && MD != null)
                                        //bussinesses.add(new Bussiness(name,MD,price,earning,ImageUrl,cust_cou,pay_mode,bussId,address,tot_pending,tot_cancelled));
                                    liveData.setValue(bussinesses);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/

        Call<BussDetailsResponse> bussDetailsResponseCall = apiInterface.getBussList(""  + currentUser);
        bussDetailsResponseCall.enqueue(new Callback<BussDetailsResponse>() {
            @Override
            public void onResponse(Call<BussDetailsResponse> call, Response<BussDetailsResponse> response) {

            }

            @Override
            public void onFailure(Call<BussDetailsResponse> call, Throwable t) {
                Log.i(AppConstants.ERROR_LOG,"Some error Occurred in BussinessRepository");
            }
        });
        return liveData;

    }
}
