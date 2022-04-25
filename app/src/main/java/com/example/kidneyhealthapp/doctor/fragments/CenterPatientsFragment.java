package com.example.kidneyhealthapp.doctor.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.doctor.adapters.CenterPatientsAdapter;
import com.example.kidneyhealthapp.model.Patient;
import com.example.kidneyhealthapp.utils.SharedPrefManager;
import com.example.kidneyhealthapp.utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CenterPatientsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "patientsList";
    RecyclerView mList;
    ArrayList<Patient> list;
    CenterPatientsAdapter mAdapter;
    Context context;
    ProgressDialog pDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public CenterPatientsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_center_patients, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            getCenterPatients();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList = view.findViewById(R.id.rv);

        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");

    }

    @Override
    public void onRefresh() {
        getCenterPatients();
    }

    private void getCenterPatients() {
        String url = Urls.GET_CENTER_PATIENTS;
        list = new ArrayList<Patient>();
        pDialog.show();

        String centerId = String.valueOf(SharedPrefManager.getInstance(context).getCenterData().getId());
        AndroidNetworking.get(url)
                .addQueryParameter("center_id",centerId)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response;
                            String message = jsonObject.getString("message");
                            String successMessage = "Success";
                            if (message.toLowerCase().contains(successMessage.toLowerCase())) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
//                                    JSONObject doctor_data = obj.getJSONObject("doctor");
                                    list.add(
                                            new Patient(
                                                    Integer.parseInt(obj.getString("id")),
                                                    obj.getString("first_name"),
                                                    obj.getString("last_name"),
                                                    obj.getString("email"),
                                                    obj.getString("phone"),
                                                    obj.getString("address"),
                                                    obj.getInt("age"),
                                                    obj.getInt("gender")

                                            )
                                    );
                                }
                                mAdapter = new CenterPatientsAdapter(context, list);
                                mList.setAdapter(mAdapter);

                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.error_load_data), Toast.LENGTH_SHORT).show();
                            }
                            mSwipeRefreshLayout.setRefreshing(false);
                            pDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            mSwipeRefreshLayout.setRefreshing(false);
                            Log.e(TAG, e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pDialog.dismiss();
                        mSwipeRefreshLayout.setRefreshing(false);
                        Log.e(TAG + "error", error.getErrorBody());
                    }
                });

    }
}