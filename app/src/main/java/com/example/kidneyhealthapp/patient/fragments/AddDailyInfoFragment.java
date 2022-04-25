package com.example.kidneyhealthapp.patient.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.utils.SharedPrefManager;
import com.example.kidneyhealthapp.utils.Urls;
import com.example.kidneyhealthapp.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

public class AddDailyInfoFragment extends Fragment {


    //TODO : create list of daily info

    EditText mWaterET, mMedicineET;
    Button mInsertInfoBtn;
    ProgressDialog pDialog;

    NavController navController;
    Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public AddDailyInfoFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_daily_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWaterET = view.findViewById(R.id.water);
        mMedicineET = view.findViewById(R.id.medicine);
        mInsertInfoBtn = view.findViewById(R.id.insertInfo);

        navController = Navigation.findNavController(view);
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");

        mInsertInfoBtn.setOnClickListener(v->{
            if(Validation.validateInput(context, mWaterET, mMedicineET)){
                insertInfo();
            }
        });

    }

    private void insertInfo() {
        pDialog.show();
        String url = Urls.INSERT_INFO;
        String patientId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        String water = mWaterET.getText().toString().trim();
        String medicine = mMedicineET.getText().toString().trim();

        AndroidNetworking.post(url)
                .addBodyParameter("patient_id", patientId)
                .addBodyParameter("water", water)
                .addBodyParameter("medicine", medicine)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String successMessage = "User Saved";
                            //if no error in response
                            if (message.toLowerCase().contains(successMessage.toLowerCase())) {
                                Toast.makeText(context, context.getResources().getString(R.string.insert_info_success), Toast.LENGTH_SHORT).show();
                                navController.popBackStack();
                            }else{
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
                            if (data.has("water")) {
                                Toast.makeText(context, data.getJSONArray("water").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("medicine")) {
                                Toast.makeText(context, data.getJSONArray("medicine").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}