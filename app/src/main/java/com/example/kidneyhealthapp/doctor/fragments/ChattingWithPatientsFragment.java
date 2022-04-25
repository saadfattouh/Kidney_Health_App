package com.example.kidneyhealthapp.doctor.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.chat.ChatMessagesAdapter;
import com.example.kidneyhealthapp.model.Message;
import com.example.kidneyhealthapp.utils.SharedPrefManager;
import com.example.kidneyhealthapp.utils.Urls;
import com.example.kidneyhealthapp.utils.Validation;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChattingWithPatientsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public static final String TAG = "Dchatting";

    RecyclerView mList;
    ArrayList<Message> list;
    ChatMessagesAdapter mAdapter;

    TextInputEditText mMessageET;
    ImageView mSendBtn;
    private String chatId, patientId;

    Context context;
    ProgressDialog pDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;

    NavController navController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public ChattingWithPatientsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chatId = String.valueOf(getArguments().getInt("id"));
            patientId = String.valueOf(getArguments().getInt("doctor_id"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatting_with_patients, container, false);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            getMessages();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList = view.findViewById(R.id.messages_list);
        mMessageET = view.findViewById(R.id.message_edit_text);
        mSendBtn = view.findViewById(R.id.send_btn);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        navController = Navigation.findNavController(view);
        mSendBtn.setOnClickListener(v -> {
            if(Validation.validateInput(context, mMessageET)){
                sendMessage();
            }else{
                Toast.makeText(context, context.getResources().getString(R.string.empty_msg), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(){
        pDialog.show();
        mSendBtn.setEnabled(false);
        String url = Urls.SEND_MESSAGE;
        String message = mMessageET.getText().toString().trim();
        String senderType = String.valueOf(SharedPrefManager.getInstance(context).getUserType());
        AndroidNetworking.post(url)
                .addBodyParameter("chat_id", chatId)
                .addBodyParameter("sender_type", senderType)
                .addBodyParameter("content", message)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String successMessage = "Done";
                            //if no error in response

                            if (message.toLowerCase().contains(successMessage.toLowerCase())) {

                                Toast.makeText(context, context.getResources().getString(R.string.success_send_msg), Toast.LENGTH_SHORT).show();
                                mMessageET.setText("");
                                mSwipeRefreshLayout.post(()->{
                                    mSwipeRefreshLayout.setRefreshing(true);
                                    getMessages();
                                });
                            }else{
                                Toast.makeText(context, context.getResources().getString(R.string.error_send_msg), Toast.LENGTH_SHORT).show();
                            }
                            mSendBtn.setEnabled(true);
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            pDialog.dismiss();
                            mSendBtn.setEnabled(true);
                            e.printStackTrace();
                            Log.e("login catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        mSendBtn.setEnabled(true);
                        Log.e("loginerror", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("chat_id")) {
                                Toast.makeText(context, data.getJSONArray("chat_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("sender_type")) {
                                Toast.makeText(context, data.getJSONArray("sender_type").toString(), Toast.LENGTH_SHORT).show();
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

    private void getMessages() {
//        pDialog.show();
        String url = Urls.GET_CHAT_CONTENT;

        list = new ArrayList<>();

        AndroidNetworking.get(url)
                .addQueryParameter("chat_id", chatId)
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
                            String successMessage = "Done";
                            if (message.toLowerCase().contains(successMessage.toLowerCase())) {
                                for (int i = 0; i < messagesJsonArray.length(); i++){
                                    JSONObject messageJsonObject = messagesJsonArray.getJSONObject(i);
                                    boolean fromMe;
                                    if(messageJsonObject.getInt("sender_type") == SharedPrefManager.getInstance(context).getUserType())
                                        fromMe = true;
                                    else
                                        fromMe = false;
                                    list.add(new Message(
                                            Integer.parseInt(messageJsonObject.getString("id")),
                                            Integer.parseInt(messageJsonObject.getString("sender_type")),
                                            messageJsonObject.getString("content"),
                                            messageJsonObject.getString("created_at"),
                                            fromMe

                                    ));
                                }
                                mAdapter = new ChatMessagesAdapter(list, getContext());
                                mList.setAdapter(mAdapter);

                            } else {
                                Toast.makeText(getContext(), context.getResources().getString(R.string.error_load_data), Toast.LENGTH_SHORT).show();
                            }
                            mSwipeRefreshLayout.setRefreshing(false);
//                            pDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
//                            pDialog.dismiss();
                            mSwipeRefreshLayout.setRefreshing(false);
                            Log.e(TAG, e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
//                        pDialog.dismiss();
                        mSwipeRefreshLayout.setRefreshing(false);
                        Log.e(TAG + "error", error.getErrorBody());
                    }
                });
    }

    @Override
    public void onRefresh() {
        getMessages();
    }
}