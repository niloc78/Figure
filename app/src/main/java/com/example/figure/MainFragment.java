package com.example.figure;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    NavigationView sideBar;
    NavigationView sideBarFooter;
    View _rootView;
    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.context = (MainActivity) context;
        }
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
        }

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
        sideBar = view.findViewById(R.id.side_bar_nav_view);
        sideBarFooter = view.findViewById(R.id.side_bar_nav_view_footer);

        sideBar.getHeaderView(0).findViewById(R.id.side_bar_header_menu_icon_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerLayout) view.findViewById(R.id.side_bar_drawer)).closeDrawer(Gravity.LEFT);
            }
        });

        sideBar.getMenu().getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.cook_side_icon_selector_style));
        sideBar.getMenu().getItem(1).setIcon(ContextCompat.getDrawable(context, R.drawable.dine_side_icon_selector_style));
        sideBar.getMenu().getItem(2).setIcon(ContextCompat.getDrawable(context, R.drawable.delivery_side_icon_selector_style));
        sideBar.setItemIconTintList(null);
        sideBar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                for (int i = 0; i <= sideBarFooter.getMenu().size() - 1; i++) {
                    sideBarFooter.getMenu().getItem(i).setChecked(false);
                }
                item.setChecked(!item.isChecked());
                return true;
            }
        });

        sideBarFooter.getMenu().getItem(1).setIcon(ContextCompat.getDrawable(context, R.drawable.fitness_side_icon_selector_style));
        sideBarFooter.setItemIconTintList(null);

        sideBarFooter.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                for (int i = 0; i <= sideBar.getMenu().size() - 1; i++) {
                    sideBar.getMenu().getItem(i).setChecked(false);
                }
                item.setChecked(!item.isChecked());
                return true;
            }
        });
    }

}
