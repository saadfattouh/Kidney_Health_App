package com.example.kidneyhealthapp.patient.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.kidneyhealthapp.utils.Constants;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.model.Patient;
import com.example.kidneyhealthapp.utils.SharedPrefManager;

public class PatientProfileFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{


    Context context;
    TextView mFullNameTV, mEmailTV, mPhoneTV, mAddressTV, mAgeTV, mGenderTV;
    Button  mUpdateBtn;
    NavController navController;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public PatientProfileFragment() {}

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
        View view = inflater.inflate(R.layout.fragment_patient_profile, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            updateUi();
        });
        return  view;
    }

    private void updateUi() {
        Patient patient = SharedPrefManager.getInstance(context).getPatientData();

        mFullNameTV.setText(patient.getFirstName() + " " + patient.getLastName());
        mEmailTV.setText(patient.getEmail());
        mPhoneTV.setText(patient.getPhone());
        mAddressTV.setText(patient.getAddress());
        mAgeTV.setText(String.valueOf(patient.getAge()));
        mGenderTV.setText(patient.getGender()== Constants.FEMALE?Constants.FEMALE_TXT:Constants.MALE_TXT);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUpdateBtn = view.findViewById(R.id.update);

        mFullNameTV = view.findViewById(R.id.tv_name);
        mEmailTV = view.findViewById(R.id.tv_email);
        mPhoneTV = view.findViewById(R.id.tv_phone);
        mAddressTV = view.findViewById(R.id.tv_details);
        mAgeTV = view.findViewById(R.id.tv_age);
        mGenderTV = view.findViewById(R.id.tv_gender);


        mUpdateBtn.setOnClickListener(v -> {
            navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_Profile_to_UpdateProfileFragment);
        });
    }

    @Override
    public void onRefresh() {
        updateUi();
    }
}
