package com.example.springboot.controller;

import com.example.springboot.model.YelpSearchResponse;
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


@RestController
public class YelpController {
    private static final String AUTH_TOKEN = "Bearer w-heucx-gIx0Wan7sU51qHwUNY6wTK5RIdQP03W18E_q0sX2CKpK0XF-h4LyWPprafi9f9HsEt3q-fxYPztl9QnWBnqhhx6mAh8KG6CG1Om2MNmyMGa3235zzg3XY3Yx";
    //request url

    @GetMapping("/search")
    public ResponseEntity<YelpSearchResponse> rerankOnLocTerm(@RequestParam String location,
                                                              @RequestParam(required = false) String term) throws IOException, InterruptedException {
        YelpSearchResponse beforeRerank = searchLocTerm(location, term);
        YelpSearchResponse afterRerank = beforeRerank.rerank();
        return ResponseEntity.ok().body(afterRerank);
    }

    public YelpSearchResponse searchLocTerm(@RequestParam String location,
                                            @RequestParam(required = false) String term) throws IOException, InterruptedException {
        String requestStr = String.format("https://api.yelp.com/v3/businesses/search?location=%s&term=%s&sort_by=best_match",location , term);
        //request json
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestStr))
                .header("accept", "application/json")
                .header("Authorization", AUTH_TOKEN)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("aaaa");
        //json to object
        ObjectMapper mapper = new ObjectMapper();
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

}
