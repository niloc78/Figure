package com.example.figure.data;

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
}
