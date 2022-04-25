package com.example.kidneyhealthapp.admin.fragments;

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
import com.example.kidneyhealthapp.model.Center;
import com.example.kidneyhealthapp.utils.SharedPrefManager;
import com.example.kidneyhealthapp.utils.Urls;
import com.example.kidneyhealthapp.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

public class AddCenterFragment extends Fragment {

    private static final String TAG = "updateCenter";
    EditText nameET, infoET, locationET, latET, lonET;
    Button mAddCenterBtn;
    ProgressDialog pDialog;
    NavController navController;
    Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public AddCenterFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_center, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameET = view.findViewById(R.id.tv_name);
        infoET = view.findViewById(R.id.tv_info);
        locationET = view.findViewById(R.id.tv_location);
        latET = view.findViewById(R.id.tv_lat);
        lonET = view.findViewById(R.id.tv_lon);
        mAddCenterBtn = view.findViewById(R.id.add_center);

        navController = Navigation.findNavController(view);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Processing Please wait...");

        mAddCenterBtn.setOnClickListener(v->{
            if(Validation.validateInput(context, nameET, infoET, locationET, latET, lonET)){
                addCenter();
            }
        });

    }

    private void addCenter() {

        pDialog.show();
        mAddCenterBtn.setEnabled(false);

        String url = Urls.ADD_CENTER;
        String name = nameET.getText().toString().trim();
        String info = infoET.getText().toString().trim();
        String location = locationET.getText().toString().trim();
        String lat = latET.getText().toString().trim();
        String lon = lonET.getText().toString().trim();
        AndroidNetworking.post(url)
                .addBodyParameter("name", name)
                .addBodyParameter("location", location)
                .addBodyParameter("info", info)
                .addBodyParameter("lat", lon)
                .addBodyParameter("lon", lat)
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
                                Toast.makeText(context, context.getResources().getString(R.string.center_added), Toast.LENGTH_SHORT).show();
                                pDialog.dismiss();
                                mAddCenterBtn.setEnabled(true);
                                navController.popBackStack();
                            }else{
                                Toast.makeText(context, context.getResources().getString(R.string.operation_error), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            pDialog.dismiss();
                            mAddCenterBtn.setEnabled(true);
                            e.printStackTrace();
                            Log.e(TAG+"c", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mAddCenterBtn.setEnabled(true);

                        Log.e(TAG+"e", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("name")) {
                                Toast.makeText(context, data.getJSONArray("name").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("location")) {
                                Toast.makeText(context, data.getJSONArray("location").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("info")) {
                                Toast.makeText(context, data.getJSONArray("info").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("lat")) {
                                Toast.makeText(context, data.getJSONArray("lat").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("lon")) {
                                Toast.makeText(context, data.getJSONArray("lon").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}