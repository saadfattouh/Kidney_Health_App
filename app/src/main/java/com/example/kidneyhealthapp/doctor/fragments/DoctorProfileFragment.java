package com.example.kidneyhealthapp.doctor.fragments;

import android.app.ProgressDialog;
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

import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.model.Doctor;
import com.example.kidneyhealthapp.utils.SharedPrefManager;

public class DoctorProfileFragment extends Fragment {

    Context context;
    TextView nameTV, emailTV, phoneTV, detailsTV;
    Button updateBtn;
    NavController navController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public DoctorProfileFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameTV = view.findViewById(R.id.tv_name);
        emailTV = view.findViewById(R.id.tv_email);
        phoneTV = view.findViewById(R.id.tv_phone);
        detailsTV = view.findViewById(R.id.tv_details);
        updateBtn = view.findViewById(R.id.update);

        Doctor doctor = SharedPrefManager.getInstance(context).getDoctorData();
        nameTV.setText(doctor.getFirstName() + "  " + doctor.getLastName());
        emailTV.setText(doctor.getEmail());
        phoneTV.setText(doctor.getPhone());
        detailsTV.setText(doctor.getDetails());

        updateBtn.setOnClickListener(v->{
            navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_Profile_to_UpdateProfileFragment);
        });
    }
}
