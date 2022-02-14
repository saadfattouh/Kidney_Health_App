package com.example.kidneyhealthapp.fragments.patient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.adapters.patient.DialysisCentersAdapter;
import com.example.kidneyhealthapp.model.Center;

import java.util.ArrayList;

public class DialysisCentersFragment extends Fragment {

    DialysisCentersAdapter mAdapter;
    ArrayList<Center> centers;
    RecyclerView mList;

    public DialysisCentersFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialysis_centers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}