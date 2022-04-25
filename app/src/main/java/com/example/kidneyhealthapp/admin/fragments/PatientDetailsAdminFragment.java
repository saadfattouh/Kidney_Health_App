package com.example.kidneyhealthapp.admin.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kidneyhealthapp.utils.Constants;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.model.Patient;

public class PatientDetailsAdminFragment extends Fragment {

    private static final String TAG = "patDetails";
    TextView mFullNameTV, mEmailTV, mPhoneTV, mAddressTV, mAgeTV, mGenderTV;
    Patient patient;
    Context context;

    ProgressDialog pDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public PatientDetailsAdminFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            patient = (Patient) getArguments().getSerializable("patient");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_details_admin, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFullNameTV = view.findViewById(R.id.tv_name);
        mEmailTV = view.findViewById(R.id.tv_email);
        mPhoneTV = view.findViewById(R.id.tv_phone);
        mAddressTV = view.findViewById(R.id.tv_details);
        mAgeTV = view.findViewById(R.id.tv_age);
        mGenderTV = view.findViewById(R.id.tv_gender);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);

        mFullNameTV.setText(patient.getFirstName() + " " + patient.getLastName());
        mEmailTV.setText(patient.getEmail());
        mPhoneTV.setText(patient.getPhone());
        mAddressTV.setText(patient.getAddress());
        mAgeTV.setText(String.valueOf(patient.getAge()));
        mGenderTV.setText(patient.getGender()== Constants.FEMALE?Constants.FEMALE_TXT:Constants.MALE_TXT);

    }

}