package com.daytoday.business.dailydelivery.MainHomeScreen.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.daytoday.business.dailydelivery.LoginActivity.AdditionalInfo;
import com.daytoday.business.dailydelivery.LoginActivity.LoginPage;
import com.daytoday.business.dailydelivery.MainHomeScreen.UI.QrCodeActivity;
import com.daytoday.business.dailydelivery.R;
import com.daytoday.business.dailydelivery.Utilities.SaveOfflineManager;
import com.facebook.login.LoginManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView imageView;
    MaterialTextView userName;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    CircleImageView profileImg;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mAuth = FirebaseAuth.getInstance();
        imageView= findViewById(R.id.bell_icon_click);
        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayShowTitleEnabled(false); //disabling title name in this acitivty to show logo here
        //-------------------- initialization of all the elements starts here------------------------------------

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navbar_home);

        //Log.i("msg",userName.getText().toString());
        //-----------------------initialization ends here-------------------------------------------------------
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_draw_open, R.string.nav_draw_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //------------------changing colour of status bar through java startsss----------------------- //
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        //-----changing color of status bar ends here ------------------------------------------------------//

        //-------------------addition of name of user--------------------------------------------//
        navigationView.setNavigationItemSelectedListener(this);
        Log.i("msg", navigationView.getHeaderCount() + " ");
        View view = navigationView.getHeaderView(0);
        userName= view.findViewById(R.id.userName);
        profileImg =view.findViewById(R.id.profile_image);
        Log.i("CURRENTUSER", "onCreate: "+mAuth.getCurrentUser().getDisplayName());
        userName.setText(mAuth.getCurrentUser().getDisplayName());
        Uri imageUri=mAuth.getCurrentUser().getPhotoUrl();
        if(imageUri!=null){
            Picasso.get()
                    .load(imageUri.toString())
                    .resize(500, 500)
                    .centerCrop()
                    .error(R.drawable.profile001)
                    .into(profileImg);
        }else{
            profileImg.setImageResource(R.drawable.profile001);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        //------------------- Bell Notification clicking is handled here------------------------------------------------//

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, QrCodeActivity.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update com.daytoday.business.dailydelivery.MainHomeScreen.UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.i("AUTHENT_US", "onStart: Home"+SaveOfflineManager.getUserAddress(HomeScreen.this));

            if(SaveOfflineManager.getUserAddress(HomeScreen.this)==null||SaveOfflineManager.getUserAddress(HomeScreen.this).length()==0){
                Intent loginIntent = new Intent(HomeScreen.this, AdditionalInfo.class);
                if(currentUser.getPhoneNumber()!=null&&currentUser.getPhoneNumber().length()!=0){
                    loginIntent.putExtra("isPhoneAuth",true);
                }else if(currentUser.getEmail()!=null&&currentUser.getEmail().length()!=0){
                    loginIntent.putExtra("isPhoneAuth",false);
                }
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
                finish();
            }
        }
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    //---------------------------This method for navigation between fragments----------------------------//
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers(); //Developers kindly check here the drawer is not closing without this method
        switch (item.getItemId()) {
            case R.id.nav_earning:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, new EarningFragment()).commit();
                break;
            case R.id.nav_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, new MyAccFragment()).commit();
                break;
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, new HomeFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, new SettingFragment()).commit();
                break;
            case R.id.nav_signout:
                MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(this);
                alertDialog.setMessage("Do you want to logout?");
                alertDialog.setTitle("Confirm");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginManager.getInstance().logOut();
                        FirebaseAuth.getInstance().signOut();
                        SaveOfflineManager.clearSharedPreferences(HomeScreen.this);
                        finish();
                        startActivity(new Intent(HomeScreen.this,LoginPage.class));
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                break;
            case R.id.nav_recentpayment:
                Toast.makeText(this, "Recent Payment fragment", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_paymentadjust:
                Toast.makeText(this, "Payment adjust fragment", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_empsalary:
                Toast.makeText(this,"Empsalary Fragment",Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
}