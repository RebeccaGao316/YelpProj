package com.example.springboot.model;

import java.util.List;

public class MyUtils {
    public static List<Business>  rerank(List<Business> businesses) {
        businesses.sort((Business b1, Business b2)
                ->Double.valueOf(b2.getReview_count() * b2.getRating()).compareTo(Double.valueOf(b1.getReview_count() * b1.getRating())));
        return businesses;
    }
}

