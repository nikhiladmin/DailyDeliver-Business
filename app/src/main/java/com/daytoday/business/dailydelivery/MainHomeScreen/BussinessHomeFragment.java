package com.daytoday.business.dailydelivery.MainHomeScreen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daytoday.business.dailydelivery.R;

import java.util.ArrayList;
import java.util.List;


public class BussinessHomeFragment extends Fragment {

    RecyclerView recyclerView;
    BussinessAdapter bussinessAdapter;

    List<Bussiness> bussinessList;


    public BussinessHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_bussiness_home, container, false);

        bussinessList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.buss_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        BussinessViewModel bussinessViewModel = new BussinessViewModel();
        bussinessViewModel.getBussiness().observe(getActivity(), new Observer<List<Bussiness>>() {
            @Override
            public void onChanged(List<Bussiness> bussinesses) {
                bussinessAdapter = new BussinessAdapter(view.getContext(), bussinesses);
                recyclerView.setAdapter(bussinessAdapter);

            }
        });



        return view;
    }
}
