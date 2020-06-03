package com.daytoday.business.dailydelivery.MainHomeScreen.View;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daytoday.business.dailydelivery.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;

public class MyAccFragment extends Fragment {
    FloatingActionButton floatingActionButton;
    MaterialTextView userName,currentID;
    TextInputEditText phoneNo,usernameEditText;
    FirebaseAuth firebaseAuth;
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
        return view;
    }
}
