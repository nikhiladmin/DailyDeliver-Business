package com.daytoday.business.dailydelivery.MainHomeScreen.View;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daytoday.business.dailydelivery.MainHomeScreen.Model.EmployeeInfo;
import com.daytoday.business.dailydelivery.MainHomeScreen.ViewModel.EmployeeViewModel;
import com.daytoday.business.dailydelivery.R;

import java.util.List;

public class EmployeeHomeFragment extends Fragment {
    RecyclerView emp_list;
    public EmployeeHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_employee_home, container, false);
        emp_list = view.findViewById(R.id.emp_list);
        emp_list.setHasFixedSize(true);
        emp_list.setLayoutManager(new LinearLayoutManager(getContext()));
        EmployeeViewModel employeeViewModel = new EmployeeViewModel();

        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        emp_list.addItemDecoration(itemDecor);
        employeeViewModel.getEmployee().observe(getActivity(), new Observer<List<EmployeeInfo>>() {
            @Override
            public void onChanged(List<EmployeeInfo> employeeInfos) {
                EmployeeAdapter employeeAdapter = new EmployeeAdapter(employeeInfos,view.getContext());
                emp_list.setAdapter(employeeAdapter);
            }
        });
        return view;
    }
}
