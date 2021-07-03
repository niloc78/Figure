package com.example.figure;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class CookPreferencesFragment extends Fragment {
    Context context;
    View _rootView;
    RecyclerView mealTypeRecyclerView;
    RecyclerView cuisineTypeRecyclerView;
    RecyclerView.LayoutManager mealTypeLayoutManager;
    RecyclerView.LayoutManager cuisineTypeLayoutManager;
    LinkedHashMap<String, Integer> mealTypeData;
    LinkedHashMap<String, Integer> cuisineTypeData;
    PreferenceRecyclerAdapter mealTypeAdapter;
    PreferenceRecyclerAdapter cuisineTypeAdapter;

    //    public IngredientFragment() {
//        super(R.layout.ingred_frag_layout);
//    }
    @Override
    public void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
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

            _rootView = inflater.inflate(R.layout.cook_preferences_layout, container, false);

        }
        return _rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { // what happens after onCreateView
        super.onViewCreated(view, savedInstanceState);

        if (cuisineTypeRecyclerView == null) {
            initRecyclerViews(view);
            ((ImageButton) view.findViewById(R.id.cook_pref_back_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CookFragment) getParentFragment()).sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    ((MainFragment)getParentFragment().getParentFragment()).mainSideBarIcon.setVisibility(View.VISIBLE);

//                    scrim.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//                            scrim.setClickable(false);
//                            scrim.setBackgroundColor(Color.TRANSPARENT);
//                            ((MainFragment)getParentFragment()).mainSideBarIcon.setVisibility(View.VISIBLE);
//                        }
//                    });
//                    scrim.setClickable(false);
                }
            });

        }
    }

    public void initRecyclerViews(View view) {
        String[] mealTypeKeys = new String[]{"Breakfast", "Lunch", "Dinner", "Snacks", "Midnight"};
        final int[] mealTypeImages = new int[]{R.drawable.meal_type_breakfast, R.drawable.meal_type_lunch, R.drawable.meal_type_dinner,
                R.drawable.meal_type_snack, R.drawable.meal_type_midnight};
        mealTypeData = new LinkedHashMap<String, Integer>();
        for (int i = 0; i <= mealTypeKeys.length - 1; i++) {
            mealTypeData.put(mealTypeKeys[i], mealTypeImages[i]);
        }

        String[] cuisineTypeKeys = new String[]{
                "American", "British", "Chinese", "Eastern Europe", "French", "Indian", "Italian", "Japanese",
        "Kosher", "Mediterranean", "Mexican", "Middle Eastern", "South American"};
        final int[] cuisineTypeImages = new int[]{
                R.drawable.cuisine_type_american, R.drawable.cuisine_type_british, R.drawable.cuisine_type_chinese, R.drawable.cuisine_type_eastern_europe,
                R.drawable.cuisine_type_french, R.drawable.cuisine_type_indian, R.drawable.cuisine_type_italian, R.drawable.cuisine_type_japanese,
                R.drawable.cuisine_type_kosher, R.drawable.cuisine_type_mediterranean, R.drawable.cuisine_type_mexican, R.drawable.cuisine_type_middle_eastern,
                R.drawable.cuisine_type_south_american
        };
        cuisineTypeData = new LinkedHashMap<String, Integer>();
        for (int i = 0; i <= cuisineTypeKeys.length - 1; i++) {
            cuisineTypeData.put(cuisineTypeKeys[i], cuisineTypeImages[i]);
        }

        mealTypeRecyclerView = (RecyclerView) view.findViewById(R.id.meal_type_recycler_view);
        cuisineTypeRecyclerView = (RecyclerView) view.findViewById(R.id.cuisine_type_recycler_view);

        mealTypeLayoutManager = new GridLayoutManager(context, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        cuisineTypeLayoutManager = new GridLayoutManager(context, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };



        mealTypeRecyclerView.setLayoutManager(mealTypeLayoutManager);
        cuisineTypeRecyclerView.setLayoutManager(cuisineTypeLayoutManager);

        mealTypeAdapter = new PreferenceRecyclerAdapter(context,mealTypeData);
        cuisineTypeAdapter = new PreferenceRecyclerAdapter(context,cuisineTypeData);

        mealTypeRecyclerView.setAdapter(mealTypeAdapter);
        cuisineTypeRecyclerView.setAdapter(cuisineTypeAdapter);


    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    @Override
    public String toString () {
        return "cookPreferencesFragment";
    }
}
