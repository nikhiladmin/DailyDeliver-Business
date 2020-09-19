package com.daytoday.business.dailydelivery.MainHomeScreen.UI;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Bussiness;
import com.daytoday.business.dailydelivery.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class BusinessDetailActivity extends AppCompatActivity {
    RadioGroup rg1, rg2;
    TextInputEditText buss_name, buss_price, buss_address;
    Button button;
    Bussiness bussiness;
    String paymode, mord;
    ImageView bussImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buisness_detail);
        bussiness = getIntent().getParcelableExtra("buisness-object");
        getSupportActionBar().setTitle(bussiness.getName()+" - Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        Log.e("TAG", "onCreate: "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        rg1 = findViewById(R.id.radioGroup1);
        rg2 = findViewById(R.id.radioGroup2);
        buss_name = findViewById(R.id.buss_name);
        buss_price = findViewById(R.id.buss_price);
        buss_address = findViewById(R.id.buss_address);
        bussImg = findViewById(R.id.BusinessImg);
        button = findViewById(R.id.submitbutton);
        setFalse();
        buss_name.setText(bussiness.getName());
        buss_price.setText(bussiness.getPrice());
        Picasso.get()
                .load(bussiness.getImgurl())
                .resize(5000,5000)
                .centerCrop().into(bussImg);
        Log.e("TAG", "onCreate: " + bussiness.getdOrM());
        buss_address.setText(bussiness.getAddress());
        if (bussiness.getdOrM().equals("Daily")) {
            rg1.check(R.id.radio_btn_daily);
            mord="Daily";
        } else {
            rg1.check(R.id.radio_btn_monthly);
            mord="Monthly";
        }

        if (bussiness.getPayment().equals("Cash")) {
            rg2.check(R.id.radio_btn_cash);
            paymode="Cash";
        } else if (bussiness.getPayment().equals("Online")) {
            rg2.check(R.id.radio_btn_online);
            paymode="Online";
        } else {
            paymode="Both";
            rg2.check(R.id.radio_btn_both);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(BusinessDetailActivity.this);
                alertDialog.setMessage("You are about to modify to your business details");
                alertDialog.setTitle("Please Confirm");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateData();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Snackbar.make(v,"Data Update Cancelled",Snackbar.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Toast.makeText(this, "Editing Business", Toast.LENGTH_SHORT).show();
                button.setEnabled(true);
                buss_name.setEnabled(true);
                buss_price.setEnabled(true);
                buss_address.setEnabled(true);
                for (int i = 0; i < rg1.getChildCount(); i++)
                    rg1.getChildAt(i).setEnabled(true);
                for (int i = 0; i < rg2.getChildCount(); i++)
                    rg2.getChildAt(i).setEnabled(true);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void updateData() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("Name", buss_name.getText().toString().trim());
        updates.put("Address", buss_address.getText().toString().trim());
        rg1.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
            if (null != radioButton) {
                mord = radioButton.getText().toString().trim();
            }
        });
        rg2.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
            if (null != radioButton) {
                paymode = radioButton.getText().toString().trim();
            }
        });
        updates.put("M_Or_D", mord);
        updates.put("Pay_Mode", paymode);
        updates.put("Price", buss_price.getText().toString().trim());
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firestore.collection("Buss_Info").document("" + bussiness.getBussid());
        Log.e("TAG", "updateData: " + paymode + mord);
        documentReference.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully updated!");
                        Snackbar.make(findViewById(android.R.id.content),"Data Updated Successfully",Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error updating document", e);
                        Snackbar.make(findViewById(android.R.id.content),"Data update failed. Try Again",Snackbar.LENGTH_LONG).show();
                    }
                });
        setFalse();
    }

    public void setFalse()
    {
        button.setEnabled(false);
        buss_name.setEnabled(false);
        buss_price.setEnabled(false);
        buss_address.setEnabled(false);
        for (int i = 0; i < rg1.getChildCount(); i++)
            rg1.getChildAt(i).setEnabled(false);
        for (int i = 0; i < rg2.getChildCount(); i++)
            rg2.getChildAt(i).setEnabled(false);
    }

}
