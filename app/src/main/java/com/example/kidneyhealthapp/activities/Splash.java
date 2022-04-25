package com.example.kidneyhealthapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kidneyhealthapp.utils.Constants;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.utils.SharedPrefManager;

public class Splash extends AppCompatActivity {

    public static final int TIME_TO_START = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                nextPhase();
            }
        }, TIME_TO_START);

    }

    private void nextPhase() {
        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            if(SharedPrefManager.getInstance(this).getUserType() == Constants.USER_TYPE_PATIENT){
                startActivity(new Intent(this, PatientMain.class));
            }else if (SharedPrefManager.getInstance(this).getUserType() == Constants.USER_TYPE_DOCTOR){
                startActivity(new Intent(this, DoctorMain.class));
            }else{
                startActivity(new Intent(this, AdminMain.class));
            }
        }else{
            startActivity(new Intent(this, Login.class));
        }
        finish();
    }
}