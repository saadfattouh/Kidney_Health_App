package com.example.kidneyhealthapp.fragments.patient;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidneyhealthapp.R;
import com.example.kidneyhealthapp.adapters.patient.DialysisCentersAdapter;
import com.example.kidneyhealthapp.model.Center;

import java.util.ArrayList;

public class DialysisCentersFragment extends Fragment {

    DialysisCentersAdapter mAdapter;
    ArrayList<Center> centers;
    RecyclerView mList;
    SearchView mSearchView;

    Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public DialysisCentersFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialysis_centers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList = view.findViewById(R.id.rv);
        mSearchView = view.findViewById(R.id.search);

        centers = new ArrayList<Center>()
        {{
            add(new Center(1, "dialysis center", 34.43543, 33.4352342, "this dialyses center is the best center in the world"));
            add(new Center(1, "dialysis center", 34.43543, 33.4352342, "this dialyses center is the best center in the world"));
            add(new Center(1, "dialysis center", 34.43543, 33.4352342, "this dialyses center is the best center in the world"));
            add(new Center(1, "dialysis center", 34.43543, 33.4352342, "this dialyses center is the best center in the world"));
            add(new Center(1, "dialysis center", 34.43543, 33.4352342, "this dialyses center is the best center in the world"));
            add(new Center(1, "dialysis center", 34.43543, 33.4352342, "this dialyses center is the best center in the world"));
            add(new Center(1, "dialysis center", 34.43543, 33.4352342, "this dialyses center is the best center in the world"));
        }};

        mAdapter = new DialysisCentersAdapter(context, centers);
        mList.setAdapter(mAdapter);

        mSearchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}