package com.example.kidneyhealthapp.fragments.patient;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.adapters.patient.AppointmentsAdapter;
import com.example.kidneyhealthapp.model.Appointment;

import java.util.ArrayList;

public class MyAppointmentsFragment extends Fragment {

    Context ctx;

    ArrayList<Appointment> appointments;
    RecyclerView mList;
    AppointmentsAdapter mAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
    }
    public MyAppointmentsFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_appointments, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = view.findViewById(R.id.rv);

        appointments = new ArrayList<Appointment>(){{
            add(new Appointment(1,"doctor","center",1,"10-1-",1,"hhh", "patient status"));
            add(new Appointment(1,"doctor","center",1,"10-1-",0,"hhh", "patient status"));
            add(new Appointment(1,"doctor","center",1,"10-1-",-1,"hhh", "patient status"));
            add(new Appointment(1,"doctor","center",1,"10-1-",-1,"hhh", "patient status"));
            add(new Appointment(1,"doctor","center",1,"10-1-",-1,"hhh", "patient status"));

        }};

        mAdapter = new AppointmentsAdapter(ctx, appointments);
        mList.setAdapter(mAdapter);
    }
}