package com.daytoday.business.dailydelivery.MainHomeScreen.UI;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.CompoundButton;
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
import com.daytoday.business.dailydelivery.LargeImageView;
import com.daytoday.business.dailydelivery.Network.ApiInterface;
import com.daytoday.business.dailydelivery.Network.Client;
import com.daytoday.business.dailydelivery.Network.Response.YesNoResponse;
import com.daytoday.business.dailydelivery.R;
import com.daytoday.business.dailydelivery.Utilities.SaveOfflineManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessAddition extends AppCompatActivity {

    Toolbar toolbar;
    RadioGroup rg1, rg2;
    TextInputLayout textInputLayout,namelayout,pricelayout,phonelayout,addresslayout;
    TextInputEditText buisnessName, price,bussphonenumber,bussaddress;
    ApiInterface apiInterface;
    String monthOrDay, pay_mode;
    CircleImageView profilepic;
    FloatingActionButton profilePicUploadbtn;
    CheckBox samephone,sameaddress;
    RadioButton radioType1,radioType2,paymentType1,paymentType2,paymentType3;
    private final String URL = "https://api.icons8.com/api/iconsets/search?term=milk&amount=1";

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

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

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
        profilePicUploadbtn = findViewById(R.id.profilepiceditbtn);
        profilepic = findViewById(R.id.business_profile_pic);
        rg2.clearCheck();
        samephone = findViewById(R.id.samephonecheckbox);
        sameaddress = findViewById(R.id.sameaddresscheckbox);
        bussaddress = findViewById(R.id.buss_address);
        bussphonenumber = findViewById(R.id.buss_phone);
        namelayout = findViewById(R.id.name_layout);
        pricelayout = findViewById(R.id.price_layout);
        phonelayout = findViewById(R.id.phone_layout);
        addresslayout = findViewById(R.id.address_layout);
        radioType1 = findViewById(R.id.radio_btn_monthly);
        radioType2 = findViewById(R.id.radio_btn_daily);
        paymentType1 = findViewById(R.id.radio_btn_cash);
        paymentType2 = findViewById(R.id.radio_btn_online);
        paymentType3 = findViewById(R.id.radio_btn_both);

        //---------------------listener on radio group-----------------------------------------------//
        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String temp;
                if (null != radioButton) {
                    temp = radioButton.getText().toString().trim();
                    if (temp.equals("Monthly")) {
                        pricelayout.setSuffixText("/Month");
                        monthOrDay = "M";
                    } else {
                        pricelayout.setSuffixText("/Day");
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
        profilePicUploadbtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT).setType("image/*");
            startActivityForResult(intent, 1);
        });
        profilepic.setOnClickListener(v->{
            if(picture==null)
            {
                new LargeImageView(getApplicationContext(),v,"http://dailydeliver.000webhostapp.com/img/business_default.png",null);
            }else
            {
                new LargeImageView(getApplicationContext(),v,null,picture);
            }
        });
        samephone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
            {
                bussphonenumber.setText(SaveOfflineManager.getUserPhoneNumber(getBaseContext()));
            }else
            {
                bussphonenumber.setText("");
            }
        });
        sameaddress.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
            {
                bussaddress.setText(SaveOfflineManager.getUserAddress(getBaseContext()));
            }else
            {
                bussaddress.setText("");
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
                allFieldsValid();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private FirebaseStorage storage;
    private StorageReference storageRef;
    String uploadedPicName;

    private void CreateInstanseInDatabase() {
        ProgressDialog progressDialog =new ProgressDialog(BusinessAddition.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please Wait...");
        progressDialog.show();
        uploadedPicName = "pic" + System.currentTimeMillis();
        StorageReference reference = storageRef.child("BusinessUser/" + uploadedPicName);
        if (picture != null) {
            reference.putFile(picture).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            addNewBusiness(uri.toString());
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BusinessAddition.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                            addNewBusiness(null);
                            progressDialog.dismiss();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BusinessAddition.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                    addNewBusiness(null);
                    progressDialog.dismiss();
                }
            });
        }else
        {
            addNewBusiness(null);
            progressDialog.dismiss();
        }

    }

    private void addNewBusiness(String imageUrl) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String bussname = buisnessName.getText().toString();
        String priceofbuss = price.getText().toString();
        String phone = bussphonenumber.getText().toString();
        String address =bussaddress.getText().toString();
        Call<YesNoResponse> addbusscall = apiInterface.addBussDetails(bussname, monthOrDay, pay_mode, priceofbuss, currentUser.getUid(), imageUrl,phone,address);
        addbusscall.enqueue(new Callback<YesNoResponse>() {
            @Override
            public void onResponse(Call<YesNoResponse> call, Response<YesNoResponse> response) {
                Log.i("message", "Response successful " + response.body().getMessage());
                finish();
            }

            @Override
            public void onFailure(Call<YesNoResponse> call, Throwable t) {
                Toast.makeText(BusinessAddition.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    Uri picture, imageUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            imageUri = data.getData();
            startCrop(imageUri);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri uri = UCrop.getOutput(data);
            profilepic.setImageURI(uri);
        }
    }

    private void startCrop(@NonNull Uri uri) {
        String des = "pic" + System.currentTimeMillis() + ".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), des)));
        uCrop.withAspectRatio(1, 1);
        uCrop.withMaxResultSize(450, 450);
        uCrop.withOptions(getCropOptions());
        uCrop.start(BusinessAddition.this);
        picture = Uri.fromFile(new File(getCacheDir(), des));
    }

    private UCrop.Options getCropOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
        options.setCompressionQuality(50);
        options.setToolbarTitle("Crop Your Image");
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        return options;
    }
    private void allFieldsValid()
    {
        boolean error = false;
        namelayout.setError(null);
        pricelayout.setError(null);
        addresslayout.setError(null);
        phonelayout.setError(null);
        radioType1.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorBlack));
        radioType2.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorBlack));
        paymentType1.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorBlack));
        paymentType2.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorBlack));
        paymentType3.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorBlack));
        String name = buisnessName.getText().toString();
        String cost = price.getText().toString();
        String phone = bussphonenumber.getText().toString();
        String address = bussaddress.getText().toString();
        if(name.isEmpty()) {
            namelayout.setError("Enter a name");
            error=true;
        }
        if(cost.isEmpty()) {
            pricelayout.setError("Enter Price");
            error=true;
        }
        if(phone.isEmpty()) {
            phonelayout.setError("Enter phone number");
            error=true;
        }
        if(address.isEmpty()) {
            addresslayout.setError("Enter Address");
            error=true;
        }
        if(monthOrDay==null) {
            radioType1.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorDelete));
            radioType2.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorDelete));
            error=true;
        }
        if(pay_mode==null)
        {
            paymentType1.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorDelete));
            paymentType2.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorDelete));
            paymentType3.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorDelete));
            error=true;
        }
        if(!error)
        {
            CreateInstanseInDatabase();
        }
    }

}
