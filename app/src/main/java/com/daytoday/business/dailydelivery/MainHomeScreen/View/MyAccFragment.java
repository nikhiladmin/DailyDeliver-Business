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

import com.daytoday.business.dailydelivery.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class MyAccFragment extends Fragment {
    FloatingActionButton floatingActionButton;
    MaterialTextView userName,currentID;
    TextInputEditText phoneNo,usernameEditText;
    FirebaseAuth firebaseAuth;
    CircleImageView profileImg;
    private int GALLERY = 1, CAMERA = 2;
    private FirebaseStorage storage;
    private StorageReference storageRef;
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
        profileImg =view.findViewById(R.id.profile_image);

        storage=FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMultiplePermissions();
                showPictureDialog();
            }
        });
        userName.setText(firebaseAuth.getCurrentUser().getDisplayName());
        usernameEditText.setText(firebaseAuth.getCurrentUser().getDisplayName().toUpperCase());
        currentID.setText("ID - " + firebaseAuth.getCurrentUser().getUid());
        phoneNo.setText(firebaseAuth.getCurrentUser().getPhoneNumber());


        setProfileImage();


        return view;
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
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



    private void  requestMultiplePermissions(){
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("UPLOADIMAGE", "onActivityResult: "+resultCode);
            return;
        }
        if (requestCode == GALLERY&& data!=null&&data.getData()!=null&& resultCode==RESULT_OK) {
            if (data != null) {
                Uri contentURI = data.getData();
                FirebaseUpload(contentURI);
            }

        } else if (requestCode == CAMERA) {
               Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                profileImg.setImageBitmap(thumbnail);
             FirebaseUpload(getImageUri(getApplicationContext(),thumbnail));
        }
    }

    private void FirebaseUpload(Uri ImageUri){
        if(ImageUri!=null){
            ProgressDialog progressDialog =new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Please Wait...");
            progressDialog.show();
            try {
                StorageReference reference = storageRef.child("BusinessUser/" + firebaseAuth.getCurrentUser().getUid().toString());
                reference.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                        Log.i("UPLOADIMAGE", "onFailure: "+e.getMessage());
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
            }catch (Exception e){
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

    private void  setProfileImage(){
        Uri imageUri=firebaseAuth.getCurrentUser().getPhotoUrl();
        if(imageUri!=null){
            Picasso.get()
                    .load(imageUri.toString())
                    .resize(500, 500)
                    .centerCrop()
                    .into(profileImg);
        }else{
            profileImg.setImageResource(R.drawable.ic_account);
        }
    }

}
