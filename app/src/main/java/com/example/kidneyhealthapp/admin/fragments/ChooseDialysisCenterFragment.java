package com.example.kidneyhealthapp.admin.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.admin.adapters.ChooseDialysisCenterAdapter;
import com.example.kidneyhealthapp.model.Center;
import com.example.kidneyhealthapp.utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChooseDialysisCenterFragment extends Fragment implements ChooseDialysisCenterAdapter.OnCenterChosen {

    ChooseDialysisCenterAdapter mAdapter;
    ArrayList<Center> list;
    RecyclerView mList;
    Context context;
    ProgressDialog pDialog;

    NavController navController;

    String doctorId;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public ChooseDialysisCenterFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            doctorId= getArguments().getString("doctor_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_dialysis_centers, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList = view.findViewById(R.id.rv);
        navController = Navigation.findNavController(view);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);
        getCenters();

    }

    private void getCenters() {
        String url = Urls.GET_CENTERS;
        pDialog.show();
        list = new ArrayList<Center>();

        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String messageGot = "Success";
                            String message = response.getString("message");
                            if (message.toLowerCase().contains(messageGot.toLowerCase())) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);

                                    list.add(
                                            new Center(
                                                    Integer.parseInt(obj.getString("id")),
                                                    obj.getString("name"),
                                                    obj.getDouble("lat"),
                                                    obj.getDouble("lon"),
                                                    obj.getString("location"),
                                                    obj.getString("info")
                                            )
                                    );
                                }
                                mAdapter = new ChooseDialysisCenterAdapter(context, list, ChooseDialysisCenterFragment.this);
                                mList.setAdapter(mAdapter);
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            Log.e("replies catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        Log.e("replies anerror", error.getErrorBody());
                    }
                });
    }

    @Override
    public void onCenterChosen(String centerId) {
        linkDoctorToCenter(doctorId, centerId);
    }

    private void linkDoctorToCenter(String doctorId, String centerId) {
        navController.popBackStack();
    }
}