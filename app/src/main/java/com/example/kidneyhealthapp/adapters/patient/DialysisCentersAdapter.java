package com.example.kidneyhealthapp.adapters.patient;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.example.kidneyhealthapp.model.Center;
import com.example.kidneyhealthapp.utils.SharedPrefManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class DialysisCentersAdapter extends RecyclerView.Adapter<DialysisCentersAdapter.ViewHolder> implements Filterable {

    Context context;
    private List<Center> centers;
    public NavController navController;
    private boolean selectable;
    OnDonationSelected mSelectionListener;

    private List<Center> centersFiltered;

    // RecyclerView recyclerView;
    public DialysisCentersAdapter(Context context, ArrayList<Center> centers, boolean selectable) {
        this.context = context;
        this.centers = centers;
        this.centersFiltered = centers;
        this.selectable = selectable;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_center, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        if(context instanceof DialysisCentersAdapter.OnDonationSelected){
            mSelectionListener = (DialysisCentersAdapter.OnDonationSelected) context;
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        mSelectionListener = null;
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Center center = centers.get(position);

        holder.centerNameTV.setText(center.getName());

        holder.doctorNameTV.setText(center.getDoctorName());

        holder.locationTV.setText(String.valueOf(center.getLat()) + String.valueOf(center.getLon()));


            holder.makeAppointmentBtn.setOnClickListener(v -> {
                LayoutInflater factory = LayoutInflater.from(context);
                final View view = factory.inflate(R.layout.dialog_confirm_appointment, null);
                final AlertDialog appointmentConfirmationDialog = new AlertDialog.Builder(context).create();
                appointmentConfirmationDialog.setView(view);

                TextView yes = view.findViewById(R.id.yes_btn);
                TextView no = view.findViewById(R.id.no_btn);

                yes.setOnClickListener(v1 -> appointmentConfirmationDialog.dismiss());

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appointmentConfirmationDialog.dismiss();
                    }
                });
                appointmentConfirmationDialog.show();
            });
    }




    @Override
    public int getItemCount() {
        return centers.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                if(charSequence == null | charSequence.length() == 0){
                    filterResults.count = centersFiltered.size();
                    filterResults.values = centersFiltered;

                }else{
                    String searchChr = charSequence.toString().toLowerCase();
                    String selectedCategory =  searchChr.split(":")[1];
                    String search =  searchChr.split(":")[0];

                    Log.e("filter", selectedCategory);
                    List<Center> resultData = new ArrayList<>();

                    if(search.equals("")){
                        for(Center center: centersFiltered){
                            if(true){

                                resultData.add(center);
                            }
                        }
                    }else{
                        for(Center center: centers){
                            if(true){
                                resultData.add(center);
                            }
                        }
                    }

                    filterResults.count = resultData.size();
                    filterResults.values = resultData;

                }

                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                centers = (List<Center>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView centerNameTV;
        public TextView doctorNameTV;
        public TextView locationTV;
        public TextView distanceTV;
        public Button makeAppointmentBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            this.centerNameTV = itemView.findViewById(R.id.center_name);
            this.doctorNameTV = itemView.findViewById(R.id.doctor_name);
            this.locationTV = itemView.findViewById(R.id.location);
            this.distanceTV = itemView.findViewById(R.id.distance);
            this.makeAppointmentBtn = itemView.findViewById(R.id.make_appointment);
        }
    }


    public interface OnDonationSelected{
        void onCenterSelected(String desc);
    }

    private void orderItem(int id) {

        int userId = SharedPrefManager.getInstance(context).getUserId();

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Processing Please wait...");
        pDialog.show();

        AndroidNetworking.post("Urls.GET_DONATIONS")
                .addBodyParameter("user_id", String.valueOf(userId))
                .addBodyParameter("donation_id", String.valueOf(id))
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
                            if (obj.getInt("status") == 1) {

                                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();

                            } else if(obj.getInt("status") == -1){
                                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        Toast.makeText(context, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


}