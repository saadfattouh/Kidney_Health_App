package com.example.kidneyhealthapp.doctor.fragments;

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
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.doctor.adapters.AppointmentRequestsAdapter;
import com.example.kidneyhealthapp.model.Appointment;
import com.example.kidneyhealthapp.model.Center;
import com.example.kidneyhealthapp.utils.SharedPrefManager;
import com.example.kidneyhealthapp.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppointmentRequestsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "appReqs";
    Context context;
    ArrayList<Appointment> list;
    AppointmentRequestsAdapter mAdapter;
    RecyclerView mList;
    ProgressDialog pDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public AppointmentRequestsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_requests, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            if(SharedPrefManager.getInstance(context).getCenterData().getId() == -1){
              getCenterInfo();
            }else{
                getAppointmentRequests();
            }
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

    private void getAppointmentRequests() {
        String url = Urls.GET_APPOINTMENT_REQUESTS;
        list = new ArrayList<Appointment>();
        pDialog.show();

        String centerId = String.valueOf(SharedPrefManager.getInstance(context).getCenterData().getId());
        Log.e("cid", String.valueOf(SharedPrefManager.getInstance(context).getCenterData().getId()));
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
                                    JSONObject patient_data = obj.getJSONObject("patient");
                                    list.add(
                                            new Appointment(
                                                    Integer.parseInt(obj.getString("id")),
                                                    "doctor_name",
                                                    obj.getString("center_id"),
                                                    obj.getInt("patient_id"),
                                                    obj.getString("create_at").substring(0,10),
                                                    Integer.parseInt(obj.getString("status").equals("null")?
                                                            "1":obj.getString("status")),
                                                    obj.getString("result_info").equals("null")?"no status added":obj.getString("result_info"),
                                                            obj.getString("patientStatus").equals("null")?"no status added":obj.getString("patientStatus"),
                                                    patient_data.getString("first_name") + " "+
                                                            patient_data.getString("last_name")
                                                    )
                                    );
                                }
                                mAdapter = new AppointmentRequestsAdapter(context, list);
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

    @Override
    public void onRefresh() {
        getAppointmentRequests();
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
                                    Toast.makeText(context, context.getResources().getString(R.string.get_center_succes), Toast.LENGTH_SHORT).show();

                                    //getting the user from the response
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
                                    getAppointmentRequests();
                                }
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
