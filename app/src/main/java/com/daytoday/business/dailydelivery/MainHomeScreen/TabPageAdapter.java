package com.daytoday.business.dailydelivery.MainHomeScreen;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/*
This ia the adapter to inflate the Tablayout Fragment section in the app Home Fragment and creates two sections My Bussiness and My Employees
*/
public class TabPageAdapter extends FragmentStatePagerAdapter {

    public TabPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new BussinessHomeFragment();
        } else if (position == 1) {
            fragment = new EmployeeHomeFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
