package com.api.model;

import io.restassured.RestAssured;
import io.restassured.response.Response;
/**
 * PostRequest is a concrete implementation of BaseRequest specifically for handling HTTP Post requests.
 * It builds and executes a Post call using the provided base URL, endpoint, headers, message body.
 *
 * Example:
 *   PostRequest request = new PostRequest("https://restcountries.com/v3.1", "/name/Canada");
 *   request.addHeader("Authorization", "Bearer token");
 *   Response response = request.execute();
 */
public class PostRequest extends BaseRequest<Response> {
    public PostRequest(String baseUrl, String endpoint) {
        super(baseUrl, endpoint);
    }
    @Override
    public Response execute() {
        return RestAssured.given()
                .baseUri(baseUrl)
                .basePath(endpoint)
                .headers(headers)
                .body(requestBody)
                .when()
                .post();
    }
}

