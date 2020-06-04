package com.daytoday.business.dailydelivery.MainHomeScreen.UI;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.TimePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daytoday.business.dailydelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class BusinessAddition extends AppCompatActivity {

    Toolbar toolbar;
    RadioGroup rg1, rg2;
    TextInputLayout textInputLayout;
    TextInputEditText buisnessName,price;
    String monthOrDay,pay_mode;
    private ImageView productImg;
    private final String URL="https://api.icons8.com/api/iconsets/search?term=milk&amount=1";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_addition_activity);
        getSupportActionBar().setTitle("Business Addition");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        textInputLayout = findViewById(R.id.textInputLayout);
        //------------------changing colour of status bar through java startsss----------------------- //
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));
        //-----changing color of status bar ends here ------------------------------------------------------//
        buisnessName = findViewById(R.id.buss_name_add);
        price = findViewById(R.id.buss_add_price);
        rg1 = findViewById(R.id.radioGroup1);
        rg1.clearCheck();
        rg2 = findViewById(R.id.radioGroup2);
        rg2.clearCheck();

        productImg=findViewById(R.id.productImg);
        //-----------------------Example of Render Image through URL-------------------
        Picasso.get()
                .load("https://img.icons8.com/color/10x/milk.png")
                .resize(5000,5000)
                .centerCrop()
                .into(productImg);

        //---------------------listener on radio group-----------------------------------------------//
        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String temp;
                if (null != radioButton) {
                    temp = radioButton.getText().toString().trim();
                    if (temp.equals("Monthly")) {
                        textInputLayout.setSuffixText("/Month");
                        monthOrDay = "Monthly";
                    } else {
                        textInputLayout.setSuffixText("/Day");
                        monthOrDay = "Daily";
                    }
                }
            }
        });

        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String temp;
                if (null != radioButton) {
                    temp = radioButton.getText().toString().trim();
                    pay_mode = temp;
                }
            }
        });
    }

    //----------------------------------when the back button of toolbar is pressed--------------------------
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //------------------------------inflating the menu ie the tick logo------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.business_add_menu, menu);
        return true;
    }

    //---------------------------------implementing action on the tick icon on the toolbar-----------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_business:
                //Toast.makeText(this, "To be Added soon", Toast.LENGTH_SHORT).show();
                CreateInstanseInFirestore();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void CreateInstanseInFirestore() {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DocumentReference referenceofCollection = firestore.collection("Buss_Info").document();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        HashMap<String,String> data = new HashMap<>();
        data.put("productImg","https://img.icons8.com/color/10x/"+buisnessName.getText().toString().trim().toLowerCase()+".png");
        data.put("Name",buisnessName.getText().toString());
        data.put("No_Of_Cust","0");
        data.put("Price",price.getText().toString());
        data.put("Tot_Can","0");
        data.put("Tot_Earn","0");
        data.put("Tot_Pen","0");
        data.put("M_Or_D",monthOrDay);
        data.put("Pay_Mode",pay_mode);
        data.put("Address","RB II 671 / D A Road");
        data.put("PhoneNo",currentUser.getPhoneNumber());
        referenceofCollection.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("msg","done");
            }
        });
        reference.child("User_Buss_Rel").child(currentUser.getUid()).child(referenceofCollection.getId()).setValue(true);
        finish();
    }

}
