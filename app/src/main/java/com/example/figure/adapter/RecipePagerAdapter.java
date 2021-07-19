package com.example.figure.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.figure.Recipe;
import com.example.figure.fragment.RecipeFragment;

import java.util.ArrayList;

public class RecipePagerAdapter extends FragmentStateAdapter {

    ArrayList<Recipe> recipes;
    ArrayList<RecipeFragment> recipeFrags = new ArrayList<RecipeFragment>();
    ArrayList<Long> id = new ArrayList<Long>();


    public RecipePagerAdapter(FragmentActivity fa, ArrayList<Recipe> recipes) { // use context as constructor
        super(fa);
        this.recipes = recipes;

    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {

        recipeFrags.add(new RecipeFragment(recipes.get(position)));
        Log.d("create frag called : ", "recipes size: " + Integer.toString(recipes.size()) + " recipe frags size: "  + Integer.toString(recipeFrags.size()));
        //id.add(Long.valueOf(recipes.get(position).hashCode()));
//        return new RecipeFragment(recipes.get(position));
        return recipeFrags.get(recipeFrags.size()-1);
    }

    public void addFragment(Recipe recipe) {
        recipeFrags.add(new RecipeFragment(recipe));
        notifyDataSetChanged();
    }
    public void removeFragment(int position) {
        recipes.remove(position);
        recipeFrags.remove(position);

        notifyDataSetChanged();
    }
    public void clearFragments() {
        recipes.clear();
        recipeFrags.clear();

        Log.d("recipes size", Integer.toString(recipes.size()));
        Log.d("recipefrags size", Integer.toString(recipes.size()));
        notifyDataSetChanged();
    }



    public RecipeFragment getRegisteredFragment(int pos) {
        return recipeFrags.get(pos);
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public int getItemCount() {
        if (recipeFrags.size() < recipes.size()) {
            return recipes.size();
        }
//        return recipes.size();
        return recipes.size();

    }
//    @Override
//    public long getItemId(int position) {
//        return id.get(position);
//    }
//    @Override
//    public boolean containsItem(long itemId) {
//        return id.contains(itemId);
//    }

//    @Override
//    public long getItemId(int position) {
//        return recipeFrags.get(position);
//    }


}
