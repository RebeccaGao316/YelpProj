package com.example.springboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = Business.Builder.class)
public class Business {
    private final String id;
    private final String name;
    @JsonProperty("review_count")
    private final int reviewCount;
    private final double rating;
    @JsonProperty("categories")
    private final List<Category> categories;
    /*
    @JsonCreator
    private Business(@JsonProperty("id") String id,
                    @JsonProperty("name") String name,
                    @JsonProperty("review_count") int review_count,
                    @JsonProperty("rating") double rating){
        this.id = id;
        this.name = name;
        this.review_count = review_count;
        this.rating = rating;
    }
    */
    private Business(Builder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.reviewCount = builder.reviewCount;
        this.rating = builder.rating;
        this.categories = builder.categories;
    }
    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public int getReview_count() {
        return reviewCount;
    }

    public List<Category> getCategories(){return categories;}
    public String getId() {
        return id;
    }
    @JsonPOJOBuilder(withPrefix = "", buildMethodName = "build")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder{
        private String id;
        private String name;
        @JsonProperty("review_count")
        private int reviewCount;
        private double rating;

        private List<Category> categories;
        //let id be mandatory
        public Builder id(String id) {
            this.id = id;
            return this;
        }
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public Builder review_count(int reviewCount) {
            this.reviewCount = reviewCount;
            return this;
        }
        public Builder rating(double rating) {
            this.rating = rating;
            return this;
        }
        public Builder categories(List<Category> categories) {
            this.categories = categories;
            return this;
        }
        //Return the finally consrcuted User object
        public Business build() {
            return new Business(this);
        }
    }
}
