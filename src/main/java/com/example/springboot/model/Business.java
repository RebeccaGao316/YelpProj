package com.example.springboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.Transient;

import java.util.List;
@Entity
@Table(name = "business")
@JsonDeserialize(builder = Business.Builder.class)
public class Business {
    @Id
    @Column(name = "id")
    private final String id;
    @Column(name = "name")
    private final String name;
    @JsonProperty("review_count")
    @Column(name = "reviewCount")
    private final int reviewCount;
    @Column(name = "rating")
    private final double rating;

    @Transient
    @JsonProperty("categories")
    private final List<Category> categories;
    protected Business() {
        this.id = null;
        this.name = null;
        this.reviewCount = 0;
        this.rating = 0.0;
        this.categories = null;
    }
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

    public int getReviewCount() {
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
        public Builder reviewCount(int reviewCount) {
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
