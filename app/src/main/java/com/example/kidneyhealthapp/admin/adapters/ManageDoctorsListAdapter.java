package com.example.kidneyhealthapp.admin.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.model.Doctor;
import com.example.kidneyhealthapp.utils.SharedPrefManager;
import com.example.kidneyhealthapp.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ManageDoctorsListAdapter extends RecyclerView.Adapter<ManageDoctorsListAdapter.ViewHolder> {

    private static final String TAG = "manageD";
    Context context;
    ArrayList<Doctor> list;
    ProgressDialog pDialog;

    NavController navController;

    public ManageDoctorsListAdapter(Context context, ArrayList<Doctor> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_admin_doctor, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        navController = Navigation.findNavController(parent);
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Doctor doctor = list.get(position);

        holder.name.setText(doctor.getFirstName() + " " + doctor.getLastName());
        holder.phone.setText(doctor.getPhone());
        holder.delete.setOnClickListener(v->{

            LayoutInflater factory = LayoutInflater.from(context);
            final View view = factory.inflate(R.layout.dialog_delete, null);
            final AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
            deleteDialog.setView(view);

            TextView ok = view.findViewById(R.id.ok);
            TextView cancel = view.findViewById(R.id.cancel);

            Log.e("id", String.valueOf(doctor.getId()));

            ok.setOnClickListener(v1 ->{
                deleteDoctor(doctor.getId(), position);
                deleteDialog.dismiss();
            });
            cancel.setOnClickListener(v1 ->
                    deleteDialog.dismiss()
            );
            deleteDialog.show();
        });
        holder.itemView.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putSerializable("doctor", doctor);
            navController.navigate(R.id.action_doctors_to_doctorDetailsFragment,bundle);
        });

        holder.linkToCenter.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putString("doctor_id",String.valueOf(doctor.getId()));
            navController.navigate(R.id.action_doctors_to_linkDoctorToCenterFragment, bundle);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, phone;
        public ImageView delete;
        public Button linkToCenter;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.person_name);
            this.phone = itemView.findViewById(R.id.phone);
            this.delete = itemView.findViewById(R.id.delete);
            this.linkToCenter = itemView.findViewById(R.id.link_to_center);

        }
    }

    private void deleteDoctor(int doctorId, int position) {
        pDialog.show();
        String url = Urls.DELETE_DOCTOR;
        String userId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());

        AndroidNetworking.post(url)
                .addBodyParameter("doctor_id", String.valueOf(doctorId))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String successMessage = "Deleted";
                            //if no error in response
                            if (message.toLowerCase().contains(successMessage.toLowerCase())) {
                                Toast.makeText(context, context.getResources().getString(R.string.doctor_deleted_success), Toast.LENGTH_SHORT).show();
                                notifyItemRemoved(position);
                                list.remove(position);
                            } else{
                                Toast.makeText(context, context.getResources().getString(R.string.operation_error), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            Log.e(TAG+"c", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        Log.e(TAG + "e", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("patient_id")) {
                                Toast.makeText(context, data.getJSONArray("patient_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("doctor_id")) {
                                Toast.makeText(context, data.getJSONArray("center_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
