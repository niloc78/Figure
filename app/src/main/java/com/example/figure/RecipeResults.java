package com.example.figure;

public class RecipeResults {
    int from;
    int to;
    int count;
    Hit[] hits;

    public int getCount() {
        return count;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public Hit[] getHits() {
        return hits;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setHits(Hit[] hits) {
        this.hits = hits;
    }

    public void setTo(int to) {
        this.to = to;
    }

}
