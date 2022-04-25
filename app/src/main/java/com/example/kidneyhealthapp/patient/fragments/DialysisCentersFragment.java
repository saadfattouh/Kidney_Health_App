package com.example.kidneyhealthapp.patient.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kidneyhealthapp.utils.Constants;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.utils.Urls;
import com.example.kidneyhealthapp.patient.adapters.DialysisCentersAdapter;
import com.example.kidneyhealthapp.model.Center;
import com.example.kidneyhealthapp.utils.GpsTracker;
import com.example.kidneyhealthapp.utils.SharedPrefManager;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class DialysisCentersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    DialysisCentersAdapter mAdapter;
    ArrayList<Center> list;
    RecyclerView mList;
    SearchView mSearchView;
    Button mViewNearest , mViewAll;
    Context context;

    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressDialog pDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public DialysisCentersFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialysis_centers, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            getCenters();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.e("id", String.valueOf(SharedPrefManager.getInstance(context).getUserId()));
        mList = view.findViewById(R.id.rv);
        mSearchView = view.findViewById(R.id.search);
        mViewNearest = view.findViewById(R.id.nearest);
        mViewAll = view.findViewById(R.id.all);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);

        mSearchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText + ":" + Constants.SEARCH);
                return true;

            }
        });

        mViewAll.setOnClickListener(v->{
            mAdapter.getFilter().filter("" + ":" + Constants.SEARCH);
        });
        mViewNearest.setOnClickListener(v->{
            GpsTracker gpsTracker= new GpsTracker(getActivity().getApplicationContext());
            Location location = gpsTracker.getLocation();
            Log.e("location",location.getLatitude() + "   :  " +location.getLongitude());

            while(location.getLatitude() == 0.0 | location.getLongitude() == 0.0){
                location = gpsTracker.getLocation();
                Log.e("gps tracker", location.getLatitude() + " : "+ gpsTracker.getLongitude());
            }
            SharedPrefManager.getInstance(context).setUserLocation(location.getLatitude(), location.getLongitude());
            mAdapter.getFilter().filter("query" + ":"+Constants.NEAREST);
        });
    }

    private void getCenters() {
        String url = Urls.GET_CENTERS;
        pDialog.show();
        list = new ArrayList<Center>();

        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String messageGot = "Success";
                            String message = response.getString("message");
                            if (message.toLowerCase().contains(messageGot.toLowerCase())) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);

                                    list.add(
                                            new Center(
                                                    Integer.parseInt(obj.getString("id")),
                                                    obj.getString("name"),
                                                    obj.getDouble("lat"),
                                                    obj.getDouble("lon"),
                                                    obj.getString("location"),
                                                    obj.getString("info")
                                            )
                                    );
                                }
                                mAdapter = new DialysisCentersAdapter(context, list);
                                mList.setAdapter(mAdapter);
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                            mSwipeRefreshLayout.setRefreshing(false);
                            pDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            mSwipeRefreshLayout.setRefreshing(false);
                            pDialog.dismiss();
                            Log.e("replies catch", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        pDialog.dismiss();
                        Log.e("replies anerror", error.getErrorBody());
                    }
                });
    }
    @Override
    public void onRefresh() {
        getCenters();
    }
}