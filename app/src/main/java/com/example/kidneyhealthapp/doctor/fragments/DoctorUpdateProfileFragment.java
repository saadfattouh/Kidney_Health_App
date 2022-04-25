package com.example.kidneyhealthapp.doctor.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.model.Doctor;
import com.example.kidneyhealthapp.utils.SharedPrefManager;
import com.example.kidneyhealthapp.utils.Urls;
import com.example.kidneyhealthapp.utils.Validation;
import org.json.JSONException;
import org.json.JSONObject;

public class DoctorUpdateProfileFragment extends Fragment {

    Context context;
    EditText mFirstNameET, mLastNameET, mDetailsET, mPhoneET;
    Button mUpdateBtn;
    ProgressDialog pDialog;
    NavController navController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public DoctorUpdateProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_update_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Doctor doctor = SharedPrefManager.getInstance(context).getDoctorData();

        mFirstNameET = view.findViewById(R.id.tv_first_name);
        mLastNameET = view.findViewById(R.id.tv_last_name);
        mPhoneET = view.findViewById(R.id.tv_phone);
        mDetailsET = view.findViewById(R.id.tv_details);

        mUpdateBtn = view.findViewById(R.id.update);

        mFirstNameET.setText(doctor.getFirstName());
        mLastNameET.setText(doctor.getLastName());
        mPhoneET.setText(doctor.getPhone());
        mDetailsET.setText(doctor.getDetails());

        navController = Navigation.findNavController(view);

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);

        mUpdateBtn.setOnClickListener(v->{
            if(Validation.validateInput(context, mFirstNameET, mLastNameET, mDetailsET, mPhoneET)){
                update();
            }
        });

    }

    private void update() {

        pDialog.show();
        mUpdateBtn.setEnabled(false);

        String url = Urls.UPDATE_DOCTOR_ACC;
        String firstName = mFirstNameET.getText().toString().trim();
        String lastName = mLastNameET.getText().toString().trim();
        String details = mDetailsET.getText().toString().trim();
        String phone = mPhoneET.getText().toString().trim();
        String id = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        AndroidNetworking.post(url)
                .addBodyParameter("id", id)
                .addBodyParameter("first_name", firstName)
                .addBodyParameter("last_name", lastName)
                .addBodyParameter("phone", phone)
                .addBodyParameter("details", details)
                .addBodyParameter("address", "address")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String userFounded = "User Saved";
                            //if no error in response
                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("data");
                                SharedPrefManager.getInstance(context).patientUpdate(
                                        userJson.getString("first_name"),
                                        userJson.getString("last_name"),
                                        userJson.getString("phone"),
                                        userJson.getString("address")
                                );
                                pDialog.dismiss();
                                mUpdateBtn.setEnabled(true);
                                navController.popBackStack();

                            }

                        } catch (JSONException e) {
                            pDialog.dismiss();
                            mUpdateBtn.setEnabled(true);
                            e.printStackTrace();
                            Log.e("updatepatient catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mUpdateBtn.setEnabled(true);

                        Log.e("updatepatienterror", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("first_name")) {
                                Toast.makeText(context, data.getJSONArray("first_name").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("last_name")) {
                                Toast.makeText(context, data.getJSONArray("last_name").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("phone")) {
                                Toast.makeText(context, data.getJSONArray("phone").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("address")) {
                                Toast.makeText(context, data.getJSONArray("address").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        

    }
}