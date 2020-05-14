package com.daytoday.business.dailydelivery.MainHomeScreen;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.daytoday.business.dailydelivery.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class BusinessAddition extends AppCompatActivity {

    Toolbar toolbar;
    RadioGroup rg1, rg2;
    TextInputLayout textInputLayout;

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

        rg1 = findViewById(R.id.radioGroup1);
        rg1.clearCheck();

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
                    } else {
                        textInputLayout.setSuffixText("/Day");
                    }
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
                Toast.makeText(this, "To be Added soon", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
