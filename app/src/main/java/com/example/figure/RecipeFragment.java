package com.example.figure;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;

public class RecipeFragment extends Fragment {

    Context context;
    View _rootView;
    ImageView recipeImage;
    TextView recipeName;
    Recipe recipe;
    //    public IngredientFragment() {
//        super(R.layout.ingred_frag_layout);
//    }
    public RecipeFragment(Recipe recipe) {
        super();
        this.recipe = recipe;
    }

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
            _rootView = inflater.inflate(R.layout.recipe_frag_layout, container, false);

        }
        return _rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { // what happens after onCreateView
        super.onViewCreated(view, savedInstanceState);
        recipeImage = (ImageView) view.findViewById(R.id.recipe_image);
        recipeName = (TextView) view.findViewById(R.id.recipe_name);
        recipeName.setText(recipe.getRecipeName());
        Bitmap bmp = null;
        Log.d("image url", recipe.getImageUrl());
        Picasso.with(getActivity()).load(Uri.parse(recipe.getImageUrl())).into(recipeImage);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        recipeImage.setMinimumHeight((displayMetrics.widthPixels/4)*3);
    }

    public ImageView getRecipeImage() {
        return recipeImage;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    @Override
    public String toString () {
        return "recipeFragment";
    }

//    public void setImage() {
//
//    }
//    public void setRecipeName() {
//
//    }

}

