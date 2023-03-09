package com.example.springboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = Category.Builder.class)
public class Category {
    private final String alias;
    private final String title;
    private Category(Builder builder){
        this.alias = builder.alias;
        this.title = builder.title;
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

        public Builder alias(String alias) {
            this.alias = alias;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Category build() {
            return new Category(this);
        }
    }
}

