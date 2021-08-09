package com.example.figure.data;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RestaurantResults {
    private int totalResults;
    private int page;
    private int total_pages;
    private boolean more_pages;
    private int numResults;
    private Restaurant[] data;

    public int getNumResults() {
        return numResults;
    }

    public int getPage() {
        return page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public Restaurant[] getData() {
        return data;
    }

    public void setData(Restaurant[] data) {
        this.data = data;
    }

    public void setMore_pages(boolean more_pages) {
        this.more_pages = more_pages;
    }

    public void setNumResults(int numResults) {
        this.numResults = numResults;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public void filterByPriceLevel(String priceLevel) {
        if (!priceLevel.isEmpty()) {
            List<Restaurant> restaurants = new LinkedList<>(Arrays.asList(data));
            Iterator<Restaurant> iter = restaurants.iterator();
            while (iter.hasNext()) {
                if (!((Restaurant)iter.next()).getPrice_range().equalsIgnoreCase(priceLevel)) {
                    iter.remove();
                }
            }
//            for (Restaurant restaurant : restaurants) {
//                if (!restaurant.getPrice_range().equalsIgnoreCase(priceLevel)) {
//                    restaurants.remove(restaurant);
//                }
//            }
            if (restaurants.size() > 0) {
                this.data = restaurants.toArray(new Restaurant[0]);
            }
        }


    }
}
