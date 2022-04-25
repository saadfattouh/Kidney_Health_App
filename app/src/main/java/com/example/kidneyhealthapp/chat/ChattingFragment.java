package com.example.kidneyhealthapp.chat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.utils.Urls;
import com.example.kidneyhealthapp.chat.models.BaseMessage;
import com.example.kidneyhealthapp.utils.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChattingFragment extends Fragment {

    public static final String TAG = "chatInAction";


    RecyclerView mMessagesList;
    ArrayList<BaseMessage> messages;
    ChatMessagesAdapter messagesAdapter;

    TextInputEditText messageET;
    ImageView sendBtn;

    private String from_id;
    private String from_user_name;

    public ChattingFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            from_id = getArguments().getString("from_id");
            from_user_name = getArguments().getString("user_name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMessagesList = view.findViewById(R.id.messages_list);
        messageET = view.findViewById(R.id.message_edit_text);
        sendBtn = view.findViewById(R.id.send_btn);


        sendBtn.setOnClickListener(v -> {

            String message = messageET.getText().toString();
            messageET.setText("");
            if(TextUtils.isEmpty(message)){

            }else{
                sendMessage(message);
            }

        });

        messages = new ArrayList<BaseMessage>()
        {{
            add(new BaseMessage("1", false, "hi"));
            add(new BaseMessage("1", true, "hey"));
            add(new BaseMessage("1", false, "how are you"));
            add(new BaseMessage("1", true, "fine HBU?"));
            add(new BaseMessage("1", false, "great thanks, how's the weather today?"));
            add(new BaseMessage("1", true, "it's cool let's hang out bud"));
        }};

//        messagesAdapter = new ChatMessagesAdapter(messages, getContext());
        mMessagesList.setAdapter(messagesAdapter);

//        getAllMessages();


    }

    private void sendMessage(String message) {

        String url = Urls.BASE_URL;

        BaseMessage baseMessage = new BaseMessage(
                "1",
                true,
                message
        );
        if(messages!=null){
            messages.add(baseMessage);
        }else{
            messages = new ArrayList<BaseMessage>();
            messages.add(baseMessage);
        }

//        messagesAdapter = new ChatMessagesAdapter(messages, getContext());
        mMessagesList.setAdapter(messagesAdapter);

//        AndroidNetworking.post(url)
//                .setPriority(Priority.MEDIUM)
//                .addBodyParameter("type", "send_message")
//                .addBodyParameter("content", message)
//                .addBodyParameter("from_id", String.valueOf(SharedPrefManager.getInstance(getContext()).getUserId()))
//                .addBodyParameter("to_id", String.valueOf(from_id))
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // do anything with response
//                        JSONObject obj = response;
//                        try {
//
//                            if (!obj.getBoolean("error")) {
//                                boolean fromMe;
//                                JSONObject messageJsonObject = obj.getJSONObject("data");
//                                if (messageJsonObject.getString("from_id").equals(String.valueOf(SharedPrefManager.getInstance(getContext()).getUserId())))
//                                    fromMe = true;
//                                else
//                                    fromMe = false;
//                                BaseMessage baseMessage = new BaseMessage(
//                                        messageJsonObject.getString("id"),
//                                        fromMe,
//                                        messageJsonObject.getString("content")
//                                );
//                                if(messages!=null){
//                                    messages.add(baseMessage);
//                                }else{
//                                    messages = new ArrayList<BaseMessage>();
//                                    messages.add(baseMessage);
//                                }
//
//                                messagesAdapter = new ChatMessagesAdapter(messages, getContext());
//                                mMessagesList.setAdapter(messagesAdapter);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//                        // handle error
//                        Log.e("Error", error.getMessage());
//
//                    }
//                });

    }

    private void getAllMessages() {

        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        String url = Urls.GET_MESSAGES_WITH + "&user_id="+ SharedPrefManager.getInstance(getContext()).getUserId() + "&with_id="+ from_id;

        messages = new ArrayList<>();

        AndroidNetworking.get(url)
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

                            //if no error in response
                            if (!obj.getBoolean("error")) {

                                //getting the user from the response
                                JSONArray messagesJsonArray = obj.getJSONArray("data");
                                BaseMessage baseMessage;
                                for (int i = 0; i < messagesJsonArray.length(); i++){
                                    JSONObject messageJsonObject = messagesJsonArray.getJSONObject(i);
                                    boolean fromMe;
                                    if(messageJsonObject.getString("from_id").equals(String.valueOf(SharedPrefManager.getInstance(getContext()).getUserId())))
                                        fromMe = true;
                                    else
                                        fromMe = false;
                                    baseMessage = new BaseMessage(
                                            messageJsonObject.getString("id"),
                                            fromMe,
                                            messageJsonObject.getString("content")
                                    );
                                    messages.add(baseMessage);
                                }

//                                messagesAdapter = new ChatMessagesAdapter(messages, getContext());
                                mMessagesList.setAdapter(messagesAdapter);

                            } else {
                                Toast.makeText(getContext(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}