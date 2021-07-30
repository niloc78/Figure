package com.example.figure.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.figure.fragment.CookFragment;
import com.example.figure.fragment.DeliveryFragment;
import com.example.figure.fragment.DineFragment;
import com.example.figure.fragment.ProfileFragment;

public class ModePagerAdapter extends FragmentStatePagerAdapter {
    int numTabs;

    public ModePagerAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.numTabs = numTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                CookFragment cookFrag = new CookFragment();
                return cookFrag;
            case 1:
                DineFragment dineFrag = new DineFragment();
                return dineFrag;
            case 2:
                DeliveryFragment deliveryFrag = new DeliveryFragment();
                return deliveryFrag;
            case 3:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }

}
