package com.example.figure;

import java.util.HashMap;

public class Restaurant {
    private String restaurant_name;
    private String restaurant_phone;
    private String restaurant_website;
    private String hours;
    private String price_range;
    private long restaurant_id;
    private HashMap<String, String> address;
    private HashMap<String, Double> geo;
    private Menu[] menus;

    public HashMap<String, Double> getGeo() {
        return geo;
    }

    public HashMap<String, String> getAddress() {
        return address;
    }

    public long getRestaurant_id() {
        return restaurant_id;
    }

    public Menu[] getMenus() {
        return menus;
    }

    public String getHours() {
        return hours;
    }

    public String getPrice_range() {
        return price_range;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public String getRestaurant_phone() {
        return restaurant_phone;
    }

    public String getRestaurant_website() {
        return restaurant_website;
    }

    public void setAddress(HashMap<String, String> address) {
        this.address = address;
    }

    public void setGeo(HashMap<String, Double> geo) {
        this.geo = geo;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public void setMenus(Menu[] menus) {
        this.menus = menus;
    }

    public void setPrice_range(String price_range) {
        this.price_range = price_range;
    }

    public void setRestaurant_id(long restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public void setRestaurant_phone(String restaurant_phone) {
        this.restaurant_phone = restaurant_phone;
    }

    public void setRestaurant_website(String restaurant_website) {
        this.restaurant_website = restaurant_website;
    }
}

