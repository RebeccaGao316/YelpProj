package com.example.springboot.controller;

import com.example.springboot.model.Business;
import com.example.springboot.model.Category;
import com.example.springboot.model.MyUtils;
import com.example.springboot.model.YelpSearchResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@RestController
public class YelpController {
    private static final String AUTH_TOKEN = "Bearer w-heucx-gIx0Wan7sU51qHwUNY6wTK5RIdQP03W18E_q0sX2CKpK0XF-h4LyWPprafi9f9HsEt3q-fxYPztl9QnWBnqhhx6mAh8KG6CG1Om2MNmyMGa3235zzg3XY3Yx";
    //request url

    @GetMapping("/search")
    public ResponseEntity<YelpSearchResponse> rerankOnLocTerm(@RequestParam String location,
                                                              @RequestParam(required = false) String term) throws IOException, InterruptedException {
        YelpSearchResponse beforeRerank = searchLocTerm(location,term,"");
        MyUtils.rerank(beforeRerank.getBusinesses());
        return ResponseEntity.ok().body(beforeRerank);
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
        String requestStr = String.format("https://api.yelp.com/v3/businesses/search?location=%s&term=%s&category=%s&sort_by=best_match",location , term, category);
        //request json
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestStr))
                .header("accept", "application/json")
                .header("Authorization", AUTH_TOKEN)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
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
