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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daytoday.business.dailydelivery.Network.ApiInterface;
import com.daytoday.business.dailydelivery.Network.Client;
import com.daytoday.business.dailydelivery.Network.Response.YesNoResponse;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessAddition extends AppCompatActivity {

    Toolbar toolbar;
    RadioGroup rg1, rg2;
    TextInputLayout textInputLayout;
    TextInputEditText buisnessName,price;
    ApiInterface apiInterface;
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
        apiInterface = Client.getClient().create(ApiInterface.class);
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
                        monthOrDay = "M";
                    } else {
                        textInputLayout.setSuffixText("/Day");
                        monthOrDay = "D";
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
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String bussname = buisnessName.getText().toString();
        String priceofbuss = price.getText().toString();
        Call<YesNoResponse> addbusscall = apiInterface.addBussDetails(bussname,monthOrDay,pay_mode,priceofbuss,currentUser.getUid());
        addbusscall.enqueue(new Callback<YesNoResponse>() {
            @Override
            public void onResponse(Call<YesNoResponse> call, Response<YesNoResponse> response) {
                Log.i("message","Response successful " + response.body().getMessage());
                finish();
            }

            @Override
            public void onFailure(Call<YesNoResponse> call, Throwable t) {
                Toast.makeText(BusinessAddition.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
