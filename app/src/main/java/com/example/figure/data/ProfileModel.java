package com.example.figure.data;

import android.graphics.Bitmap;
import android.util.Base64;

import org.bson.Document;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;



public class ProfileModel {
    private String name;
    private int calories;
    private int goalCalories;
    private int fat;
    private int goalFat;
    private int carbs;
    private int goalCarbs;
    private int protein;
    private int goalProtein;
    private HashMap<String, Integer> height;
    private int weight;
    private int goalWeight;
    private String profilePicBitmap;


    public ProfileModel() {

    }
    public ProfileModel(String name, int calories, int goalCalories,
                        int fat, int goalFat, int carbs, int goalCarbs, int protein, int goalProtein,
                        int weight, int goalWeight, HashMap<String, Integer> height, Bitmap profilePicBitmap) {
        this.name = name;
        this.calories = calories;
        this.goalCalories = goalCalories;
        this.fat = fat;
        this.goalFat = goalFat;
        this.carbs = carbs;
        this.goalCarbs = goalCarbs;
        this.protein = protein;
        this.goalProtein = goalProtein;
        this.weight = weight;
        this.goalWeight = goalWeight;
        this.height = height;
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        profilePicBitmap.compress(Bitmap.CompressFormat.PNG,100,boas);
        byte[] b = boas.toByteArray();
        this.profilePicBitmap = Base64.encodeToString(b, Base64.DEFAULT);

    }

    public String getProfilePicBitmapString() {
        return profilePicBitmap;
    }

    public void setProfilePicBitmapString(String profilePicBitmap) {
        this.profilePicBitmap = profilePicBitmap;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Integer>getHeight() {
        return height;
    }

    public void setHeight(HashMap<String, Integer> height) {
        this.height = height;
    }
    //    public RealmMap<String, Integer> getHeight() {
//        return height;
//    }

    public int getCalories() {
        return calories;
    }

    public int getCarbs() {
        return carbs;
    }

    public int getFat() {
        return fat;
    }

    public int getGoalCalories() {
        return goalCalories;
    }

    public int getGoalCarbs() {
        return goalCarbs;
    }

    public int getGoalFat() {
        return goalFat;
    }

    public int getGoalProtein() {
        return goalProtein;
    }

    public int getGoalWeight() {
        return goalWeight;
    }

    public int getProtein() {
        return protein;
    }

    public int getWeight() {
        return weight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public void setGoalCalories(int goalCalories) {
        this.goalCalories = goalCalories;
    }

    public void setGoalCarbs(int goalCarbs) {
        this.goalCarbs = goalCarbs;
    }

    public void setGoalFat(int goalFat) {
        this.goalFat = goalFat;
    }

    public void setGoalProtein(int goalProtein) {
        this.goalProtein = goalProtein;
    }

    public void setGoalWeight(int goalWeight) {
        this.goalWeight = goalWeight;
    }

//    public void setHeight(RealmMap<String, Integer> height) {
//        this.height = height;
//    }
    public Document convertToDocument() {
        Document height = new Document("feet", getHeight().get("feet"));
        height.put("inch", getHeight().get("inch"));
        Document profileContents = new Document("name", getName());
                 profileContents.put("image_bitmap_string", getProfilePicBitmapString());
                 profileContents.put("height", height);
                 profileContents.put("weight", getWeight());
                 profileContents.put("goal_weight", getGoalWeight());
                 profileContents.put("calories", getCalories());
                 profileContents.put("goal_calories", getGoalCalories());
                 profileContents.put("fat", getFat());
                 profileContents.put("goal_fat", getGoalFat());
                 profileContents.put("carbs", getCarbs());
                 profileContents.put("goal_carbs", getGoalCarbs());
                 profileContents.put("protein", getProtein());
                 profileContents.put("goal_protein", getGoalProtein());
        return new Document("profile", profileContents);
    }
    public void setProtein(int protein) {
        this.protein = protein;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
    @Override
    public String toString() {
        return convertToDocument().toJson();
    }
}
