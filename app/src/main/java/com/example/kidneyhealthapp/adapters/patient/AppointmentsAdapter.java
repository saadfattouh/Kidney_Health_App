package com.example.kidneyhealthapp.adapters.patient;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
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

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {

        Context context;
        private List<Appointment> appointments;
        public NavController navController;

        com.example.kidneyhealthapp.adapters.patient.AppointmentsAdapter.OnAppointmentSelected mSelectionListener;


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
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            if(context instanceof com.example.kidneyhealthapp.adapters.patient.AppointmentsAdapter.OnAppointmentSelected){
                mSelectionListener = (com.example.kidneyhealthapp.adapters.patient.AppointmentsAdapter.OnAppointmentSelected) context;
            }
        }

        @Override
        public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
            mSelectionListener = null;
            super.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onBindViewHolder(@NonNull AppointmentsAdapter.ViewHolder holder, int position) {

            Appointment appointment = appointments.get(position);

            holder.centerNameTV.setText(appointment.getCenterName());
            holder.doctorNameTV.setText(appointment.getDoctorName());
            holder.dateTV.setText(appointment.getDate());
            if(appointment.getStatus() == 1){
                holder.statusTV.setText(Constants.APPOINTMENT_APPROVED);
                holder.statusTV.setTextColor(0xff00ff00);
            }else if(appointment.getStatus() == 0){
                holder.statusTV.setText(Constants.APPOINTMENT_REJECTED);
                holder.statusTV.setTextColor(0xffff0000);

            }else{
                holder.statusTV.setText(Constants.APPOINTMENT_PROCESSING);
                holder.statusTV.setTextColor(0xff0000ff);

            }

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

        public interface OnAppointmentSelected{
            void onAppointmentSelected(String desc);
        }

        private void getMyAppointments() {

            int userId = SharedPrefManager.getInstance(context).getUserId();
            final ProgressDialog pDialog = new ProgressDialog(context);
            pDialog.setMessage("Processing Please wait...");
            pDialog.show();
            String url = Urls.GET_MY_APPOINTMENT;
            AndroidNetworking.post(url)
                    .addBodyParameter("user_id", String.valueOf(userId))
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response
                            pDialog.dismiss();
                            try {
                                //converting response to json object
                                JSONObject obj = response;
                                //if no error in response
                                if (obj.getInt("status") == 1) {
                                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                } else if(obj.getInt("status") == -1){
                                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
                            pDialog.dismiss();
                            Toast.makeText(context, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }

