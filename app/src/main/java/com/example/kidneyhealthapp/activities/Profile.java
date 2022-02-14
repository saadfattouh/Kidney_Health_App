package com.example.kidneyhealthapp.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.model.Doctor;
import com.example.kidneyhealthapp.model.User;
import com.example.kidneyhealthapp.utils.DoctorPrefs;
import com.example.kidneyhealthapp.utils.SharedPrefManager;

public class Profile extends AppCompatActivity {

    Button mLogoutBtn;

    TextView mFullNameTV;
    TextView mUserNameTV;
    TextView mPhoneTV;
    TextView mAddressTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        mLogoutBtn = findViewById(R.id.logout);

        mFullNameTV = findViewById(R.id.name_tv);
        mUserNameTV = findViewById(R.id.user_name_tv);
        mPhoneTV = findViewById(R.id.phone_tv);
        mAddressTV = findViewById(R.id.address_tv);

        Doctor doctor = DoctorPrefs.getInstance(this).getUserData();
        User user = SharedPrefManager.getInstance(this).getUserData();

        mFullNameTV.setText(user.getFirstName() + " " +user.getLastName());
        mUserNameTV.setText(user.getUserName());
        mPhoneTV.setText(user.getPhone());
//        mAddressTV.setText(user.getLocation());

        mLogoutBtn.setOnClickListener(v -> {
            logOut();
        });


    }

    public void logOut(){
        if(DoctorPrefs.getInstance(this) != null){
            DoctorPrefs.getInstance(this).logout();
        }else{
            SharedPrefManager.getInstance(this).logout();

        }


        PackageManager packageManager = this.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(this.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }
}