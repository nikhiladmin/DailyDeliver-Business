package com.daytoday.business.dailydelivery.MainHomeScreen.UI;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import com.daytoday.business.dailydelivery.LargeImageView;
import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Bussiness;
import com.daytoday.business.dailydelivery.Network.ApiInterface;
import com.daytoday.business.dailydelivery.Network.Client;
import com.daytoday.business.dailydelivery.Network.Response.YesNoResponse;
import com.daytoday.business.dailydelivery.R;
import com.daytoday.business.dailydelivery.Utilities.AppConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BusinessDetailActivity extends AppCompatActivity {
    RadioGroup rg1, rg2;
    TextInputEditText buss_name, buss_price, buss_address, buss_phone;
    Button button;
    Bussiness bussiness;
    String paymode, mord;
    CircleImageView businessImage;
    FloatingActionButton imageEditButton;
    public static final String BUSINESS_OBJECT = "business-object";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buisness_detail);
        bussiness = getIntent().getParcelableExtra(BUSINESS_OBJECT);
        getSupportActionBar().setTitle(bussiness.getName() + " - Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        Log.e("TAG", "onCreate: " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        rg1 = findViewById(R.id.radioGroup1);
        rg2 = findViewById(R.id.radioGroup2);
        buss_name = findViewById(R.id.buss_name);
        buss_price = findViewById(R.id.buss_price);
        buss_address = findViewById(R.id.buss_address);
        button = findViewById(R.id.submitbutton);
        businessImage = findViewById(R.id.business_profile_pic);
        imageEditButton = findViewById(R.id.profilepiceditbtn);
        buss_name.setText(bussiness.getName());
        buss_phone = findViewById(R.id.buss_phone);
        buss_price.setText("" + bussiness.getPrice());
        if (bussiness.getImgurl() != null) {
            Picasso.get()
                    .load(bussiness.getImgurl())
                    .resize(5000, 5000)
                    .centerCrop().into(businessImage);
        }
        Log.e("TAG", "onCreate: " + bussiness.getdOrM());
        buss_address.setText(bussiness.getAddress());
        buss_phone.setText(bussiness.getPhoneno());
        if (bussiness.getdOrM().equals("D")) {
            rg1.check(R.id.radio_btn_daily);
            mord = "Daily";
        } else {
            rg1.check(R.id.radio_btn_monthly);
            mord = "Monthly";
        }

        if (bussiness.getPayment().equals("Cash")) {
            rg2.check(R.id.radio_btn_cash);
            paymode = "Cash";
        } else if (bussiness.getPayment().equals("Online")) {
            rg2.check(R.id.radio_btn_online);
            paymode = "Online";
        } else {
            paymode = "Both";
            rg2.check(R.id.radio_btn_both);
        }
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(BusinessDetailActivity.this);
                alertDialog.setMessage("You are about to modify to your business details");
                alertDialog.setTitle("Please Confirm");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadImage();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Snackbar.make(v, "Data Update Cancelled", Snackbar.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });
        imageEditButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT).setType("image/*");
            startActivityForResult(intent, 1);
        });
        businessImage.setOnClickListener(v -> {
            if (picture != null) {
                new LargeImageView(getApplication(), v, null, picture);
            } else if (bussiness.getImgurl() == null) {
                new LargeImageView(getApplication(), v, null, null);
            } else if (bussiness.getImgurl() != null) {
                new LargeImageView(getApplication(), v, bussiness.getImgurl(), null);
            }
        });
        setFalse();
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
                buss_phone.setEnabled(true);
                imageEditButton.setClickable(true);
                for (int i = 0; i < rg1.getChildCount(); i++)
                    rg1.getChildAt(i).setEnabled(true);
                for (int i = 0; i < rg2.getChildCount(); i++)
                    rg2.getChildAt(i).setEnabled(true);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void updateData(String imageurl) {
        String name = buss_name.getText().toString().trim();
        String adress = buss_address.getText().toString().trim();
        String phone = buss_phone.getText().toString();
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
        String price = buss_price.getText().toString().trim();
        ApiInterface apiInterface = Client.getClient().create(ApiInterface.class);
        Call<YesNoResponse> updateBussDetailsCall = apiInterface.updateBussDetails(bussiness.getBussid(), name, phone, adress, price, paymode, imageurl);
        updateBussDetailsCall.enqueue(new Callback<YesNoResponse>() {
            @Override
            public void onResponse(Call<YesNoResponse> call, Response<YesNoResponse> response) {
                if (!response.body().getError()) {
                    Toast.makeText(BusinessDetailActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<YesNoResponse> call, Throwable t) {
                Log.i(AppConstants.ERROR_LOG, "Some Error Occurred in BusinessDetailActivity Error is : { " + t.getMessage() + " }");
            }
        });
        setFalse();
    }

    public void setFalse() {
        button.setEnabled(false);
        buss_name.setEnabled(false);
        buss_price.setEnabled(false);
        buss_address.setEnabled(false);
        buss_phone.setEnabled(false);
        imageEditButton.setClickable(false);
        for (int i = 0; i < rg1.getChildCount(); i++)
            rg1.getChildAt(i).setEnabled(false);
        for (int i = 0; i < rg2.getChildCount(); i++)
            rg2.getChildAt(i).setEnabled(false);
    }

    Uri picture, imageUri;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            imageUri = data.getData();
            startCrop(imageUri);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri uri = UCrop.getOutput(data);
            businessImage.setImageURI(uri);
        }
    }

    private void startCrop(@NonNull Uri uri) {
        String des = "pic" + System.currentTimeMillis() + ".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), des)));
        uCrop.withAspectRatio(1, 1);
        uCrop.withMaxResultSize(450, 450);
        uCrop.withOptions(getCropOptions());
        uCrop.start(BusinessDetailActivity.this);
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

    String uploadedPicName;

    private void uploadImage() {
        ProgressDialog progressDialog = new ProgressDialog(BusinessDetailActivity.this);
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
                            updateData(uri.toString());
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BusinessDetailActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                            updateData(null);
                            progressDialog.dismiss();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BusinessDetailActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                    updateData(null);
                    progressDialog.dismiss();
                }
            });
        } else {
            updateData(null);
            progressDialog.dismiss();
        }

    }
}
