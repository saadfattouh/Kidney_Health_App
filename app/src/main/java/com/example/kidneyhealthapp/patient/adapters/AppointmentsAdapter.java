package com.example.kidneyhealthapp.patient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidneyhealthapp.utils.Constants;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.model.Appointment;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {

        Context context;
        private List<Appointment> appointments;
        public NavController navController;

        // RecyclerView recyclerView;
        public AppointmentsAdapter(Context context, ArrayList<Appointment> appointments) {
            this.context = context;
            this.appointments = appointments;
        }

        @NonNull
        @Override
        public AppointmentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.item_appointment, parent, false);
            AppointmentsAdapter.ViewHolder viewHolder = new AppointmentsAdapter.ViewHolder(listItem);

            return viewHolder;
        }



        @Override
        public void onBindViewHolder(@NonNull AppointmentsAdapter.ViewHolder holder, int position) {

            Appointment appointment = appointments.get(position);

            holder.centerNameTV.setText(appointment.getCenterName());
            holder.doctorNameTV.setText(appointment.getDoctorName());
            holder.dateTV.setText(appointment.getDate());
            if(appointment.getStatus() == Constants.APPOINTMENT_APPROVED){
                holder.statusTV.setText(Constants.APPOINTMENT_APPROVED_TXT);
                holder.statusTV.setTextColor(0xff00ff00);
            }else if(appointment.getStatus() == Constants.APPOINTMENT_REJECTED){
                holder.statusTV.setText(Constants.APPOINTMENT_REJECTED_TXT);
                holder.statusTV.setTextColor(0xffff0000);

            }else{
                holder.statusTV.setText(Constants.APPOINTMENT_PROCESSING_TXT);
                holder.statusTV.setTextColor(0xff0000ff);

            }

            holder.itemView.setOnClickListener(v -> {
                LayoutInflater factory = LayoutInflater.from(context);
                final View view = factory.inflate(R.layout.dialog_my_appointment_info, null);
                final AlertDialog centerInfoDialog = new AlertDialog.Builder(context).create();
                centerInfoDialog.setView(view);

                TextView ok = view.findViewById(R.id.ok);
                TextView patientStatus = view.findViewById(R.id.patient_status);
                TextView result = view.findViewById(R.id.result);

                patientStatus.setText(appointment.getPatientStatus());
                result.setText(appointment.getResultInfo());

                ok.setOnClickListener(v1 ->
                        centerInfoDialog.dismiss()
                );

                centerInfoDialog.show();
            });

        }




        @Override
        public int getItemCount() {
            return appointments.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView centerNameTV,doctorNameTV, dateTV, statusTV;

            public ViewHolder(View itemView) {
                super(itemView);
                this.centerNameTV = itemView.findViewById(R.id.center_name);
                this.doctorNameTV = itemView.findViewById(R.id.doctor_name);
                this.dateTV = itemView.findViewById(R.id.date);
                this.statusTV = itemView.findViewById(R.id.status);
            }
        }
    }

