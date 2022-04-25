package com.example.kidneyhealthapp.admin.fragments;

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
import com.example.kidneyhealthapp.model.Center;
import com.example.kidneyhealthapp.utils.SharedPrefManager;
import com.example.kidneyhealthapp.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CenterDetailsFragment extends Fragment {

    Context context;
    TextView name, location , lat, lon, info;
    Center center;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public CenterDetailsFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            center = (Center)getArguments().getSerializable("center");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_center_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.center_name);
        location = view.findViewById(R.id.center_location);
        lat = view.findViewById(R.id.center_lat);
        lon = view.findViewById(R.id.center_lon);
        info = view.findViewById(R.id.center_info);

        name.setText(center.getName());
        lat.setText(String.valueOf(center.getLat()));
        lon.setText(String.valueOf(center.getLon()));
        location.setText(center.getLocation());
        info.setText(center.getInfo());

    }

}

