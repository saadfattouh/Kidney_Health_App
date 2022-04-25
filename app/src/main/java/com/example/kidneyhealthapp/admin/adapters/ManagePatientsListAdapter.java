package com.example.kidneyhealthapp.admin.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.kidneyhealthapp.utils.Constants;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.model.Patient;
import com.example.kidneyhealthapp.utils.SharedPrefManager;
import com.example.kidneyhealthapp.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ManagePatientsListAdapter extends RecyclerView.Adapter<ManagePatientsListAdapter.ViewHolder> {

    private static final String TAG = "manageP";
    Context context;
    ArrayList<Patient> list;
    ProgressDialog pDialog;

    NavController navController;

    public ManagePatientsListAdapter(Context context, ArrayList<Patient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_admin_patient, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");
        navController = Navigation.findNavController(parent);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Patient patient = list.get(position);


        holder.name.setText(patient.getFirstName() + " " + patient.getLastName());
        holder.age.setText(String.valueOf(patient.getAge()));
        String gender = "";
        if(patient.getGender() == Constants.FEMALE){
            gender = Constants.FEMALE_TXT;
        }else gender = Constants.MALE_TXT;
        holder.gender.setText(gender);


        holder.delete.setOnClickListener(v->{
            LayoutInflater factory = LayoutInflater.from(context);
            final View view = factory.inflate(R.layout.dialog_delete, null);
            final AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
            deleteDialog.setView(view);

            TextView ok = view.findViewById(R.id.ok);
            TextView cancel = view.findViewById(R.id.cancel);

            Log.e("id", String.valueOf(patient.getId()));

            ok.setOnClickListener(v1 ->{
                deletePatient(patient.getId(), position);
                deleteDialog.dismiss();
            });
            cancel.setOnClickListener(v1 ->
                    deleteDialog.dismiss()
            );
            deleteDialog.show();        });
        holder.itemView.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putSerializable("patient", patient);
            navController.navigate(R.id.action_patients_to_patientDetailsFragment, bundle);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, age, gender;
        public ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.patient_name);
            this.age = itemView.findViewById(R.id.age);
            this.gender = itemView.findViewById(R.id.gender);
            this.delete = itemView.findViewById(R.id.delete);

        }
    }

    private void deletePatient(int patientId, int position) {
        pDialog.show();
        String url = Urls.DELETE_PATIENT;
        String userId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());

        AndroidNetworking.post(url)
                .addBodyParameter("patient_id", String.valueOf(patientId))
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
                                Toast.makeText(context, context.getResources().getString(R.string.patient_deleted_success), Toast.LENGTH_SHORT).show();
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
