package com.example.kidneyhealthapp.patient.adapters;

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
import com.example.kidneyhealthapp.utils.Constants;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.model.Center;
import com.example.kidneyhealthapp.model.LatLon;
import com.example.kidneyhealthapp.utils.GPSTools;
import com.example.kidneyhealthapp.utils.SharedPrefManager;
import com.example.kidneyhealthapp.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DialysisCentersAdapter extends RecyclerView.Adapter<DialysisCentersAdapter.ViewHolder> implements Filterable {

    Context context;
    private List<Center> centers;
    public NavController navController;
    private List<Center> centersFiltered;
    private List<Center> nearestCentersFiltered;
    private SharedPrefManager prefManager;
    private int patientId;
    ProgressDialog pDialog;

    // RecyclerView recyclerView;
    public DialysisCentersAdapter(Context context, ArrayList<Center> centers) {
        this.context = context;
        this.centers = centers;
        this.centersFiltered = centers;
        this.nearestCentersFiltered = centers;
        prefManager = SharedPrefManager.getInstance(context);
        patientId = prefManager.getUserId();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_center, parent, false);

        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Center center = centers.get(position);

        holder.centerNameTV.setText(center.getName());

        holder.doctorNameTV.setText(center.getDoctorName());

        LatLon patientLocation = prefManager.getUserLocation();

        double distance = GPSTools.distance(center.getLat(), patientLocation.getLat(), center.getLon(), patientLocation.getLon());
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage("Processing please wait ...");
        int d = (int) distance;
        holder.distanceTV.setText(String.valueOf(d) + " km");

        holder.itemView.setOnClickListener(v -> {
            LayoutInflater factory = LayoutInflater.from(context);
            final View view = factory.inflate(R.layout.dialog_center_info, null);
            final AlertDialog centerInfoDialog = new AlertDialog.Builder(context).create();
            centerInfoDialog.setView(view);

            TextView ok = view.findViewById(R.id.ok);
            TextView name = view.findViewById(R.id.tv_name);
            TextView details = view.findViewById(R.id.details);

            name.setText(center.getName());
            details.setText(center.getInfo());

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    centerInfoDialog.dismiss();
                }
            });

            centerInfoDialog.show();
        });

        holder.makeAppointmentBtn.setOnClickListener(v -> {
            LayoutInflater factory = LayoutInflater.from(context);
            final View view = factory.inflate(R.layout.dialog_confirm_appointment, null);
            final AlertDialog appointmentConfirmationDialog = new AlertDialog.Builder(context).create();
            appointmentConfirmationDialog.setView(view);

            TextView yes = view.findViewById(R.id.yes_btn);
            TextView no = view.findViewById(R.id.no_btn);

            yes.setOnClickListener(v1 -> {
                bookAppointment(patientId, center.getId());
                appointmentConfirmationDialog.dismiss();
            });

            no.setOnClickListener(v12 ->
                    appointmentConfirmationDialog.dismiss()
            );
            appointmentConfirmationDialog.show();
        });
    }


    //todo api call (book an appointment)
    private void bookAppointment(int patientId, int centerId) {
        pDialog.show();
        String url = Urls.MAKE_APPOINTMENT;
        String userId = String.valueOf(SharedPrefManager.getInstance(context).getUserId());

        AndroidNetworking.post(url)
                .addBodyParameter("patient_id", userId)
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
                            String userFounded = "Saved";
                            //if no error in response
                            if (message.toLowerCase().contains(userFounded.toLowerCase())) {
                                Toast.makeText(context, context.getResources().getString(R.string.appointment_success), Toast.LENGTH_SHORT).show();
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
        return centers.size();
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

                        for(Center center: centers){
                            if(center.getName().toLowerCase().contains(searchString)){
                                resultData.add(center);
                            }
                        }
                        filterResults.count = resultData.size();
                        filterResults.values = resultData;
                    }
                }else{
//                        List<Center> resultData = new ArrayList<>();

                    for(Center center: nearestCentersFiltered){
                       SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(context);
                       LatLon userLocation = sharedPrefManager.getUserLocation();
                       center.setDistance(GPSTools.distance(center.getLat(),userLocation.getLat(),center.getLon(), userLocation.getLon()));
                    }

                    Collections.sort(nearestCentersFiltered, Center.DISTANCECOMPARE);

                    filterResults.count = nearestCentersFiltered.size();
                    filterResults.values = nearestCentersFiltered;
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
        public TextView distanceTV;
        public Button makeAppointmentBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            this.centerNameTV = itemView.findViewById(R.id.center_name);
            this.doctorNameTV = itemView.findViewById(R.id.doctor_name);
            this.distanceTV = itemView.findViewById(R.id.distance);
            this.makeAppointmentBtn = itemView.findViewById(R.id.make_appointment);
        }
    }
}