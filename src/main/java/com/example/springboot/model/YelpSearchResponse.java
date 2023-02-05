package com.example.springboot.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
"businesses": [
    {
      "id": "H4jJ7XB3CetIr1pg56CczQ",
      "alias": "levain-bakery-new-york",
      "name": "Levain Bakery",
      "image_url": "https://s3-media3.fl.yelpcdn.com/bphoto/DH29qeTmPotJbCSzkjYJwg/o.jpg",
      "is_closed": false,
      "url": "https://www.yelp.com/biz/levain-bakery-new-york?adjust_creative=u55i4aug5FSusmvtMA_mfw&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=u55i4aug5FSusmvtMA_mfw",
      "review_count": 9449,
      "categories": [
        {
          "alias": "bakeries",
          "title": "Bakeries"
        }
      ],
      "rating": 4.5,
      "coordinates": {
        "latitude": 40.779961,
        "longitude": -73.980299
      },
      "transactions": [],
      "price": "$$",
      "location": {
        "address1": "167 W 74th St",
        "address2": "",
        "address3": "",
        "city": "New York",
        "zip_code": "10023",
        "country": "US",
        "state": "NY",
        "display_address": [
          "167 W 74th St",
          "New York, NY 10023"
        ]
      },
      "phone": "+19174643769",
      "display_phone": "(917) 464-3769",
      "distance": 8115.903194093832
    }
 */
public class YelpSearchResponse {
    private List<Business> businesess;
    private String locationTerm;
    private int total;

    YelpSearchResponse(List<Business> businesess, String locationTerm, int total){
        this.businesess = businesess;
        this.locationTerm = locationTerm;
        this.total = total;
    }
    public YelpSearchResponse rerank(){
        Collections.sort(businesess, new Comparator<Business>() {
            @Override
            public int compare(Business b1, Business b2) {
                return Double.compare(b1.getReview_count() * b1.getRating(), b2.getReview_count() * b2.getRating());
            }
        });
        return this;

    }
}
