package com.example.springboot.model;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
@Slf4j

public class BusinessDao {
    private static final Logger logger = LoggerFactory.getLogger(MyUtils.class);

    public void saveBusinessManually(StoreBusinessRequestManual b) throws SQLException{
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/yelpDB", "root", "yu010316");
            Statement stmt=con.createStatement();
            //String requestStr = String.format("https://api.yelp.com/v3/businesses/search?location=%s&term=%s&category=%s&sort_by=best_match",location , term, category);
            //

            String requestStr = String.format("insert into business(id, name, reviewCount, rating) values ('%s', '%s', %d, %f)",
                    b.getId(), b.getName(), b.getReviewCount(), b.getRating());
            int rowInserted = stmt.executeUpdate(requestStr);
            if(rowInserted > 0){
                System.out.println("A business is inserted successfully");
            } else {
                System.out.println("No new line");
            }

            // Insert categories in category table
            List<Category> categories = b.getCategories();
            if(categories != null && !categories.isEmpty()){
                for (Category c: categories){
                    String categoryInsertQuery = String.format("insert into category(alias, title) values ('%s','%s')",
                            c.getAlias(), c.getTitle());
                    stmt.executeUpdate(categoryInsertQuery);
                }
            }

            // Insert categories in business_category table
            if(categories != null && !categories.isEmpty()){
                for(Category c: categories){
                    String categoryBusinessInsertQuery = String.format("insert into business_category(businessID, alias) values ('%s', '%s')",
                            b.getId(),c.getAlias());
                    stmt.executeUpdate(categoryBusinessInsertQuery);
                }
            }

        }catch(Exception e){
            System.out.println(e);
        }


    }
    public void deleteBusinessManually(StoreBusinessRequestManual b) throws SQLException{
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/yelpDB", "root", "yu010316");
            Statement stmt=con.createStatement();

            List<Category> categories = b.getCategories();

            // delete categories in business_category table
            if(categories != null && !categories.isEmpty()){
                for(Category c: categories){
                    String categoryBusinessDeleteQuery = String.format("delete from business_category where businessID = '%s' and alias = '%s'",
                            b.getId(),c.getAlias());
                    stmt.executeUpdate(categoryBusinessDeleteQuery);
                }
            }


            String deleteQuery = String.format("delete from business where id = '%s' ",
                    b.getId());

            int rowDeleted = stmt.executeUpdate(deleteQuery);
            if(rowDeleted > 0){
                logger.info("A business is deleted successfully");
                logger.info(deleteQuery);
                logger.info(String.valueOf(rowDeleted));
            } else {
                logger.info("Fail to delete");
            }

            // Delete categories in category table

            if(categories != null && !categories.isEmpty()){
                for (Category c: categories){
                    String categoryDeleteQuery = String.format("delete from category where alias = '%s' and title = '%s'",
                            c.getAlias(), c.getTitle());
                    stmt.executeUpdate(categoryDeleteQuery);
                }
            }



        }catch(Exception e){
            System.out.println(e);
        }


    }
}
