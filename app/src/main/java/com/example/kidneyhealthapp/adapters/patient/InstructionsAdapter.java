package com.example.kidneyhealthapp.adapters.patient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.model.Instruction;

import java.util.ArrayList;
import java.util.List;

public class InstructionsAdapter extends RecyclerView.Adapter<InstructionsAdapter.ViewHolder> {

    Context context;
    private List<Instruction> instructions;
    public NavController navController;


    // RecyclerView recyclerView;
    public InstructionsAdapter(Context context, ArrayList<Instruction> instructions) {
        this.context = context;
        this.instructions = instructions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_instruction, parent, false);

        return new ViewHolder(listItem);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Instruction appointment = instructions.get(position);

        holder.instruction.setText(appointment.getInstruction());
        holder.date.setText(appointment.getDate());


    }




    @Override
    public int getItemCount() {
        return instructions.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView date,instruction;

        public ViewHolder(View itemView) {
            super(itemView);
            this.instruction = itemView.findViewById(R.id.instruction);
            this.date = itemView.findViewById(R.id.date);
        }
    }





}