package com.example.figure;

import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

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
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }

}
