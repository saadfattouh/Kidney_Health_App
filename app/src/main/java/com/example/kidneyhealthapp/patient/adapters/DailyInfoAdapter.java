package com.example.kidneyhealthapp.patient.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.model.Instruction;
import com.example.kidneyhealthapp.model.PatientDaily;
import com.example.kidneyhealthapp.utils.SharedPrefManager;
import com.example.kidneyhealthapp.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DailyInfoAdapter extends RecyclerView.Adapter<DailyInfoAdapter.ViewHolder> {

    Context context;
    private List<PatientDaily> list;
    ProgressDialog pDialog;
    boolean forDoctor;

    public DailyInfoAdapter(Context context, ArrayList<PatientDaily> list, boolean forDoctor) {
        this.context = context;
        this.list = list;
        this.forDoctor = forDoctor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_daily_info, parent, false);

        return new ViewHolder(listItem);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PatientDaily item = list.get(position);
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");
        holder.water.setText(String.valueOf(item.getWater()));
        holder.medicine.setText(item.getMedicine());
        if(forDoctor){
            holder.delete.setVisibility(View.GONE);
        }else{
            holder.delete.setVisibility(View.VISIBLE);
        }
        holder.date.setText(item.getDate());
        holder.delete.setOnClickListener(v->{

            LayoutInflater factory = LayoutInflater.from(context);
            final View view = factory.inflate(R.layout.dialog_delete, null);
            final AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
            deleteDialog.setView(view);

            TextView ok = view.findViewById(R.id.ok);
            TextView cancel = view.findViewById(R.id.cancel);

            ok.setOnClickListener(v1 ->{
                    deleteItem(item.getId(), position);
                deleteDialog.dismiss();
            });
            cancel.setOnClickListener(v1 ->
                    deleteDialog.dismiss()
            );
            deleteDialog.show();
        });
    }

    private void deleteItem(int dailyInfoId, int position) {
        pDialog.show();
        String url = Urls.DELETE_DAILY;
        String userId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());

        AndroidNetworking.post(url)
                .addBodyParameter("patient_id", userId)
                .addBodyParameter("daily_id", String.valueOf(dailyInfoId))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String userFounded = "Deleted";
                            //if no error in response
                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {
                                Toast.makeText(context, context.getResources().getString(R.string.success_delete), Toast.LENGTH_SHORT).show();
                                notifyItemRemoved(position);
                                list.remove(position);
                            }else{
                                Toast.makeText(context, context.getResources().getString(R.string.operation_error), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            Log.e("apply", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        Log.e("applyError", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("patient_id")) {
                                Toast.makeText(context, data.getJSONArray("patient_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("center_id")) {
                                Toast.makeText(context, data.getJSONArray("center_id").toString(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView water,medicine,date;
        public ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            this.water = itemView.findViewById(R.id.water);
            this.medicine = itemView.findViewById(R.id.medicine);
            this.delete = itemView.findViewById(R.id.delete);
            this.date = itemView.findViewById(R.id.date);
        }
    }





}