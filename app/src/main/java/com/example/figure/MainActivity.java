package com.example.figure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements FragmentChangeListener {
    public static ViewPager viewPager;

    public MainActivity() {
        super(R.layout.activity_main);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
//            DashFragment dashFrag = new DashFragment();
//
//            getSupportFragmentManager().beginTransaction()
//                    .setReorderingAllowed(true)
//                    .add(R.id.fragment_container, dashFrag, dashFrag.toString())
//                    .commit();
            viewPager = (ViewPager) findViewById(R.id.pager);
            final MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager(), 2);

            viewPager.setAdapter(adapter);

        }
    }


    @Override
    public void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        fragmentTransaction.detach(fragmentManager.findFragmentByTag("dashFragment"));
//        fragmentTransaction.add(R.id.fragment_container, fragment, "mainFragment");
//        fragmentTransaction.addToBackStack("transition");
//
//        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            moveTaskToBack(true);
        } else {
            super.onBackPressed();
        }

    }

}