package com.example.kidneyhealthapp.doctor.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import com.example.kidneyhealthapp.utils.Urls;
import com.example.kidneyhealthapp.model.Center;
import com.example.kidneyhealthapp.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyCenterFragment extends Fragment {

    Context context;
    TextView name, location , lat, lon, info;

    Button mUpdateCenterBtn;//TODO
    ProgressDialog pDialog;

    NavController navController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public MyCenterFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_center, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing Please wait...");

        navController = Navigation.findNavController(view);

        name = view.findViewById(R.id.center_name);
        location = view.findViewById(R.id.center_location);
        lat = view.findViewById(R.id.center_lat);
        lon = view.findViewById(R.id.center_lon);
        info = view.findViewById(R.id.center_info);
        mUpdateCenterBtn = view.findViewById(R.id.update_center);

        mUpdateCenterBtn.setOnClickListener(v->{
            navController.navigate(R.id.action_Center_to_UpdateProfileFragment);
        });
        getCenterInfo();
    }

    private void getCenterInfo() {
        pDialog.show();
        String url = Urls.GET_DOCTOR_CENTER;

        String id = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        AndroidNetworking.get(url)
                .addQueryParameter("doctor_id", id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String userFounded = "Success";
                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {

                                JSONArray jsonArray = obj.getJSONArray("data");
                                if(jsonArray.length()==0){
                                    Toast.makeText(context, context.getResources().getString(R.string.account_not_linked_to_center), Toast.LENGTH_SHORT).show();

                                }else{
                                //getting the user from the response
                                    Toast.makeText(context, context.getResources().getString(R.string.get_center_succes), Toast.LENGTH_SHORT).show();
                                    JSONObject dataJson = jsonArray.getJSONObject(0);
                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(context).centerSave(new Center(
                                        Integer.parseInt(dataJson.getString("id")),
                                        dataJson.getString("name"),
                                        dataJson.getDouble("lat"),
                                        dataJson.getDouble("lon"),
                                        dataJson.getString("location"),
                                        dataJson.getString("info")
                                ));
                                name.setText(dataJson.getString("name"));
                                lat.setText( String.valueOf(dataJson.getDouble("lat")));
                                lon.setText(String.valueOf(dataJson.getDouble("lon")));
                                location.setText(dataJson.getString("location"));
                                info.setText(dataJson.getString("info"));}
                            } else{
                                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                            Log.e("center catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        Toast.makeText(context, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("center anError", anError.getErrorBody());
                    }
                });
    }
}

