package com.example.kidneyhealthapp.doctor.adapters;

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

public class CenterPatientsAdapter extends RecyclerView.Adapter<CenterPatientsAdapter.ViewHolder> {

    Context context;
    ArrayList<Patient> list;
    ProgressDialog pDialog;

    NavController navController;

    public CenterPatientsAdapter(Context context, ArrayList<Patient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_patient, parent, false);
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

        holder.viewProfile.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putSerializable("patient", patient);
            navController.navigate(R.id.action_CenterPatients_to_PatientDetailsFragment, bundle);
        });
        holder.startChat.setOnClickListener(v->{
            startChat(patient.getId());
        });
        holder.viewInstructions.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putString("patient_id", String.valueOf(patient.getId()));
            navController.navigate(R.id.action_CenterPatients_to_InstructionsFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, age, gender;
        public Button viewProfile, viewInstructions;
        public ImageView startChat;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.patient_name);
            this.age = itemView.findViewById(R.id.age);
            this.gender = itemView.findViewById(R.id.gender);
            this.startChat = itemView.findViewById(R.id.start_chat);
            this.viewProfile = itemView.findViewById(R.id.view_profile);
            this.viewInstructions = itemView.findViewById(R.id.view_instructions);

        }
    }

    private void startChat(int patientId) {
        pDialog.show();
        String url = Urls.START_CHAT;
        String userId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());

        AndroidNetworking.post(url)
                .addBodyParameter("patient_id", String.valueOf(patientId))
                .addBodyParameter("doctor_id", userId)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String successMessage = "Success";
                            String chatExist = "Done";
                            //if no error in response
                            if (message.toLowerCase().contains(successMessage.toLowerCase())) {
                                Toast.makeText(context, context.getResources().getString(R.string.start_chat_success), Toast.LENGTH_SHORT).show();
                            }else if(message.toLowerCase().contains(chatExist.toLowerCase())) {
                                Toast.makeText(context, context.getResources().getString(R.string.chat_exist), Toast.LENGTH_SHORT).show();

                            } else{
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
