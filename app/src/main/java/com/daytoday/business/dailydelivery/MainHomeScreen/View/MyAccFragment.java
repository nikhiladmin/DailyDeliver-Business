package com.daytoday.business.dailydelivery.MainHomeScreen.View;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daytoday.business.dailydelivery.LargeImageView;
import com.daytoday.business.dailydelivery.MainHomeScreen.UI.BusinessAddition;
import com.daytoday.business.dailydelivery.Network.ApiInterface;
import com.daytoday.business.dailydelivery.Network.Client;
import com.daytoday.business.dailydelivery.Network.Response.YesNoResponse;
import com.daytoday.business.dailydelivery.R;

import com.daytoday.business.dailydelivery.Utilities.SaveOfflineManager;
import com.google.android.gms.tasks.OnCanceledListener;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.FacebookSdk.getCacheDir;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.yalantis.ucrop.UCrop;

import java.util.HashMap;
import java.util.Map;

public class MyAccFragment extends Fragment {
    FloatingActionButton floatingActionButton;
    MaterialTextView userName, currentID;
    TextInputEditText phoneNo, usernameEditText, buss_address;
    FirebaseAuth firebaseAuth;

    CircleImageView profileImg;
    private int GALLERY = 1, CAMERA = 2;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    MaterialButton button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myaccount, container, false);
        floatingActionButton = view.findViewById(R.id.myacc_fab);
        firebaseAuth = FirebaseAuth.getInstance();
        userName = view.findViewById(R.id.UsersName);
        usernameEditText = view.findViewById(R.id.myacc_name);
        currentID = view.findViewById(R.id.currId);
        phoneNo = view.findViewById(R.id.myacc_phone);

        profileImg = view.findViewById(R.id.profile_image);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        buss_address = view.findViewById(R.id.buss_acc_address);
        button = view.findViewById(R.id.myacc_button);
        phoneNo.setEnabled(false);
        buss_address.setText(SaveOfflineManager.getUserAddress(getContext()));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMultiplePermissions();
                showPictureDialog();
            }
        });
        userName.setText(SaveOfflineManager.getUserName(getContext()));
        usernameEditText.setText(SaveOfflineManager.getUserName(getContext()));
        currentID.setText("ID - " + SaveOfflineManager.getUserId(getContext()));
        phoneNo.setText(SaveOfflineManager.getUserPhoneNumber(getContext()));
        buss_address.setText(SaveOfflineManager.getUserAddress(getContext()));
        Uri imageUri=firebaseAuth.getCurrentUser().getPhotoUrl();
        if(imageUri!=null){
            Picasso.get()
                    .load(imageUri.toString())
                    .resize(500, 500)
                    .centerCrop()
                    .error(R.drawable.profile001)
                    .into(profileImg);
        }
        profileImg.setOnClickListener(v->{
            if(imageUri!=null)
                new LargeImageView(getApplicationContext(),v,imageUri.toString(),null);
            else
                new LargeImageView(getApplicationContext(),v,null,null);
        });
        button.setOnClickListener(v -> {
            MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getActivity());
            alertDialog.setMessage("This will be reflected in all the customers you are connected.");
            alertDialog.setTitle("You are about to modify your profile details");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("I Understand", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseUpload();
                    updateData();
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Changes will take sometime to reflect.", Snackbar.LENGTH_LONG).show();
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Profile Update Cancelled", Snackbar.LENGTH_SHORT).show();
                }
            }).show();
        });
        return view;
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }


    public void requestMultiplePermissions() {
        Dexter.withContext(getApplicationContext())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    Uri picture, imageUri;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("TAG","test");
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("UPLOADIMAGE", "onActivityResult: " + resultCode);
            return;
        }
        if (requestCode == GALLERY && resultCode == RESULT_OK) {
            imageUri = data.getData();
            startCrop(imageUri);
        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageUri = getImageUri(getApplicationContext(),thumbnail);
            startCrop(imageUri);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri uri = UCrop.getOutput(data);
            profileImg.setImageURI(uri);
        }
    }

    private void FirebaseUpload() {
        if (picture != null) {
            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Please Wait...");
            progressDialog.show();
            try {
                StorageReference reference = storageRef.child("BusinessUser/" + firebaseAuth.getCurrentUser().getUid().toString());
                reference.putFile(picture).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Log.i("UPLOADIMAGE", "onSuccess: ");
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(uri)
                                        .build();
                                firebaseAuth.getCurrentUser().updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("UPLOADIMAGE", "User profile updated.");
                                                    setProfileImage();
                                                }
                                            }
                                        });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(getView(), "Uploading Failed", Snackbar.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        Log.i("UPLOADIMAGE", "onFailure: " + e.getMessage());
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        progressDialog.dismiss();
                        Log.i("UPLOADIMAGE", "onCanceled: ");
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i("UPLOADIMAGE", "onProgress: ");
                        double Progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploading " + (int) Progress + "%");
                    }
                }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onPaused(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void setProfileImage() {
        Uri imageUri = firebaseAuth.getCurrentUser().getPhotoUrl();
        if (imageUri != null) {
            Picasso.get()
                    .load(imageUri.toString())
                    .resize(500, 500)
                    .centerCrop()
                    .into(profileImg);
        } else {
            profileImg.setImageResource(R.drawable.ic_account);
        }
    }

    private void startCrop(@NonNull Uri uri) {
        String des = "pic" + System.currentTimeMillis() + ".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), des)));
        uCrop.withAspectRatio(1, 1);
        uCrop.withMaxResultSize(450, 450);
        uCrop.withOptions(getCropOptions());
        uCrop.start(getActivity(),this);
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

    public void getAddress() {

    }

    public void updateData() {
        String name = usernameEditText.getText().toString();
        String address = buss_address.getText().toString();
        ApiInterface apiInterface = Client.getClient().create(ApiInterface.class);
        Call<YesNoResponse> call = apiInterface.updateBussUserDetails(firebaseAuth.getUid(),name,null,address);
        call.enqueue(new Callback<YesNoResponse>() {
            @Override
            public void onResponse(Call<YesNoResponse> call, Response<YesNoResponse> response) {
                Log.e("TAG","updated");
                SaveOfflineManager.setUserAddress(getApplicationContext(),address);
                SaveOfflineManager.setUserName(getApplicationContext(),name);
            }

            @Override
            public void onFailure(Call<YesNoResponse> call, Throwable t) {

            }
        });
    }

}
