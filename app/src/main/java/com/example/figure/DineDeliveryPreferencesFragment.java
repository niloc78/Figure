package com.example.figure;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.slider.Slider;

import org.w3c.dom.Text;

import java.util.LinkedHashMap;

public class DineDeliveryPreferencesFragment extends Fragment {

    Context context;
    View _rootView;
    String mode;
    TextView radiusTextView;
    Slider radiusSlider;
    RecyclerView cuisineTypeRecyclerView;
    RecyclerView.LayoutManager cuisineTypeLayoutManager;
    LinkedHashMap<String, Integer> cuisineTypeData;
    PreferenceRecyclerAdapter cuisineTypeAdapter;

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
            _rootView = inflater.inflate(R.layout.dine_delivery_preferences_layout, container, false);
            if (getParentFragment() instanceof DineFragment) {
                mode = "Dine";
                Log.d(mode + " preferences created", "created");
            } else if (getParentFragment() instanceof DeliveryFragment) {
                mode = "Delivery";
                Log.d(mode + " preferences created", "created");
            }

        }
        return _rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { // what happens after onCreateView
        super.onViewCreated(view, savedInstanceState);
        if (cuisineTypeRecyclerView == null) {
            ImageButton backButton = (ImageButton) view.findViewById(R.id.dine_delivery_pref_back_button);

            if (mode.equalsIgnoreCase("Dine")) {
                backButton.setOnClickListener(v -> {
                    ((DineFragment) getParentFragment()).sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    ((MainFragment) getParentFragment().getParentFragment()).mainSideBarIcon.setVisibility(View.VISIBLE);
                });
            } else if (mode.equalsIgnoreCase("Delivery")) {
                backButton.setOnClickListener(v -> {
                    ((DeliveryFragment) getParentFragment()).sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    ((MainFragment) getParentFragment().getParentFragment()).mainSideBarIcon.setVisibility(View.VISIBLE);
                });
            }


            initViewsAndButtons(view);
            initRecyclerViews(view);


        }



    }


    public void initViewsAndButtons(View view) {
        Typeface face = ResourcesCompat.getFont(context, R.font.josefinsans);
        ((TextView) view.findViewById(R.id.searching_up_to)).setTypeface(face);
        ((TextView) view.findViewById(R.id.mile_num)).setTypeface(face);
        ((TextView) view.findViewById(R.id.miles_away)).setTypeface(face);
        ((TextView) view.findViewById(R.id.preferences_text)).setTypeface(face);

        ((TextView) view.findViewById(R.id.restaurant_prices)).setTypeface(face);
        ((RadioButton) view.findViewById(R.id.rad1)).setTypeface(face);
        ((RadioButton) view.findViewById(R.id.rad2)).setTypeface(face);
        ((RadioButton) view.findViewById(R.id.rad3)).setTypeface(face);
        ((TextView) view.findViewById(R.id.update_preferences)).setTypeface(face);

    }

    public void initRecyclerViews(View view) {
        String[] cuisineTypeKeys = new String[]{
                "American", "British", "Chinese", "French", "Indian", "Italian", "Japanese",
                "Kosher", "Mediterranean", "Mexican", "Middle Eastern", "South American"};
        final int[] cuisineTypeImages = new int[]{
                R.drawable.cuisine_type_american, R.drawable.cuisine_type_british, R.drawable.cuisine_type_chinese,
                R.drawable.cuisine_type_french, R.drawable.cuisine_type_indian, R.drawable.cuisine_type_italian, R.drawable.cuisine_type_japanese,
                R.drawable.cuisine_type_kosher, R.drawable.cuisine_type_mediterranean, R.drawable.cuisine_type_mexican, R.drawable.cuisine_type_middle_eastern,
                R.drawable.cuisine_type_south_american
        };
        cuisineTypeData = new LinkedHashMap<String, Integer>();
        for (int i = 0; i <= cuisineTypeKeys.length - 1; i++) {
            cuisineTypeData.put(cuisineTypeKeys[i], cuisineTypeImages[i]);
        }
        cuisineTypeRecyclerView = (RecyclerView) view.findViewById(R.id.dine_delivery_pref_cuisine_type_recycler_view);

        cuisineTypeLayoutManager = new GridLayoutManager(context, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        cuisineTypeRecyclerView.setLayoutManager(cuisineTypeLayoutManager);
        cuisineTypeAdapter = new PreferenceRecyclerAdapter(context,cuisineTypeData);
        cuisineTypeRecyclerView.setAdapter(cuisineTypeAdapter);
        radiusSlider = (Slider) view.findViewById(R.id.radius_slider);
        radiusTextView = (TextView) view.findViewById(R.id.mile_num);

        radiusSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                radiusTextView.setText(Float.toString(radiusSlider.getValue()));
            }
        });





    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    public String getMode() {
        return mode;
    }

    @Override
    public String toString () {
        return "dineDeliveryPreferencesFragment";
    }



}
