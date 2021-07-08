package com.example.figure;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecipePagerAdapter extends FragmentStateAdapter {

    ArrayList<Recipe> recipes;
    ArrayList<Long> id = new ArrayList<Long>();


    public RecipePagerAdapter(FragmentActivity fa, ArrayList<Recipe> recipes) { // use context as constructor
        super(fa);
        this.recipes = recipes;

    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        //id.add(Long.valueOf(recipes.get(position).hashCode()));
        return new RecipeFragment(recipes.get(position));
    }

    @Override
    public int getItemCount() {
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
