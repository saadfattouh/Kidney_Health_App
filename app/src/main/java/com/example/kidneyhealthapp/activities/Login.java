package com.example.kidneyhealthapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class Login extends AppCompatActivity {

    private Button mLoginBtn, mToRegisterBtn;
    private EditText mEmailET,mPassET;

    private ProgressDialog pDialog;

    RadioGroup mAccountTypeSelector;
    int selectedUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailET = findViewById(R.id.email);
        mPassET =  findViewById(R.id.password);
        mLoginBtn =  findViewById(R.id.btnLogin);
        mToRegisterBtn = findViewById(R.id.btnLinkToRegisterScreen);

        mAccountTypeSelector = findViewById(R.id.type_selector);
        selectedUserType = Constants.USER_TYPE_PATIENT;
        mAccountTypeSelector.check(R.id.patient);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);

        mAccountTypeSelector.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.doctor:
                    selectedUserType = Constants.USER_TYPE_DOCTOR;
                    break;
                case R.id.patient:
                    selectedUserType = Constants.USER_TYPE_PATIENT;
                    break;
            }
        });
        // Link to Register Screen
        mToRegisterBtn.setOnClickListener(view -> {
            startActivity(new Intent(Login.this,Register.class));
            finish();
        });

        mLoginBtn.setOnClickListener(view -> {
            if(Validation.validateInput(Login.this, mEmailET, mPassET)) {
                login();
            }
        });
    }

    private void login() {
        pDialog.show();
        mLoginBtn.setEnabled(false);
        String email = mEmailET.getText().toString().trim();
        String password = mPassET.getText().toString().trim();
        String url = Urls.LOGIN;

        if(email.equals("admin") && password.equals("12345")){
            SharedPrefManager.getInstance(this).adminLogin();
            pDialog.dismiss();
         goToAdmin();
         return;
        }
        AndroidNetworking.post(url)
                .addBodyParameter("type", String.valueOf(selectedUserType))
                .addBodyParameter("password", password)
                .addBodyParameter("email", email)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String userFounded = "User founded";
                            //if no error in response

                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {

                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("data");
                                int userType = userJson.getInt("type");
                                if(userType == Constants.USER_TYPE_DOCTOR){
                                    SharedPrefManager.getInstance(getApplicationContext()).doctorLogin(new Doctor(
                                            Integer.parseInt(userJson.getString("id")),
                                            userJson.getString("first_name"),
                                            userJson.getString("last_name"),
                                            userJson.getString("email"),
                                            userJson.getString("phone"),
                                            userJson.getString("details")
                                    ));
                                    startActivity(new Intent(Login.this, DoctorMain.class));
                                    finish();
                                }else if(userType == Constants.USER_TYPE_PATIENT){

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
                                    startActivity(new Intent(Login.this, PatientMain.class));
                                    finish();
                                }
                            }else{
                                Toast.makeText(Login.this, getResources().getString(R.string.wrong_pass), Toast.LENGTH_SHORT).show();
                            }
                            mLoginBtn.setEnabled(true);
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            pDialog.dismiss();
                            mLoginBtn.setEnabled(true);
                            e.printStackTrace();
                            Log.e("login catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mLoginBtn.setEnabled(true);
                        Log.e("loginerror", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(Login.this, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("email")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("email").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("password")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("password").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("type")) {
                                Toast.makeText(getApplicationContext(), data.getJSONArray("type").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void goToAdmin() {
        startActivity(new Intent(this, AdminMain.class));
        finish();
    }
}