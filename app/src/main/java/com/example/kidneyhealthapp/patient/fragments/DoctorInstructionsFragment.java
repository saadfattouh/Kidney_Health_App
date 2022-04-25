package com.example.kidneyhealthapp.patient.fragments;

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
import com.example.kidneyhealthapp.patient.adapters.InstructionsAdapter;
import com.example.kidneyhealthapp.model.Instruction;
import com.example.kidneyhealthapp.utils.SharedPrefManager;
import com.example.kidneyhealthapp.utils.Urls;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class DoctorInstructionsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "doctorInst";
    InstructionsAdapter mAdapter;
    RecyclerView mList;
    ArrayList<Instruction> list;

    private String doctorId;

    Context context;
    ProgressDialog pDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public DoctorInstructionsFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            doctorId = getArguments().getString("doctor_id");
            Log.e("dId", doctorId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_instructions, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            getInstructions();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList = view.findViewById(R.id.rv);
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);
    }

    @Override
    public void onRefresh() {
        getInstructions();
    }

    private void getInstructions() {
        pDialog.show();
        String url = Urls.GET_INSTRUCTIONS;

        list = new ArrayList<>();



        String userId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        Log.e("dId", doctorId);
        Log.e("pId", userId);
        AndroidNetworking.get(url)
                .addQueryParameter("patient_id", userId)
                .addQueryParameter("doctor_id", doctorId)
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
                                    list.add(new Instruction(
                                            Integer.parseInt(object.getString("id")),
//                                            object.getString("created_at").subString(0,10),
                                            "22-4-2022",
                                            object.getString("content")
                                    ));
                                }
                                mAdapter = new InstructionsAdapter(context, list);
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