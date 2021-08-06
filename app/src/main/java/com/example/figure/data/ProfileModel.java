package com.example.figure.data;

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

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
