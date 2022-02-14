package com.example.kidneyhealthapp.chat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.kidneyhealthapp.api.Urls;
import com.example.kidneyhealthapp.chat.models.Chat;
import com.example.kidneyhealthapp.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ChatListFragment extends Fragment {


    public static final String TAG = "chatsFragment";

    RecyclerView mChatsList;
    ArrayList<Chat> chats;
    ChatListAdapter chatListAdapter;

    public ChatListFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChatsList = view.findViewById(R.id.chats_list);

        chats = new ArrayList<Chat>()
        {{
            add(new Chat("teacher1", "1"));
            add(new Chat("teacher1", "1"));
            add(new Chat("teacher1", "1"));
            add(new Chat("teacher1", "1"));
            add(new Chat("teacher1", "1"));
            add(new Chat("teacher1", "1"));
            add(new Chat("teacher1", "1"));
            add(new Chat("teacher1", "1"));

        }};

        chatListAdapter = new ChatListAdapter(getContext(), chats);
        mChatsList.setAdapter(chatListAdapter);

//        loadChatsList();

    }

    private void loadChatsList() {
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        String url = Urls.GET_MESSAGES_LIST;
        url += "&user_id=" + SharedPrefManager.getInstance(getContext()).getUserId();

        chats = new ArrayList<>();

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
                                JSONArray chatsArray = obj.getJSONArray("data");
                                Chat chat;
                                for (int i = 0; i < chatsArray.length(); i++){
                                    JSONObject chatJson = chatsArray.getJSONObject(i);
                                    chat = new Chat(
                                            chatJson.getString("from_name"),
                                            chatJson.getString("from_id"),
                                            null
                                    );
                                    chats.add(chat);
                                }

                                if(chats.isEmpty()){
                                    Toast.makeText(getContext(), "no chats to display yet", Toast.LENGTH_SHORT).show();
                                }else{
                                    chatListAdapter = new ChatListAdapter(getContext(), chats);
                                    mChatsList.setAdapter(chatListAdapter);
                                }

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