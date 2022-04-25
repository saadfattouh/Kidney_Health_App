package com.example.kidneyhealthapp.patient.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.model.Chat;

import java.util.ArrayList;

public class DoctorsChatListAdapter extends RecyclerView.Adapter<DoctorsChatListAdapter.ViewHolder> {

    Context context;
    ArrayList<Chat> chats;

    NavController navController;

    public DoctorsChatListAdapter(Context context, ArrayList<Chat> chats) {
        this.context = context;
        this.chats = chats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_doctor_chat_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chat chat = chats.get(position);

        holder.name.setText(chat.getUserName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController = Navigation.findNavController(holder.itemView);
                Bundle bundle = new Bundle();
                bundle.putInt("id", chat.getChatId());
                Log.e("chtl", String.valueOf(chat.getOtherMemberId()));
                bundle.putInt("doctor_id", chat.getOtherMemberId());
                navController.navigate(R.id.action_doctorsChatList_to_chattingFragment,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.person_name);
        }
    }
}
