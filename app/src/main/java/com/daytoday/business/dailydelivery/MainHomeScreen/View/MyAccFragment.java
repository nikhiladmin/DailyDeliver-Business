package com.daytoday.business.dailydelivery.MainHomeScreen.View;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daytoday.business.dailydelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyAccFragment extends Fragment {
    FloatingActionButton floatingActionButton;
    MaterialTextView userName,currentID;
    TextInputEditText phoneNo,usernameEditText,buss_address;
    FirebaseAuth firebaseAuth;
    MaterialButton button;
    boolean flag=false;
    TextInputLayout textInputLayout1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_myaccount,container,false);
        floatingActionButton=view.findViewById(R.id.myacc_fab);
        firebaseAuth = FirebaseAuth.getInstance();
        userName = view.findViewById(R.id.UsersName);
        usernameEditText=view.findViewById(R.id.myacc_name);
        currentID = view.findViewById(R.id.currId);
        phoneNo = view.findViewById(R.id.myacc_phone);
        buss_address=view.findViewById(R.id.buss_acc_address);
        button=view.findViewById(R.id.myacc_button);
        textInputLayout1=view.findViewById(R.id.textInputLayout1);
        phoneNo.setEnabled(false);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"To be added soon",Snackbar.LENGTH_LONG).show();
            }
        });
        userName.setText(firebaseAuth.getCurrentUser().getDisplayName());
        usernameEditText.setText(firebaseAuth.getCurrentUser().getDisplayName().toUpperCase());
        currentID.setText("ID - " + firebaseAuth.getCurrentUser().getUid());
        phoneNo.setText(firebaseAuth.getCurrentUser().getPhoneNumber());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getActivity());
                alertDialog.setMessage("This will be reflected in all the customers you are connected.");
                alertDialog.setTitle("You are about to modify your profile details");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("I Understand", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateData();
                        Snackbar.make(getActivity().findViewById(android.R.id.content),"Changes will take sometime to reflect.",Snackbar.LENGTH_LONG).show();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Snackbar.make(getActivity().findViewById(android.R.id.content),"Profile Update Cancelled",Snackbar.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });
        getAddress();
        return view;
    }

    public void getAddress()
    {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth;
        firebaseAuth=FirebaseAuth.getInstance();
        Log.e("TAG", "getAddress: "+firebaseAuth.getUid() );
        firestore.collection("Buss_User_Info").document(firebaseAuth.getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        String name = documentSnapshot.get("Name").toString();
                        String address = documentSnapshot.get("Address").toString();
                        String PhoneNo = documentSnapshot.get("PhoneNo").toString();
                        Log.e("TAG", "onEvent: "+ address);
                        buss_address.setText(address);
                    }
                });
    }

    public void updateData()
    {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firestore.collection("Buss_User_Info").document(FirebaseAuth.getInstance().getUid());
        Map<String,Object> updateMap=new HashMap<>();
        updateMap.put("Name",usernameEditText.getText().toString().trim());
        updateMap.put("Address",buss_address.getText().toString().trim());
        documentReference.update(updateMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully updated!");
                        Snackbar.make(getActivity().findViewById(android.R.id.content),"Profile Updated Successfully",Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error updating document", e);
                        Snackbar.make(getActivity().findViewById(android.R.id.content),"Profile update failed. Try Again",Snackbar.LENGTH_LONG).show();
                    }
                });
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(usernameEditText.getText().toString().trim())
                .build();
        firebaseAuth.getCurrentUser().updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "User profile updated.");
                        }
                    }
                });
    }
}
