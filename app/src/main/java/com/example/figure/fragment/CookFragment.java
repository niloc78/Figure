package com.example.figure.fragment;

import android.app.Activity;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.figure.adapter.CookPagerAdapter;
import com.example.figure.view.CustomViewPager;
import com.example.figure.MainActivity;
import com.example.figure.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

public class CookFragment extends Fragment {

    View _rootView;
    Context context;
    public static CustomViewPager cookPager;
    TabLayout cookTabLayout;

    public BottomSheetBehavior sheetBehavior;

//    public CookFragment() {
//        super(R.layout.cook_frag_layout);
//    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.context = (MainActivity) context;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (_rootView == null) {
            Toast.makeText(context, "rootview was null", Toast.LENGTH_SHORT).show();
            _rootView = inflater.inflate(R.layout.cook_frag_layout, container, false);
            // contains all the dash saved state/makes new if null
        }
        Toast.makeText(context, "child oncreateview called", Toast.LENGTH_SHORT).show();
        return _rootView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { // what happens after onCreateView
        super.onViewCreated(view, savedInstanceState);
        if (cookPager == null) {
            initCookPager(view);
            initCookPref(view);
        }
    }

    public void initCookPager(View view) {

        cookPager = (CustomViewPager) view.findViewById(R.id.cook_pager);
        final CookPagerAdapter adapter = new CookPagerAdapter(getChildFragmentManager(), 2);
        cookPager.setAdapter(adapter);
        cookPager.setOnTouchListener((v, event) -> true);
        cookPager.setOnDragListener((v, event) -> true);
        cookPager.requestDisallowInterceptTouchEvent(true);

        cookTabLayout = (TabLayout) view.findViewById(R.id.cook_tab_layout);
        cookTabLayout.setupWithViewPager(cookPager, true);
        final int[] icons = new int[]{
                R.drawable.ingred_icon_selector_style, R.drawable.recipe_icon_selector_style
        };

        for (int i = 0; i <= icons.length - 1; i++) {
            View v = getLayoutInflater().inflate(R.layout.customtab, null);
            TabLayout.Tab tab = cookTabLayout.getTabAt(i);
            v.findViewById(R.id.tabIcon).setBackgroundResource(icons[i]);
            if (tab != null) {
                tab.setCustomView(v);
            }
        }
//        cookTabLayout.getTabAt(0).setIcon(icons[0]);
//        cookTabLayout.getTabAt(1).setIcon(icons[1]);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    public void initCookPref(View view) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        trans.add(R.id.cook_pref_container, new CookPreferencesFragment(), "cookPreferencesFragment");
        trans.commit();

        sheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.cook_pref_view));

        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        sheetBehavior.setDraggable(false);


//
//        ((ImageButton) view.findViewById(R.id.cook_pref_button)).bringToFront();
//        ((ImageButton) view.findViewById(R.id.cook_pref_button)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                scrim.setClickable(true);
//                scrim.setBackgroundColor(ContextCompat.getColor(context, R.color.main_pink_alpha));
//            }
//        });
    }

    @Override
    public String toString () {
        return "cookFragment";
    }

    public BottomSheetBehavior getSheetBehavior() {
        return sheetBehavior;
    }
}
