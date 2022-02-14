package com.example.kidneyhealthapp.adapters.doctor;


import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kidneyhealthapp.Constants;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.api.Urls;
import com.example.kidneyhealthapp.model.Appointment;
import com.example.kidneyhealthapp.utils.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppointmentRequestsAdapter extends RecyclerView.Adapter<AppointmentRequestsAdapter.ViewHolder> {

    Context context;
    private List<Appointment> appointments;
    public NavController navController;


    // RecyclerView recyclerView;
    public AppointmentRequestsAdapter(Context context, ArrayList<Appointment> appointments) {
        this.context = context;
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public AppointmentRequestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_appointment_request, parent, false);
        AppointmentRequestsAdapter.ViewHolder viewHolder = new AppointmentRequestsAdapter.ViewHolder(listItem);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull AppointmentRequestsAdapter.ViewHolder holder, int position) {

        Appointment appointment = appointments.get(position);

        holder.centerNameTV.setText(appointment.getCenterName());
        holder.doctorNameTV.setText(appointment.getDoctorName());
        holder.dateTV.setText(appointment.getDate());
        if (appointment.getStatus() == 1) {
            holder.statusTV.setText(Constants.APPOINTMENT_APPROVED);
            holder.statusTV.setTextColor(0xff00ff00);
        } else if (appointment.getStatus() == 0) {
            holder.statusTV.setText(Constants.APPOINTMENT_REJECTED);
            holder.statusTV.setTextColor(0xffff0000);

        } else {
            holder.statusTV.setText(Constants.APPOINTMENT_PROCESSING);
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

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    centerInfoDialog.dismiss();
                }
            });

            centerInfoDialog.show();
        });

    }


    @Override
    public int getItemCount() {
        return appointments.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView centerNameTV, doctorNameTV, dateTV, statusTV;

        public ViewHolder(View itemView) {
            super(itemView);
            this.centerNameTV = itemView.findViewById(R.id.center_name);
            this.doctorNameTV = itemView.findViewById(R.id.doctor_name);
            this.dateTV = itemView.findViewById(R.id.date);
            this.statusTV = itemView.findViewById(R.id.status);
        }
    }


}

