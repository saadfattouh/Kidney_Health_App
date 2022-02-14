package com.example.kidneyhealthapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.api.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateProfile extends AppCompatActivity {

    EditText mNameET;
    EditText mUserNameET;
    EditText mPhoneET;
    EditText mAddressET;

    Button mSubmitBtn;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


        bindViews();

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        mSubmitBtn.setOnClickListener(v -> {
            if(validateUserInput()){
                mSubmitBtn.setEnabled(false);
                register();
            }
        });


    }


    private void bindViews() {
        mNameET = findViewById(R.id.name);
        mUserNameET = findViewById(R.id.user_name);
        mPhoneET = findViewById(R.id.phone);
        mAddressET = findViewById(R.id.address);
        mSubmitBtn = findViewById(R.id.btnRegister);
    }

    private boolean validateUserInput() {

        //first getting the values
        final String name = mNameET.getText().toString();
        final String phone = mPhoneET.getText().toString();
        final String userName = mUserNameET.getText().toString();
        final String address = mAddressET.getText().toString();

        //checking if username is empty
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, getResources().getString(R.string.field_missing_message), Toast.LENGTH_SHORT).show();
            mSubmitBtn.setEnabled(true);
            return false;
        }

        //checking if userName is empty
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, getResources().getString(R.string.field_missing_message), Toast.LENGTH_SHORT).show();
            mSubmitBtn.setEnabled(true);
            return false;
        }


        //checking if phone is empty
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, getResources().getString(R.string.field_missing_message), Toast.LENGTH_SHORT).show();
            mSubmitBtn.setEnabled(true);
            return false;
        }



        //checking if address is empty
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, getResources().getString(R.string.field_missing_message), Toast.LENGTH_SHORT).show();
            mSubmitBtn.setEnabled(true);
            return false;
        }


        return true;
    }

    private void register() {
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        //first getting the values
        final String name = mNameET.getText().toString();
        final String phone = mPhoneET.getText().toString();
        final String userName = mUserNameET.getText().toString();
        final String address = mAddressET.getText().toString();


        String url = Urls.BASE_URL + Urls.REGISTER;

        AndroidNetworking.post(url)
                .addBodyParameter("name", name)
                .addBodyParameter("phone", phone)
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
                        mSubmitBtn.setEnabled(true);
                        Toast.makeText(UpdateProfile.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}