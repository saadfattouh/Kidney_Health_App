package com.example.kidneyhealthapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kidneyhealthapp.utils.Constants;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.utils.Urls;
import com.example.kidneyhealthapp.model.Doctor;
import com.example.kidneyhealthapp.model.Patient;
import com.example.kidneyhealthapp.utils.SharedPrefManager;
import com.example.kidneyhealthapp.utils.Validation;
import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    EditText mFirstNameET, mLastNameET, mEmailET, mPassET, mPhoneET, mAddressET, mDetailsET, mAgeET;

    String firstName, lastName, email, password, phone, address, details, age;

    Button mRegisterBtn, mToLoginBtn;

    RadioGroup mAccountTypeSelector, mGenderSelector;

    int selectedUserType = Constants.USER_TYPE_PATIENT;
    int selectedGender = Constants.FEMALE;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirstNameET = findViewById(R.id.first_name);
        mLastNameET = findViewById(R.id.last_name);
        mEmailET = findViewById(R.id.email);
        mPassET = findViewById(R.id.password);
        mPhoneET = findViewById(R.id.phone);
        mAddressET = findViewById(R.id.address);
        mAgeET = findViewById(R.id.age);
        mDetailsET = findViewById(R.id.details);
        mToLoginBtn = findViewById(R.id.btnLinkToLoginScreen);
        mRegisterBtn = findViewById(R.id.btnRegister);
        mDetailsET.setVisibility(View.GONE);

        mAccountTypeSelector = findViewById(R.id.type_selector);
        mGenderSelector = findViewById(R.id.gender_selector);
        mAccountTypeSelector.check(R.id.patient);
        selectedUserType = Constants.USER_TYPE_PATIENT;

        mGenderSelector.check(R.id.female);
        selectedGender = Constants.FEMALE;

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);

        mToLoginBtn.setOnClickListener(v -> {
          startActivity(new Intent(Register.this, Login.class));
          finish();
        });

        mAccountTypeSelector.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.doctor:
                    selectedUserType = Constants.USER_TYPE_DOCTOR;
                    updateUi();
                    break;
                case R.id.patient:
                    selectedUserType = Constants.USER_TYPE_PATIENT;
                    updateUi();
                    break;
            }
        });

        mGenderSelector.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.male:
                    selectedGender = Constants.MALE;
                    break;
                case R.id.female:
                    selectedGender = Constants.FEMALE;
                    break;
            }
        });

        mRegisterBtn.setOnClickListener(v -> {
            switch (selectedUserType){
                case Constants.USER_TYPE_PATIENT:
                    if(Validation.validateInput(Register.this, mFirstNameET, mEmailET, mPassET, mPhoneET, mAddressET, mAgeET)){
                        register_patient();
                    }
                    break;

                case Constants.USER_TYPE_DOCTOR:
                    if(Validation.validateInput(Register.this, mFirstNameET, mEmailET, mPassET, mPhoneET, mDetailsET)){
                        registerDoctor();
                    }
                    break;
            }
        });

    }

    private void updateUi() {
        if(selectedUserType == Constants.USER_TYPE_DOCTOR){
            mDetailsET.setVisibility(View.VISIBLE);
            mAddressET.setVisibility(View.GONE);
            mGenderSelector.setVisibility(View.GONE);
            mAgeET.setVisibility(View.GONE);
        }else {
            mDetailsET.setVisibility(View.GONE);
            mAddressET.setVisibility(View.VISIBLE);
            mGenderSelector.setVisibility(View.VISIBLE);
            mAgeET.setVisibility(View.VISIBLE);
        }
    }

    private void registerDoctor() {
        pDialog.show();
        mRegisterBtn.setEnabled(false);

        //first getting the values
        firstName = mFirstNameET.getText().toString().trim();
        lastName = mLastNameET.getText().toString().trim();
        email = mEmailET.getText().toString().trim();
        password = mPassET.getText().toString().trim();
        phone = mPhoneET.getText().toString().trim();
        age = mAgeET.getText().toString().trim();
        address = mAddressET.getText().toString().trim();
        details = mDetailsET.getText().toString().trim();

        String url = Urls.REGISTER_DOCTOR;

        AndroidNetworking.post(url)
                .addBodyParameter("first_name", firstName)
                .addBodyParameter("last_name", lastName)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .addBodyParameter("phone", phone)
                .addBodyParameter("details", details)
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
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("data");
                                int userType = userJson.getInt("type");

                                Log.e("uType", userType+"");
                                SharedPrefManager.getInstance(getApplicationContext()).doctorLogin(new Doctor(
                                        Integer.parseInt(userJson.getString("id")),
                                        userJson.getString("first_name"),
                                        userJson.getString("last_name"),
                                        userJson.getString("email"),
                                        userJson.getString("phone"),
                                        userJson.getString("details")
                                ));
                                startActivity(new Intent(Register.this, DoctorMain.class));
                                finish();
                            }else{
                                Toast.makeText(Register.this, getResources().getString(R.string.wrong_pass), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                            mRegisterBtn.setEnabled(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("regdoctor catch", e.getMessage());
                            pDialog.dismiss();
                            mRegisterBtn.setEnabled(true);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mRegisterBtn.setEnabled(true);
                        Log.e("regdoctorerror", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(Register.this, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("first_name")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("first_name").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("last_name")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("last_name").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("phone")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("phone").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("email")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("email").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("password")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("password").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("details")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("details").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void register_patient() {
        pDialog.show();
        mRegisterBtn.setEnabled(false);

        //first getting the values
        firstName = mFirstNameET.getText().toString().trim();
        lastName = mLastNameET.getText().toString().trim();
        email = mEmailET.getText().toString().trim();
        password = mPassET.getText().toString().trim();
        phone = mPhoneET.getText().toString().trim();
        age = mAgeET.getText().toString().trim();
        address = mAddressET.getText().toString().trim();

        String url = Urls.REGISTER_PATIENT;

        AndroidNetworking.post(url)
                .addBodyParameter("first_name", firstName)
                .addBodyParameter("last_name", lastName)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .addBodyParameter("phone", phone)
                .addBodyParameter("address", address)
                .addBodyParameter("age", age)
                .addBodyParameter("gender", String.valueOf(selectedGender))
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
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("data");
                                SharedPrefManager.getInstance(getApplicationContext()).patientLogin(new Patient(
                                        Integer.parseInt(userJson.getString("id")),
                                        userJson.getString("first_name"),
                                        userJson.getString("last_name"),
                                        userJson.getString("email"),
                                        userJson.getString("phone"),
                                        userJson.getString("address"),
                                        userJson.getInt("age"),
                                        userJson.getInt("gender")
                                ));
                                startActivity(new Intent(Register.this, PatientMain.class));
                                finish();
                            }else{
                                Toast.makeText(Register.this, getResources().getString(R.string.wrong_pass), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                            mRegisterBtn.setEnabled(true);
                        } catch (JSONException e) {
                            pDialog.dismiss();
                            mRegisterBtn.setEnabled(true);
                            e.printStackTrace();
                            Log.e("regpatient catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mRegisterBtn.setEnabled(true);

                        Log.e("regpatienterror", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(Register.this, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("first_name")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("first_name").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("last_name")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("last_name").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("phone")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("phone").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("email")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("email").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("password")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("password").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("address")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("address").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("details")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("details").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("age")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("age").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("gender")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("gender").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


}