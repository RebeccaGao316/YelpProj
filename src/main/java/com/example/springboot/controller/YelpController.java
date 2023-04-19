package com.example.springboot.controller;

import com.example.springboot.model.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;


@Slf4j
@RestController
public class YelpController {
    private static final String AUTH_TOKEN = "Bearer w-heucx-gIx0Wan7sU51qHwUNY6wTK5RIdQP03W18E_q0sX2CKpK0XF-h4LyWPprafi9f9HsEt3q-fxYPztl9QnWBnqhhx6mAh8KG6CG1Om2MNmyMGa3235zzg3XY3Yx";
    //request url
    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(YelpController.class);


    @GetMapping("/search")
    public ResponseEntity<YelpSearchResponse> rerankOnLocTerm(@RequestParam String location,
                                                              @RequestParam(required = false) String term) throws IOException, InterruptedException {
        YelpSearchResponse beforeRerank = searchLocTerm(location,term,"");
        MyUtils.rerank(beforeRerank.getBusinesses());
        return ResponseEntity.ok().body(beforeRerank);
    }
    @GetMapping("/searchAndStore")
    public ResponseEntity<String> searchAndStore(@RequestParam String location,
                                            @RequestParam String term) throws IOException, InterruptedException{
        try {
            YelpSearchResponse response = searchLocTerm(location, term, null);
            List<Business> businesses = response.getBusinesses();
            int count = 0;
            // Insert businesses and categories into database
            for (Business business : response.getBusinesses()) {
                // Check if business already exists in database
                Optional<Business> existingBusiness = businessRepository.findById(business.getId());
                if (existingBusiness.isPresent()) {
                    continue;
                }
                // Insert new business into database
                Set<Category> categories = new HashSet<>();
                for (Category category : business.getCategories()) {
                    String categoryAlias = category.getAlias();
                    Optional<Category> existingCategory = categoryRepository.findByAlias(categoryAlias);
                    if (existingCategory.isPresent()) {
                        category = existingCategory.get();
                    } else {
                        categoryRepository.save(category);
                    }
                    categories.add(category);
                    count += categories.size();
                }
                business.setCategories(categories);
                businessRepository.save(business);
            }
            logger.info(count + "Data stored successfully to database");
        } catch (Exception ex) {
            logger.error("Error while storing data to database: " + ex.getMessage(), ex);
        }




        return ResponseEntity.ok().body("Businesses and categories inserted successfully!");

    }

    @GetMapping("/searchByCategory")
    public ResponseEntity<YelpSearchResponse> searchByCategory(@RequestParam String location,
                                                               @RequestParam String category)
            throws IOException, InterruptedException {
        YelpSearchResponse beforeRerank = searchLocTerm(location,null,category);

        MyUtils.rerank(beforeRerank.getBusinesses());
        //YelpSearchResponse afterRerank = beforeRerank.rerank();
        return ResponseEntity.ok().body(beforeRerank);
    }

    public YelpSearchResponse searchLocTerm(@RequestParam String location,
                                            @RequestParam(required = false) String term,
                                            @RequestParam(required = false) String category)
                                            throws IOException, InterruptedException {
        logger.info("Starting API request");
        //start time and end time
        long startTime = System.currentTimeMillis();

        String requestStr = String.format("https://api.yelp.com/v3/businesses/search?location=%s&term=%s&category=%s&sort_by=best_match",location , term, category);
        //request json
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestStr))
                .header("accept", "application/json")
                .header("Authorization", AUTH_TOKEN)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        long endTime = System.currentTimeMillis();
        logger.info("API request completed in {} ms", (endTime - startTime));
        //json to object
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return mapper.readValue(response.body(), YelpSearchResponse.class);

    }
    /*
    public YelpSearchResponse searchLocTerm(@RequestParam String location,
                                            @RequestParam(required = false) String term){
        OkHttpClient client = new OkHttpClient();
        String request = String.format("https://api.yelp.com/v3/businesses/search?location=%s&term=%s&sort_by=best_match",location , term);

        Request request = new Request.Builder()
                .url(request)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer w-heucx-gIx0Wan7sU51qHwUNY6wTK5RIdQP03W18E_q0sX2CKpK0XF-h4LyWPprafi9f9HsEt3q-fxYPztl9QnWBnqhhx6mAh8KG6CG1Om2MNmyMGa3235zzg3XY3Yx")
                .build();

        Response response = client.newCall(request).execute();
    }
    */

    /*
    function2
    @GetMapping("/searchCategoriesGroup")
    public ResponseEntity<Map<String, List<Business>>> searchCategoriesGroup(@RequestParam String location,
                                                              @RequestParam(required = false) String term) throws IOException, InterruptedException {
        YelpSearchResponse beforeRerank = searchLocTerm(location, term);
        YelpSearchResponse afterRerank = beforeRerank.rerank();
        LinkedHashMap<String, List<Business>> categoryMap = new LinkedHashMap<>();
        for(Business b: afterRerank.getBusinesses()){
            for(Map cMap: b.getCategories()) {
                String categoryName = (String) cMap.get("alias");
                List<Business> curr = null;
                if (categoryMap.containsKey(categoryName)) {
                    curr = categoryMap.get(categoryName);
                } else {
                    curr = new ArrayList<>();
                }
                curr.add(b);
                categoryMap.put(categoryName, curr);
            }
        }
        categoryMap.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().size() - e1.getValue().size());
        return ResponseEntity.ok().body(categoryMap);
    }
     */
    @GetMapping("/searchCategoriesGroup")
    public ResponseEntity<Map<String, List<Business>>> searchCategoriesGroup(@RequestParam String location,
                                                              @RequestParam(required = false) String term) throws IOException, InterruptedException {
        YelpSearchResponse beforeRerank = searchLocTerm(location, term, "");
        YelpSearchResponse afterRerank = beforeRerank.rerank();
        LinkedHashMap<String, List<Business>> categoryMap = new LinkedHashMap<>();
        for(Business b: afterRerank.getBusinesses()){
            for(Category categoriesOfBusiness: b.getCategories()) {
                String categoryName = categoriesOfBusiness.getAlias();
                List<Business> curr = null;
                if (categoryMap.containsKey(categoryName)) {
                    curr = categoryMap.get(categoryName);
                } else {
                    curr = new ArrayList<>();
                }
                curr.add(b);
                categoryMap.put(categoryName, curr);
            }
        }
        for (List<Business> list : categoryMap.values()) {
            MyUtils.rerank(list);
        }
        categoryMap.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().size() - e1.getValue().size());
        return ResponseEntity.ok().body(categoryMap);
    }


}
