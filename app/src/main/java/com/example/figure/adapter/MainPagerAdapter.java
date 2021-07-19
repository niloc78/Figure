package com.example.figure.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.figure.fragment.DashFragment;
import com.example.figure.fragment.MainFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    int numTabs;

    public MainPagerAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.numTabs = numTabs;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                DashFragment dashFrag = new DashFragment();
                return dashFrag;
            case 1:
                MainFragment mainFrag = new MainFragment();
                return mainFrag;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
