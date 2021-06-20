package com.example.figure;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DashFragment extends Fragment {
    Context context;
    ViewPager dashPager;
    DashPagerAdapter dashPagerAdapter;
    List<DashModel> models;
    TabLayout dashDotLayout;
    Button skip;
    View _rootView;
    MainFragment mainFrag;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.context = (MainActivity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        models = new ArrayList<DashModel>();
        dashPagerAdapter = new DashPagerAdapter(models, context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        if (_rootView == null) {
            _rootView = inflater.inflate(R.layout.dash_fragment_layout, parent, false); // contains all the dash saved state/makes new if null
        }

        return _rootView; //load in again
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { // what happens after onCreateView
            super.onViewCreated(view, savedInstanceState);
            if (dashPager == null) { // single condition which determines whether or not the fragment was just created.. certain instance variables would be null
                initDash(view);
            }

    }
    public void initDash(View v) {

        models.add(new DashModel(R.drawable.dash_cook_icon));
        models.add(new DashModel(R.drawable.dash_dine_icon));
        models.add(new DashModel(R.drawable.dash_delivery_icon));


        dashPager = (ViewPager) v.findViewById(R.id.dash_pager);

        dashPager.setAdapter(dashPagerAdapter);

        dashDotLayout = (TabLayout) v.findViewById(R.id.dash_tab_dots);
        dashDotLayout.setupWithViewPager(dashPager, true);
        skip = (Button) v.findViewById(R.id.skip_button);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "skip clicked", Toast.LENGTH_SHORT).show();
                skip();
            }
        });
    }
    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }
    @Override
    public String toString () {
        return "dashFragment";
    }

    public void skip() {
        if (mainFrag == null) {
            mainFrag = new MainFragment();
        }
        FragmentChangeListener fcListener = (FragmentChangeListener) getActivity();
        fcListener.replaceFragment(mainFrag);
    }

}
