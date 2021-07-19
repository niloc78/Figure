package com.example.figure.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.figure.MainActivity;
import com.example.figure.R;
import com.example.figure.data.Recipe;
import com.example.figure.model.RecipeModel;
import com.example.figure.adapter.RecipePagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class RecipeHolderFragment extends Fragment {
    Context context;
    View _rootView;
    ViewPager2 recipePager;
    ArrayList<Recipe> recipeData;
    RecipePagerAdapter adapter;
    private boolean recipesLoaded = false;
    TabLayout tabLayout;

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

            _rootView = inflater.inflate(R.layout.recipe_holder_frag_layout, container, false);

        }
        return _rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { // what happens after onCreateView
        super.onViewCreated(view, savedInstanceState);
            if (recipePager == null) {
                tabLayout = view.findViewById(R.id.recipe_tabs);
                recipePager = (ViewPager2) view.findViewById(R.id.recipe_pager);

                ((Button)view.findViewById(R.id.test_remove_button)).setOnClickListener(v -> {
                    adapter.clearFragments();
                });
            }

            getParentFragmentManager().setFragmentResultListener("recipesLoaded", this, new FragmentResultListener() {
                @Override
                public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
//                    RecipeModel recipeModel = new ViewModelProvider(requireActivity()).get(RecipeModel.class);
//                    recipeData = recipeModel.sort();
//                    adapter = new RecipePagerAdapter(getActivity(), recipeData);
//                    recipePager.setAdapter(adapter);
                    recipesLoaded = result.getBoolean("loaded");
                    if (recipesLoaded) {
                        initRecipePager(view);
                    } else {
                        unload();
                    }


//                    recipesLoaded = result.getBoolean("loaded");
//                    Log.d("recipes loaded", "loaded");
//                    RecipeModel recipeModel = new ViewModelProvider(requireActivity()).get(RecipeModel.class);
//                    recipeData = recipeModel.sort();
//                    Log.d("test recipe callback", recipeData.get(0).getRecipeName());
//                    adapter.notifyDataSetChanged();
                }
            });

    }
    public boolean unload() {
        if(adapter != null) {
            adapter.clearFragments();
            recipeData.clear();
        }
//        tabLayout.removeAllTabs();
//        recipePager.setAdapter(null);
        return true;
    }

    public void initRecipePager(View view) {
//        tabLayout = view.findViewById(R.id.recipe_tabs);
//        recipePager = (ViewPager2) view.findViewById(R.id.recipe_pager);
        RecipeModel recipeModel = new ViewModelProvider(requireActivity()).get(RecipeModel.class);
        //recipeData = new ArrayList<Recipe>();

        if (adapter == null) {
            recipeData = recipeModel.sort();
            adapter = new RecipePagerAdapter(getActivity(), recipeData);
            recipePager.setAdapter(adapter);
            recipePager.setOffscreenPageLimit(7);
            recipePager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { // gets called when pages are cleared
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    if (recipeData.size() != 0) {
                        if (adapter.getRegisteredFragment(position).getShowRecipe().isSelected()) {
                            adapter.getRegisteredFragment(position).getShowRecipe().performClick();
                        }
                    }

                }
            });
        } else {
            Log.d("else", "else called");
            recipeData = recipeModel.sort();
            adapter.setRecipes(recipeData);
            adapter.notifyDataSetChanged();
        }


        new TabLayoutMediator(tabLayout, recipePager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

            }
        }).attach();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    @Override
    public String toString () {
        return "recipeHolderFragment";
    }
}
