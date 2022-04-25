package com.example.kidneyhealthapp.admin.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.model.Center;

import java.util.ArrayList;
import java.util.List;

public class ChooseDialysisCenterAdapter extends RecyclerView.Adapter<ChooseDialysisCenterAdapter.ViewHolder>{

    Context context;
    private List<Center> centers;
    public NavController navController;
    ProgressDialog pDialog;
    OnCenterChosen onCenterChosen;
    // RecyclerView recyclerView;
    public ChooseDialysisCenterAdapter(Context context, ArrayList<Center> centers, OnCenterChosen onCenterChosen) {
        this.context = context;
        this.centers = centers;
        this.onCenterChosen = onCenterChosen;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_choose_center, parent, false);
        navController = Navigation.findNavController(parent);

        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Center center = centers.get(position);

        holder.centerNameTV.setText(center.getName());

        holder.centerLocationTV.setText(center.getLocation());

        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");

        holder.itemView.setOnClickListener(v -> {
            onCenterChosen.onCenterChosen(String.valueOf(center.getId()));
        });

    }

    @Override
    public int getItemCount() {
        return centers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView centerNameTV;
        public TextView centerLocationTV;
        public Button update;

        public ViewHolder(View itemView) {
            super(itemView);
            this.centerNameTV = itemView.findViewById(R.id.center_name);
            this.centerLocationTV = itemView.findViewById(R.id.center_location);
            this.update = itemView.findViewById(R.id.update_center);
        }
    }

    public  interface  OnCenterChosen{
        public void onCenterChosen( String centerId);
    }
}