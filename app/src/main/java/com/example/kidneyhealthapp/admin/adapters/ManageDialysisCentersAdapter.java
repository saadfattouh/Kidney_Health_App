package com.example.kidneyhealthapp.admin.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kidneyhealthapp.utils.Constants;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.model.Center;
import com.example.kidneyhealthapp.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ManageDialysisCentersAdapter extends RecyclerView.Adapter<ManageDialysisCentersAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "centerM";
    Context context;
    private List<Center> list;
    public NavController navController;
    private List<Center> centersFiltered;
    ProgressDialog pDialog;

    // RecyclerView recyclerView;
    public ManageDialysisCentersAdapter(Context context, ArrayList<Center> list) {
        this.context = context;
        this.list = list;
        this.centersFiltered = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_admin_center, parent, false);
        navController = Navigation.findNavController(parent);

        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Center center = list.get(position);

        holder.centerNameTV.setText(center.getName());

        holder.doctorNameTV.setText(center.getDoctorName());

        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");


        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("center", center);
            navController.navigate(R.id.action_centers_to_centerDetailsFragment, bundle);
        });

        holder.update.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("center", center);
            navController.navigate(R.id.action_centers_to_updateCenterFragment, bundle);
        });

        holder.delete.setOnClickListener(v->{
            LayoutInflater factory = LayoutInflater.from(context);
            final View view = factory.inflate(R.layout.dialog_delete, null);
            final AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
            deleteDialog.setView(view);

            TextView ok = view.findViewById(R.id.ok);
            TextView cancel = view.findViewById(R.id.cancel);

            Log.e("id", String.valueOf(center.getId()));

            ok.setOnClickListener(v1 ->{
                deleteCenter(center.getId(), position);
                deleteDialog.dismiss();
            });
            cancel.setOnClickListener(v1 ->
                    deleteDialog.dismiss()
            );
            deleteDialog.show();        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                String request = charSequence.toString().split(":")[1];
                String searchString = charSequence.toString().split(":")[0];
                if(request.equals(Constants.SEARCH)){
                    if(searchString == null | searchString.length() == 0){
                        filterResults.count = centersFiltered.size();
                        filterResults.values = centersFiltered;

                    }else{

                        List<Center> resultData = new ArrayList<>();

                        for(Center center: list){
                            if(center.getName().toLowerCase().contains(searchString)){
                                resultData.add(center);
                            }
                        }
                        filterResults.count = resultData.size();
                        filterResults.values = resultData;
                    }
                }
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (List<Center>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView centerNameTV;
        public TextView doctorNameTV;
        public Button update;
        public ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            this.centerNameTV = itemView.findViewById(R.id.center_name);
            this.doctorNameTV = itemView.findViewById(R.id.doctor_name);
            this.update = itemView.findViewById(R.id.update_center);
            this.delete = itemView.findViewById(R.id.delete);
        }
    }

    private void deleteCenter(int centerId, int position) {
        pDialog.show();
        String url = Urls.DELETE_CENTER;

        AndroidNetworking.post(url)
                .addBodyParameter("center_id", String.valueOf(centerId))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting response to json object
                            JSONObject obj = response;
                            String message = obj.getString("message");
                            String successMessage = "Deleted";
                            //if no error in response
                            if (message.toLowerCase().contains(successMessage.toLowerCase())) {
                                Toast.makeText(context, context.getResources().getString(R.string.center_deleted_success), Toast.LENGTH_SHORT).show();
                                notifyItemRemoved(position);
                                list.remove(position);
                            } else{
                                Toast.makeText(context, context.getResources().getString(R.string.operation_error), Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismiss();
                            Log.e(TAG+"c", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        Log.e(TAG + "e", anError.getErrorBody());
                        try {
                            JSONObject error = new JSONObject(anError.getErrorBody());
                            JSONObject data = error.getJSONObject("data");
                            Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
                            if (data.has("patient_id")) {
                                Toast.makeText(context, data.getJSONArray("patient_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (data.has("doctor_id")) {
                                Toast.makeText(context, data.getJSONArray("center_id").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}