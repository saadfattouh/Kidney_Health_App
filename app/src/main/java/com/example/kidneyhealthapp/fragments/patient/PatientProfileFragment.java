package com.example.kidneyhealthapp.fragments.patient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.model.User;
import com.example.kidneyhealthapp.utils.SharedPrefManager;

public class PatientProfileFragment extends Fragment {

    Context context;

    Button mLogoutBtn;
    TextView mFullNameTV;
    TextView mUserNameTV;
    TextView mPhoneTV;
    TextView mAddressTV;

    public PatientProfileFragment() {
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLogoutBtn = view.findViewById(R.id.logout);

        mFullNameTV = view.findViewById(R.id.name_tv);
        mUserNameTV = view.findViewById(R.id.user_name_tv);
        mPhoneTV = view.findViewById(R.id.phone_tv);
        mAddressTV = view.findViewById(R.id.address_tv);
        User user = SharedPrefManager.getInstance(context).getUserData();

        mFullNameTV.setText(user.getFirstName() + " " +user.getLastName());
        mUserNameTV.setText(user.getUserName());
        mPhoneTV.setText(user.getPhone());
//        mAddressTV.setText(user.getLocation());

        mLogoutBtn.setOnClickListener(v -> {
            logOut();
        });


    }

    public void logOut(){
        SharedPrefManager.getInstance(context).logout();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }
}
