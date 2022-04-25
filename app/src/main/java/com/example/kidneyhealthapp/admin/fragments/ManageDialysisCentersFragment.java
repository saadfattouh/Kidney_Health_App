package com.example.kidneyhealthapp.admin.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.kidneyhealthapp.utils.Constants;
import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.admin.adapters.ManageDialysisCentersAdapter;
import com.example.kidneyhealthapp.model.Center;
import com.example.kidneyhealthapp.utils.SharedPrefManager;
import com.example.kidneyhealthapp.utils.Urls;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ManageDialysisCentersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    ManageDialysisCentersAdapter mAdapter;
    ArrayList<Center> list;
    RecyclerView mList;
    SearchView mSearchView;
    FloatingActionButton mAddCenterFAB;
    Context context;

    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressDialog pDialog;
    NavController navController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public ManageDialysisCentersFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_dialysis_centers, container, false);
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
        mAddCenterFAB = view.findViewById(R.id.add_btn);
        navController = Navigation.findNavController(view);
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Processing Please wait...");
        pDialog.setCancelable(false);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        mAddCenterFAB.setOnClickListener(v->{
            navController.navigate(R.id.action_centers_to_AddCenterFragment);
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
                                mAdapter = new ManageDialysisCentersAdapter(context, list);
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