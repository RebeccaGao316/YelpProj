package com.example.springboot.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Business {
    private final String id;
    private String name;
    private int review_count;
    private double rating;
    @JsonCreator
    public Business(@JsonProperty("id") String id,
                    @JsonProperty("name") String name,
                    @JsonProperty("review_count") int review_count,
                    @JsonProperty("rating") double rating){
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
