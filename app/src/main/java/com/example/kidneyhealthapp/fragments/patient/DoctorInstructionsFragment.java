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
import android.widget.Toast;

import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.adapters.patient.InstructionsAdapter;
import com.example.kidneyhealthapp.model.Instruction;

import java.util.ArrayList;


public class DoctorInstructionsFragment extends Fragment {


    private String doctorId;

    InstructionsAdapter mAdapter;
    RecyclerView mList;
    ArrayList<Instruction> instructions;

    Context context;

    public DoctorInstructionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            doctorId = getArguments().getString("doctor_id");
            Toast.makeText(getContext(), doctorId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_instructions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList = view.findViewById(R.id.rv);

        instructions = new ArrayList<Instruction>()
        {{
            add(new Instruction(1, "20/10/2021", "you must take your medicines on time!"));
            add(new Instruction(1, "20/10/2021", "you must take your medicines on time!"));
            add(new Instruction(1, "20/10/2021", "you must take your medicines on time!"));
            add(new Instruction(1, "20/10/2021", "you must take your medicines on time!"));
            add(new Instruction(1, "20/10/2021", "you must take your medicines on time!"));
            add(new Instruction(1, "20/10/2021", "you must take your medicines on time!"));
            add(new Instruction(1, "20/10/2021", "you must take your medicines on time!"));
            add(new Instruction(1, "20/10/2021", "you must take your medicines on time!"));
            add(new Instruction(1, "20/10/2021", "you must take your medicines on time!"));
            add(new Instruction(1, "20/10/2021", "you must take your medicines on time!"));

        }};


        mAdapter = new InstructionsAdapter(context, instructions);
        mList.setAdapter(mAdapter);

    }
}