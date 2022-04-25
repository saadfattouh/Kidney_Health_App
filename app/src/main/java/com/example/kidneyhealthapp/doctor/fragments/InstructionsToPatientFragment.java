package com.example.kidneyhealthapp.doctor.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.model.Instruction;
import com.example.kidneyhealthapp.patient.adapters.InstructionsAdapter;
import com.example.kidneyhealthapp.utils.SharedPrefManager;
import com.example.kidneyhealthapp.utils.Urls;
import com.example.kidneyhealthapp.utils.Validation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InstructionsToPatientFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "InstToPat";
    InstructionsAdapter mAdapter;
    RecyclerView mList;
    ArrayList<Instruction> list;
    FloatingActionButton addInstructionFAB;
    private String patientId;

    Context context;
    ProgressDialog pDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public InstructionsToPatientFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            patientId = getArguments().getString("patient_id");
            Log.e(TAG ,patientId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instructions_to_patient, container, false);
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
        addInstructionFAB = view.findViewById(R.id.add_btn);

        addInstructionFAB.setOnClickListener(v->{
            LayoutInflater factory = LayoutInflater.from(context);
            final View view1 = factory.inflate(R.layout.dialog_add_instruction, null);
            final AlertDialog changeStatusDialog = new AlertDialog.Builder(context).create();
            changeStatusDialog.setView(view1);

            TextView ok = view1.findViewById(R.id.yes_btn);
            TextView no = view1.findViewById(R.id.no_btn);
            EditText instructionET = view1.findViewById(R.id.instruction);
            no.setOnClickListener(v12 ->
                    changeStatusDialog.dismiss()
            );
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Validation.validateInput(context, instructionET)){
                        String ins = instructionET.getText().toString().trim();
                        addInstruction(ins);
                        changeStatusDialog.dismiss();
                    }
                }
            });

            changeStatusDialog.show();
        });

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);
    }

    @Override
    public void onRefresh() {
        getInstructions();
    }

    public void addInstruction(String content){
        pDialog.show();
        String url = Urls.ADD_INSTRUCTION;

        String userId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        AndroidNetworking.post(url)
                .addBodyParameter("doctor_id", userId)
                .addBodyParameter("patient_id", patientId)
                .addBodyParameter("content", content)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String userFounded = "User Saved";
                            //if no error in response
                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {
                                Toast.makeText(context, context.getResources().getString(R.string.inst_add_success), Toast.LENGTH_SHORT).show();
                                mSwipeRefreshLayout.post(() -> {
                                    mSwipeRefreshLayout.setRefreshing(true);
                                    getInstructions();
                                });
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.operation_error), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            Log.e(TAG, e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        Log.e(TAG, anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("doctor_id")) {
                                Toast.makeText(context, data.getJSONArray("doctor_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("patient_id")) {
                                Toast.makeText(context, data.getJSONArray("patient_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("content")) {
                                Toast.makeText(context, data.getJSONArray("content").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    private void getInstructions() {
        pDialog.show();
        String url = Urls.GET_INSTRUCTIONS;

        list = new ArrayList<>();

        String userId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());
        AndroidNetworking.get(url)
                .addQueryParameter("doctor_id", userId)
                .addQueryParameter("patient_id", patientId)
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
//                                            object.getString("created_at").substring(0,10),
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