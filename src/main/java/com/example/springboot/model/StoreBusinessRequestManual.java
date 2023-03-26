package com.example.springboot.model;

import java.util.List;

public class StoreBusinessRequestManual {
    private final String id;
    private final String name;
    private final int reviewCount;
    private final double rating;
    private final List<Category> categories;
    public StoreBusinessRequestManual(String id,
                         String name,
                         int reviewCount,
                         double rating, List<Category> categories){
        this.id = id;
        this.name = name;
        this.reviewCount = reviewCount;
        this.rating = rating;
        this.categories = categories;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public double getRating() {
        return rating;
    }
}
