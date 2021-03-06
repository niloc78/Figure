package com.example.figure.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.figure.data.Restaurant;
import com.example.figure.data.RestaurantResults;
import com.google.gson.Gson;


import java.util.Random;

public class RestaurantModel extends ViewModel {
    private final MutableLiveData<String> response = new MutableLiveData<String>();
    private final MutableLiveData<String> priceLevel = new MutableLiveData<String>();
    private final MutableLiveData<RestaurantResults> restaurantResults = new MutableLiveData<RestaurantResults>();

    public void setResponse(String resp, String priceLevel) { // 0 is response body, 1 is ingreds
        response.setValue(resp);
        this.priceLevel.setValue(priceLevel);
        restaurantResults.setValue(createResultObject());
    }
    public LiveData getResponse() {
        return response;
    }
    //choose random restaurant: create RestaurantResults, Restaurant, Menu,


    public LiveData getPriceLevel() {
        return priceLevel;
    }

    public RestaurantResults createResultObject() {
        Gson objectMapper = new Gson();

        RestaurantResults restaurantResults = objectMapper.fromJson((String) getResponse().getValue(), RestaurantResults.class);
        restaurantResults.filterByPriceLevel((String)getPriceLevel().getValue());
        return restaurantResults;
    }

    public LiveData getRestaurantResultObject() {
        return restaurantResults;
    }
    public Restaurant chooseRestaurant() {
        RestaurantResults result = (RestaurantResults)getRestaurantResultObject().getValue();
        //Log.d("Test restaurant name", restaurant.getRestaurant_name());
        return result.getData()[(new Random()).nextInt(result.getData().length)];
    }





    // organize menu: break into sections and items with cost and other info

    //


}
