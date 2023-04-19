package com.example.springboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;

import java.util.HashSet;

@Entity
@Table(name = "category")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = Category.Builder.class)
public class Category {
    @Id
    private final String alias;
    private final String title;
    @ManyToMany(mappedBy = "categories")
    private Set<Business> businesses = new HashSet<>();
    private Category(Builder builder){
        this.alias = builder.alias;
        this.title = builder.title;
        this.businesses = builder.businesses;
    }

    protected Category() {
        this.alias = null;
        this.title = null;
        this.businesses = null;
    }

    public String getAlias() {
        return alias;
    }

    public String getTitle() {
        return title;
    }
    @JsonPOJOBuilder(withPrefix = "", buildMethodName = "build")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Builder {
        private String alias;
        private String title;
        private Set<Business> businesses = new HashSet<>();


        public Builder alias(String alias) {
            this.alias = alias;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder businesses(Set<Business> businesses) {
            this.businesses = businesses;
            return this;
        }

        public Category build() {
            return new Category(this);
        }
    }
}

