package com.example.kidneyhealthapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.kidneyhealthapp.Constants;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.utils.Validation;


import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    EditText mNameET;
    EditText mUserNameET;
    EditText mPassET;
    EditText mPhoneET;
    EditText mAddressET;
    EditText mDetails;
    EditText mAgeEt;

    Button mRegisterBtn;
    Button mToLoginBtn;

    RadioGroup mAccountTypeSelector;
    int selectedUserType = -1;

    RadioGroup mGenderSelector;
    int selectedGender = -1;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bindViews();

        selectedUserType = Constants.USER_TYPE_PATIENT;
        mAccountTypeSelector.check(R.id.patient);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        mToLoginBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        mRegisterBtn.setOnClickListener(v -> {
            switch (selectedUserType){
                case Constants.USER_TYPE_PATIENT:
                    if(Validation.validateInput(Register.this, mNameET, mUserNameET, mPassET, mPhoneET, mAddressET, mAgeEt)){
                        mRegisterBtn.setEnabled(false);
                        registerPatient();
                    }
                    break;

                case Constants.USER_TYPE_DOCTOR:
                    if(Validation.validateInput(Register.this, mNameET, mUserNameET, mPassET, mPhoneET, mDetails)){
                        mRegisterBtn.setEnabled(false);
                        registerPatient();
                    }
                    break;
            }
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



    }



    private void updateUi() {
        if(selectedUserType == Constants.USER_TYPE_DOCTOR){
            mDetails.setVisibility(View.VISIBLE);
            mAddressET.setVisibility(View.GONE);
            mGenderSelector.setVisibility(View.GONE);
            mAgeEt.setVisibility(View.GONE);
        }else {
            mDetails.setVisibility(View.GONE);
            mAddressET.setVisibility(View.VISIBLE);
            mGenderSelector.setVisibility(View.VISIBLE);
            mAgeEt.setVisibility(View.VISIBLE);
        }
    }

    private void bindViews() {
        mNameET = findViewById(R.id.name);
        mPassET = findViewById(R.id.password);
        mUserNameET = findViewById(R.id.user_name);
        mPhoneET = findViewById(R.id.phone);
        mAddressET = findViewById(R.id.address);
        mDetails = findViewById(R.id.details);
        mAgeEt = findViewById(R.id.age);

        mRegisterBtn = findViewById(R.id.btnRegister);
        mToLoginBtn = findViewById(R.id.btnLinkToLoginScreen);

        mAccountTypeSelector = findViewById(R.id.type_selector);
        mGenderSelector = findViewById(R.id.gender_selector);
    }


    //todo api call send the new patient data into database
    private void registerPatient() {
    }

    //todo api call send the new doctor data into database
    private void registerDoctor() {
    }

    private void register() {
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        //first getting the values
        final String pass = mPassET.getText().toString();
        final String name = mNameET.getText().toString();
        final String phone = mPhoneET.getText().toString();
        final String userName = mUserNameET.getText().toString();
        final String address = mAddressET.getText().toString();


        String url = "";

        AndroidNetworking.post(url)
                .addBodyParameter("type", String.valueOf(selectedUserType))
                .addBodyParameter("name", name)
                .addBodyParameter("phone", phone)
                .addBodyParameter("password", pass)
                .addBodyParameter("user_name", userName)
                .addBodyParameter("address", address)
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

//                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//
//                                //getting the user from the response
//                                JSONObject userJson = obj.getJSONObject("data");
//                                User user;
//                                SharedPrefManager.getInstance(getApplicationContext()).setUserType(Constants.USER);
//                                user = new User(
//                                        Integer.parseInt(userJson.getString("id")),
//                                        userJson.getString("name"),
//                                        "+966 "+userJson.getString("phone")
//                                );
//
//                                //storing the user in shared preferences
//                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
//                                goToUserMainActivity();
//                                finish();
//
//                                mRegisterBtn.setEnabled(true);
//                            } else if(obj.getInt("status") == -1){
//                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                                mRegisterBtn.setEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mRegisterBtn.setEnabled(true);
                        Toast.makeText(Register.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}