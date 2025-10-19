package com.api.model;

import io.restassured.RestAssured;
import io.restassured.response.Response;
/**
 * GetRequest is a concrete implementation of BaseRequest specifically for handling HTTP GET requests.
 * It builds and executes a GET call using the provided base URL, endpoint, headers, and query parameters.
 *
 * Example:
 *   GetRequest request = new GetRequest("https://restcountries.com/v3.1", "/name/Canada");
 *   request.addHeader("Content-type", "application/json");
 *   Response response = request.execute();
 */
public class GetRequest extends BaseRequest<Response> {
    public GetRequest(String baseUrl, String endpoint) {
        super(baseUrl, endpoint);
    }
    @Override
    public Response execute() {
        return RestAssured.given()
                .baseUri(baseUrl)
                .basePath(endpoint)
                .headers(headers)
                .queryParams(queryParams)
                .when()
                .get();
    }
}
