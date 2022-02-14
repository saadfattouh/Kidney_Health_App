package com.example.kidneyhealthapp.chat;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.chat.models.Chat;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    Context context;
    ArrayList<Chat> chats;

    NavController navController;

    public ChatListAdapter(Context context, ArrayList<Chat> chats) {
        this.context = context;
        this.chats = chats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.chat_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chat chat = chats.get(position);


        if(chat.getUserImageUrl() == null){
            Glide.with(context)
                    .load(context.getResources().getDrawable(R.drawable.ic_man))
                    .into(holder.image);
        }else{
            Glide.with(context)
                    .load(chat.getUserImageUrl())
                    .into(holder.image);
        }

        holder.name.setText(chat.getUserName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController = Navigation.findNavController(holder.itemView);
                Bundle bundle = new Bundle();
                bundle.putString("from_id", chat.getFromId());
                bundle.putString("user_name", chat.getUserName());
                navController.navigate(R.id.action_doctorsChatList_to_chattingFragment,bundle);
            }
        });


    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.person_profile_image);
            this.name = itemView.findViewById(R.id.person_name);
        }
    }






}
