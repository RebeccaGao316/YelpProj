package com.example.springboot.model;
public class Business {
    private final String id;
    private String name;
    private int review_count;
    private double rating;
    public Business(String id, String name, int review_count, double rating){
        this.id = id;
        this.name = name;
        this.review_count = review_count;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public int getReview_count() {
        return review_count;
    }

    public String getId() {
        return id;
    }
}
