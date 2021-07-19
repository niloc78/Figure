package com.example.figure.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.figure.data.Hit;
import com.example.figure.data.Recipe;
import com.example.figure.data.RecipeResults;
import com.example.figure.RecipeSorter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

public class RecipeModel extends ViewModel {
    private final MutableLiveData<String> response = new MutableLiveData<String>();
    private final MutableLiveData<String[]> ingredients = new MutableLiveData<String[]>();

    public void setResponse(String resp) { // 0 is response body, 1 is ingreds
        response.setValue(resp);
    }
    public LiveData getResponse() {
        return response;
    }

    public LiveData<String[]> getIngredients() {
        return ingredients;
    }
    public void setIngredients(String[] ingreds) {
        ingredients.setValue(ingreds);
    }


    public ArrayList<Recipe> sort() { // sort best based on num of matched ingreds. aim to utilize as many as possible
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        Gson objectMapper = new Gson();

        RecipeResults recipeResults = objectMapper.fromJson((String) getResponse().getValue(), RecipeResults.class);
        Log.d("Test recipe name", recipeResults.getHits()[0].getRecipe().getRecipeName());

        for(Hit hit : recipeResults.getHits()) {
            recipes.add(hit.getRecipe());
            hit.getRecipe().calculateMatches((String[])ingredients.getValue());
        }
        Collections.sort(recipes, new RecipeSorter());
        Collections.reverse(recipes);


//        Log.d("Test sort first", recipes.get(0).getRecipeName());
//
//        JSONObject obj = new JSONObject(recipes.get(0).getTotalNutrients()); // use for specific nutrients
//
//        Log.d("Test nutrients", obj.toString());




//        Log.d("Test nutrients", "cal: " + recipes.get(0).getCalories() + "\n"
//                + "protein: " + recipes.get(0).getTotalNutrients().get("PROCNT").toString() + "\n"
//                + "carbs: " + recipes.get(0).getTotalNutrients().get("CHOCDF").toString() + "\n"
//                + "cholesterol: " + recipes.get(0).getTotalNutrients().get("CHOLE").toString() + "\n"
//                + "sodium: " + recipes.get(0).getTotalNutrients().get("NA").toString() + "\n");
        if (recipes.size() > 7) {
            return new ArrayList<Recipe>(recipes.subList(0,7));
        }

//        Log.d("Test nutrients", recipes.get(0).getTotalNutrients().keySet().toString());
        return recipes;

    }




}
