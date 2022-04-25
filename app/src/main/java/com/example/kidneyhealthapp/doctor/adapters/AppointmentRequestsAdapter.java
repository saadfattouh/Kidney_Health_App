package com.example.kidneyhealthapp.doctor.adapters;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.example.kidneyhealthapp.utils.Constants;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.model.Appointment;
import com.example.kidneyhealthapp.utils.Urls;
import com.example.kidneyhealthapp.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppointmentRequestsAdapter extends RecyclerView.Adapter<AppointmentRequestsAdapter.ViewHolder> {

    Context context;
    private List<Appointment> list;
    public NavController navController;
    ProgressDialog pDialog;

    // RecyclerView recyclerView;
    public AppointmentRequestsAdapter(Context context, ArrayList<Appointment> list) {
        this.context = context;
        this.list = list;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Appointment appointment = list.get(position);

        if (appointment.getStatus() == Constants.APPOINTMENT_REJECTED) {
            holder.btns.setVisibility(View.GONE);
            holder.edit.setVisibility(View.GONE);
        } else if (appointment.getStatus() == Constants.APPOINTMENT_APPROVED) {
            holder.btns.setVisibility(View.GONE);
            holder.edit.setVisibility(View.VISIBLE);
        } else {
            holder.btns.setVisibility(View.VISIBLE);
            holder.edit.setVisibility(View.GONE);
        }
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");

        holder.patientNameTV.setText(appointment.getPatientName());
        holder.dateTV.setText(appointment.getDate());
        if (appointment.getStatus() == 2) {
            holder.statusTV.setText(Constants.APPOINTMENT_APPROVED_TXT);
            holder.statusTV.setTextColor(0xff00ff00);
        } else if (appointment.getStatus() == 0) {
            holder.statusTV.setText(Constants.APPOINTMENT_REJECTED_TXT);
            holder.statusTV.setTextColor(0xffff0000);

        } else {
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

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    centerInfoDialog.dismiss();
                }
            });

            centerInfoDialog.show();
        });

        holder.reject.setOnClickListener(v -> {
            LayoutInflater factory = LayoutInflater.from(context);
            final View view = factory.inflate(R.layout.dialog_reject_appointment, null);
            final AlertDialog changeStatusDialog = new AlertDialog.Builder(context).create();
            changeStatusDialog.setView(view);

            TextView ok = view.findViewById(R.id.yes_btn);
            TextView no = view.findViewById(R.id.no_btn);

            no.setOnClickListener(v12 ->
                    changeStatusDialog.dismiss()
            );
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeAppointmentStatus(appointment.getId(), position, Constants.APPOINTMENT_REJECTED);
                    changeStatusDialog.dismiss();
                }
            });

            changeStatusDialog.show();
        });
        holder.approve.setOnClickListener(v -> {
            LayoutInflater factory = LayoutInflater.from(context);
            final View view = factory.inflate(R.layout.dialog_approve_appointment, null);
            final AlertDialog changeStatusDialog = new AlertDialog.Builder(context).create();
            changeStatusDialog.setView(view);

            TextView ok = view.findViewById(R.id.yes_btn);
            TextView no = view.findViewById(R.id.no_btn);

            no.setOnClickListener(v12 ->
                    changeStatusDialog.dismiss()
            );
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeAppointmentStatus(appointment.getId(), position, Constants.APPOINTMENT_APPROVED);
                    changeStatusDialog.dismiss();
                }
            });

            changeStatusDialog.show();
        });
        holder.edit.setOnClickListener(v->{
            LayoutInflater factory = LayoutInflater.from(context);
            final View view = factory.inflate(R.layout.dialog_edit_appointment, null);
            final AlertDialog changeStatusDialog = new AlertDialog.Builder(context).create();
            changeStatusDialog.setView(view);

            TextView ok = view.findViewById(R.id.yes_btn);
            TextView no = view.findViewById(R.id.no_btn);
            EditText resultInfo = view.findViewById(R.id.result_info);
            EditText patientStatus = view.findViewById(R.id.patient_status);
            no.setOnClickListener(v12 ->
                    changeStatusDialog.dismiss()
            );
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Validation.validateInput(context, resultInfo, patientStatus)){
                        String result = resultInfo.getText().toString().trim();
                        String status = patientStatus.getText().toString().trim();
                        editAppointment(appointment.getId(), result, status, position);
                        changeStatusDialog.dismiss();

                    }
                }
            });

            changeStatusDialog.show();
        });

    }

    private void editAppointment(int appointmentId, String result, String pStatus, int position) {
        pDialog.show();
        String url = Urls.UPDATE_APPOINTMENT;

        AndroidNetworking.get(url)
                .addQueryParameter("appointment_id", String.valueOf(appointmentId))
                .addQueryParameter("resultInfo", result)
                .addQueryParameter("patientStatus", pStatus)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String userFounded = "Success";
                            //if no error in response
                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {
                                Toast.makeText(context, context.getResources().getString(R.string.appointment_edit_success), Toast.LENGTH_SHORT).show();
                                list.get(position).setPatientStatus(pStatus);
                                list.get(position).setResultInfo(result);
                                notifyItemChanged(position);
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.operation_error), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            Log.e("apply", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        Log.e("applyError", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("appointment_id")) {
                                Toast.makeText(context, data.getJSONArray("appointment_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("resultInfo")) {
                                Toast.makeText(context, data.getJSONArray("resultInfo").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("patientStatus")) {
                                Toast.makeText(context, data.getJSONArray("patientStatus").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private void changeAppointmentStatus(int appointmentId, int position, int status) {
        pDialog.show();
        String url = Urls.CHANGE_APPOINTMENT_STATUS;

        AndroidNetworking.get(url)
                .addQueryParameter("appointment_id", String.valueOf(appointmentId))
                .addQueryParameter("status", String.valueOf(status))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String userFounded = "Success";
                            //if no error in response
                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {
                                Toast.makeText(context, context.getResources().getString(R.string.appointment_status_success), Toast.LENGTH_SHORT).show();
                                list.get(position).setStatus(status);
                                notifyItemChanged(position);

                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.operation_error), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            Log.e("apply", e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        Log.e("applyError", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("appointment_id")) {
                                Toast.makeText(context, data.getJSONArray("appointment_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("status")) {
                                Toast.makeText(context, data.getJSONArray("status").toString(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView patientNameTV, dateTV, statusTV;
        public Button reject, approve, edit;
        public RelativeLayout btns;

        public ViewHolder(View itemView) {
            super(itemView);
            this.patientNameTV = itemView.findViewById(R.id.patient_name);
            this.dateTV = itemView.findViewById(R.id.date);
            this.statusTV = itemView.findViewById(R.id.status);
            this.reject = itemView.findViewById(R.id.reject);
            this.approve = itemView.findViewById(R.id.approve);
            this.btns = itemView.findViewById(R.id.btns);
            this.edit = itemView.findViewById(R.id.edit_appointment);
        }
    }


}

