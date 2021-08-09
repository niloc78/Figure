package com.example.figure.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.figure.fragment.CookFragment;
import com.example.figure.fragment.OrderFragment;
import com.example.figure.fragment.ProfileFragment;

public class ModePagerAdapter extends FragmentStatePagerAdapter {
    int numTabs;
    public ProfileFragment profileFrag;

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
                OrderFragment dineFrag = new OrderFragment();
                return dineFrag;
            case 2:
                ProfileFragment profileFragment = new ProfileFragment();
                profileFrag = profileFragment;
                return profileFragment;
            default:
                return null;
        }
    }

    public ProfileFragment getProfileFrag() {
        return profileFrag;
    }

    @Override
    public int getCount() {
        return numTabs;
    }

}
