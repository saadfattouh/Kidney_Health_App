package com.example.kidneyhealthapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.sanabelalkhayr.R;
import com.example.sanabelalkhayr.api.Urls;

import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity {

    private Button mLoginBtn;
    private Button mToRegisterBtn;
    private EditText mUserNameET;
    private EditText mPassET;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mUserNameET = findViewById(R.id.user_name);
        mPassET =  findViewById(R.id.password);
        mLoginBtn =  findViewById(R.id.btnLogin);
        mToRegisterBtn = findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Login button Click Event
        mLoginBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                //validation
                String userName = mUserNameET.getText().toString().trim();
                String password = mPassET.getText().toString().trim();

                // Check for empty data in the form
                if (!userName.isEmpty() && !password.isEmpty()) {
                    // login user
                    mLoginBtn.setEnabled(false);
                    login(userName, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.user_and_password_required), Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


        // Link to Register Screen
        mToRegisterBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        Register.class);
                startActivity(i);
            }
        });

    }

    private void login(String userName, String password) {
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        String url = Urls.BASE_URL + Urls.LOGIN_URL;

        AndroidNetworking.post(url)
                .addBodyParameter("password", password)
                .addBodyParameter("user_name", userName)
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
                        mLoginBtn.setEnabled(true);
                        Toast.makeText(Login.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}