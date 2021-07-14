package com.example.figure;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class Recipe {
    @SerializedName(value="label")
    private String recipeName;
    @SerializedName(value="ingredientLines")
    private String[] requiredIngredients;
    private String uri;
    private String recipeId;
    Integer matches;
    @SerializedName(value="image")
    private String imageUrl;
    float calories;
    private HashMap<String, Object> totalNutrients;
    private String url;

    public HashMap<String, Object> getTotalNutrients() {
        return totalNutrients;
    }

    public float getCalories() {
        return calories;
    }
    //    public String getCalories() {
//        return calories;
//    }
//
//    public String getCarbs() {
//        return carbs;
//    }
//
//    public String getCholesterol() {
//        return cholesterol;
//    }
//
//    public String getProtein() {
//        return protein;
//    }
//
//    public String getSodium() {
//        return sodium;
//    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getRecipeId() {
        return recipeId;
    }



    public String getRecipeName() {
        return recipeName;
    }

    public String getUri() {
        return uri;
    }

    public String getUrl() {
        return url;
    }

    public String getRequiredIngredients() {
        String x = "";
        for(String s : requiredIngredients) {
            x += s + "\n";
        }
        return x;
    }

    public int calculateMatches(String[] ingreds) {
        int m = 1;
        for (String i : ingreds) {
            Log.d("my ingredient: " , i);
            Log.d("recipe name", getRecipeName());
            Log.d("required ingreds", getRequiredIngredients());

            if (getRequiredIngredients().toLowerCase().contains(i.toLowerCase())) {
                Log.d("matched", "matched ingredient: " + i);
                m += 1;
            }
        }
        matches = m;
        return matches;
    }

    public Integer getMatches() {
        return matches;
    }

    public void setImageUrl(String image) {
        this.imageUrl = image;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setRequiredIngredients(String[] requiredIngredients) {
        this.requiredIngredients = requiredIngredients;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
