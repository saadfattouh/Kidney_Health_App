package com.example.kidneyhealthapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.sanabelalkhayr.Constants;
import com.example.sanabelalkhayr.R;
import com.example.sanabelalkhayr.api.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    EditText mNameET;
    EditText mUserNameET;
    EditText mPassET;
    EditText mPhoneET;
    EditText mAddressET;

    Button mRegisterBtn;
    Button mToLoginBtn;

    RadioGroup mAccountTypeSelector;
    int selectedUserType = -1;

    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bindViews();

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        mToLoginBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        mRegisterBtn.setOnClickListener(v -> {
            if(validateUserInput()){
                mRegisterBtn.setEnabled(false);
                register();
            }
        });


        mAccountTypeSelector.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.main_user:
                    selectedUserType = Constants.USER_TYPE_MAIN;
                    break;
                case R.id.donor:
                    selectedUserType = Constants.USER_TYPE_DONOR;
                    break;
                case R.id.volunteer:
                    selectedUserType = Constants.USER_TYPE_VOLUNTEER;
                    break;
            }
        });



    }

    private void bindViews() {
        mNameET = findViewById(R.id.name);
        mPassET = findViewById(R.id.password);
        mUserNameET = findViewById(R.id.user_name);
        mPhoneET = findViewById(R.id.phone);
        mAddressET = findViewById(R.id.address);


        mRegisterBtn = findViewById(R.id.btnRegister);
        mToLoginBtn = findViewById(R.id.btnLinkToLoginScreen);

        mAccountTypeSelector = findViewById(R.id.type_selector);
    }

    private boolean validateUserInput() {

        //first getting the values
        final String pass = mPassET.getText().toString();
        final String name = mNameET.getText().toString();
        final String phone = mPhoneET.getText().toString();
        final String userName = mUserNameET.getText().toString();
        final String address = mAddressET.getText().toString();

        //checking if username is empty
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, getResources().getString(R.string.name_missing_message), Toast.LENGTH_SHORT).show();
            mRegisterBtn.setEnabled(true);
            return false;
        }

        //checking if userName is empty
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, getResources().getString(R.string.user_name_missing_message), Toast.LENGTH_SHORT).show();
            mRegisterBtn.setEnabled(true);
            return false;
        }

        //checking if password is empty
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, getResources().getString(R.string.password_missing_message), Toast.LENGTH_SHORT).show();
            mRegisterBtn.setEnabled(true);
            return false;
        }


        //checking if phone is empty
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, getResources().getString(R.string.phone_missing_message), Toast.LENGTH_SHORT).show();
            mRegisterBtn.setEnabled(true);
            return false;
        }



        //checking if address is empty
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, getResources().getString(R.string.address_missing_message), Toast.LENGTH_SHORT).show();
            mRegisterBtn.setEnabled(true);
            return false;
        }

        if(selectedUserType == -1){
            Toast.makeText(this, getResources().getString(R.string.type_missing_message), Toast.LENGTH_SHORT).show();
            mRegisterBtn.setEnabled(true);
            return false;
        }

        return true;
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


        String url = Urls.BASE_URL + Urls.REGISTER_URL;

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