package com.example.springboot.model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MyUtils {
    private static final Logger logger = LoggerFactory.getLogger(MyUtils.class);

    public static List<Business> rerank(List<Business> businesses) {
        logger.info("Starting rerank");

        try {
            businesses.sort((Business b1, Business b2)
                    ->Double.valueOf(b2.getReviewCount() * b2.getRating()).compareTo(Double.valueOf(b1.getReviewCount() * b1.getRating())));

            logger.info("Rerank completed successfully");
        } catch (Exception ex) {
            logger.error("Error while reranking: " + ex.getMessage(), ex);
            // handle exception
        }
        return businesses;
    }
}

