package com.example.figure;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CookPagerAdapter extends FragmentStatePagerAdapter {

    int numTabs;

    public CookPagerAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.numTabs = numTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                IngredientFragment ingredFragment = new IngredientFragment();
                return ingredFragment;
            case 1:
                RecipeFragment recipeFrag = new RecipeFragment();
                return recipeFrag;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
