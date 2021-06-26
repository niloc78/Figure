package com.example.figure;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainFragment extends Fragment {

    NavigationView sideBar;
    NavigationView sideBarFooter;
    View _rootView;
    Context context;
    FragmentManager childFragmentManager;
    ImageButton mainSideBarIcon;
    public static CustomViewPager modePager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Toast.makeText(context, "oncreate called", Toast.LENGTH_SHORT).show();


    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            Toast.makeText(context, "onattach called", Toast.LENGTH_SHORT).show();
            this.context = (MainActivity) context;
        }


    }

    private FragmentManager childFragmentManager() {
        if(childFragmentManager == null) {
            childFragmentManager = getChildFragmentManager();
        }
        return childFragmentManager;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        if (_rootView == null) {
            _rootView = inflater.inflate(R.layout.main_frag_layout, parent, false); // contains all the dash saved state/makes new if null
        }

        return _rootView; //load in again
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { // what happens after onCreateView
        super.onViewCreated(view, savedInstanceState);
        if (sideBar == null && sideBarFooter == null) {
            initSideBar(view);
            initModePager(view);
        } else {
//            FragmentTransaction transaction = childFragmentManager().beginTransaction();
//            transaction.add(R.id.mode_fragment_container, cookFrag, cookFrag.toString())
//                    .addToBackStack("cookFragment").commit();
        }

    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//    }

    public void initModePager(View view) {
        modePager = (CustomViewPager) view.findViewById(R.id.modePager);
        final ModePagerAdapter adapter = new ModePagerAdapter(getChildFragmentManager(), 3);
        modePager.setAdapter(adapter);
        modePager.setOffscreenPageLimit(2);
        modePager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        modePager.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                return true;
            }
        });
        modePager.requestDisallowInterceptTouchEvent(true);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }
    @Override
    public String toString () {
        return "mainFragment";
    }

    public void initSideBar(View view) {
        ((DrawerLayout) view.findViewById(R.id.side_bar_drawer)).setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mainSideBarIcon = (ImageButton) view.findViewById(R.id.main_side_bar_icon);
        mainSideBarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerLayout) view.findViewById(R.id.side_bar_drawer)).openDrawer(Gravity.LEFT);
            }
        });
        sideBar = view.findViewById(R.id.side_bar_nav_view);
        sideBarFooter = view.findViewById(R.id.side_bar_nav_view_footer);

        sideBar.getHeaderView(0).findViewById(R.id.side_bar_header_menu_icon_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerLayout) view.findViewById(R.id.side_bar_drawer)).closeDrawer(Gravity.LEFT);
            }
        });

        sideBar.getMenu().getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.cook_side_icon_selector_style)).setChecked(true); //default cook
        sideBar.getMenu().getItem(1).setIcon(ContextCompat.getDrawable(context, R.drawable.dine_side_icon_selector_style));
        sideBar.getMenu().getItem(2).setIcon(ContextCompat.getDrawable(context, R.drawable.delivery_side_icon_selector_style));
        sideBar.setItemIconTintList(null);
        sideBar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.isChecked()) {}
                else {
                    for (int i = 0; i <= sideBarFooter.getMenu().size() - 1; i++) {
                        sideBarFooter.getMenu().getItem(i).setChecked(false);
                    }
                    item.setChecked(!item.isChecked());
                    switch(item.getItemId()) {
                        case R.id.cook_menu_item:
                            modePager.setCurrentItem(0);
                            return true;
                        case R.id.dine_menu_item:
                            modePager.setCurrentItem(1);
                            return true;
                        case R.id.delivery_menu_item:
                            modePager.setCurrentItem(2);
                            return true;
                        default:
                            return true;
                    }

                }
                return true;
            }
        });

        sideBarFooter.getMenu().getItem(1).setIcon(ContextCompat.getDrawable(context, R.drawable.fitness_side_icon_selector_style));
        sideBarFooter.setItemIconTintList(null);

        sideBarFooter.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.isChecked()) {}
                else {
                    for (int i = 0; i <= sideBar.getMenu().size() - 1; i++) {
                        sideBar.getMenu().getItem(i).setChecked(false);
                    }
                    item.setChecked(!item.isChecked());
                }
                return true;
            }
        });
    }

}
