package com.example.kidneyhealthapp.patient.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
import com.example.kidneyhealthapp.model.Instruction;
import com.example.kidneyhealthapp.model.PatientDaily;
import com.example.kidneyhealthapp.patient.adapters.DailyInfoAdapter;
import com.example.kidneyhealthapp.patient.adapters.InstructionsAdapter;
import com.example.kidneyhealthapp.utils.SharedPrefManager;
import com.example.kidneyhealthapp.utils.Urls;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DailyInfoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "doctorInst";
    DailyInfoAdapter mAdapter;
    RecyclerView mList;
    ArrayList<PatientDaily> list;

    Context context;
    ProgressDialog pDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;
    FloatingActionButton addDailyInfo;
    NavController navController;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public DailyInfoFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_info, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            getDailyInfo();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList = view.findViewById(R.id.rv);
        addDailyInfo = view.findViewById(R.id.add_btn);
        navController = Navigation.findNavController(view);
        addDailyInfo.setOnClickListener(v->{
            navController.navigate(R.id.action_DailyInfo_to_AddDailtInfo);
        });
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);
    }

    @Override
    public void onRefresh() {
        getDailyInfo();
    }

    private void getDailyInfo() {
        pDialog.show();
        String url = Urls.GET_DAILY_INFO;

        list = new ArrayList<>();

        String userId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        AndroidNetworking.get(url)
                .addQueryParameter("patient_id", userId)
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
                            String message = obj.getString("message");
                            JSONArray messagesJsonArray = obj.getJSONArray("data");
                            String successMessage = "Success";
                            if (message.toLowerCase().contains(successMessage.toLowerCase())) {
                                for (int i = 0; i < messagesJsonArray.length(); i++) {
                                    JSONObject object = messagesJsonArray.getJSONObject(i);
                                    list.add(new PatientDaily(
                                            Integer.parseInt(object.getString("id")),
                                            Integer.parseInt(object.getString("water_quntity")),
                                            object.getString("medicine_info"),
                                            object.getString("created_at")
                                    ));
                                }
                                mAdapter = new DailyInfoAdapter(context, list, false);
                                mList.setAdapter(mAdapter);

                            } else {
                                Toast.makeText(getContext(), context.getResources().getString(R.string.error_load_data), Toast.LENGTH_SHORT).show();
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