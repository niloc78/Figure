package com.example.figure;

import java.util.Comparator;

public class RecipeSorter implements Comparator<Recipe> {
    @Override
    public int compare(Recipe r1, Recipe r2) {

        return r2.getMatches().compareTo(r1.getMatches());
    }

}
