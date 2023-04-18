# [Yelp helper]

Yelp helper is a Java Springboot project for building tools to collect and re-process data from external yelp API. The current product consists only backend API and the frontend webpage will be implemented later.

## Installation

Currently, Yelp helper has been designed local use. In the future, it will be deployed to cloud (AWS).
To install the dependencies locally, 
```jsx
./gradlew build (build project with dependencies)
./gradlew bootRun (start server)
```

## Documentation

You can find the React documentation [on the website](https://reactjs.org/).  

Check out the [Getting Started](https://reactjs.org/docs/getting-started.html) page for a quick overview.

The documentation is divided into several sections:

* [Tutorial](https://reactjs.org/tutorial/tutorial.html)
* [Main Concepts](https://reactjs.org/docs/hello-world.html)
* [Advanced Guides](https://reactjs.org/docs/jsx-in-depth.html)
* [API Reference](https://reactjs.org/docs/react-api.html)
* [Where to Get Support](https://reactjs.org/community/support.html)
* [Contributing Guide](https://reactjs.org/docs/how-to-contribute.html)

You can improve it by sending pull requests to [this repository](https://github.com/reactjs/reactjs.org).

## API Examples

We have several examples about using API. Currently, you need to use curl command to test API. Here is the first one to get you started:

```jsx
curl "localhost:8080/search?term=food&location=NYC"
```

Search API will search all yelp result given specific parameter and rerank them in a descending order. The rank depends on both overall rank score and number of rank, using the multiplication between these two numbers.

>Parameters: 

+ String location

+ (optional) String term

>Returns: 

+ search returns the reranked list of restaurant in a YelpSearchResponse object.
