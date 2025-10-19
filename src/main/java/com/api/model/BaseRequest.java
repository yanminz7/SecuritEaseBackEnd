package com.api.model;

import java.util.HashMap;
import java.util.Map;

/**
 * BaseRequest is an abstract class that defines the common behavior for an HTTP request.
 * It can be extended by subclasses to implement specific types of API requests (e.g. GET, POST, PUT).
 *
 * @param <T> the expected return type from the request execution (e.g. Response, custom response model)
 */

public abstract class BaseRequest<T> {
    protected String baseUrl;
    protected String endpoint;
    protected Map<String, String> headers = new HashMap<>();
    protected Map<String, String> queryParams = new HashMap<>();
    protected String requestBody;
    /**
     * Constructor for initializing base URL and endpoint.
     *
     * @param baseUrl  the base URL of the API
     * @param endpoint the specific API endpoint for this request
     */
    public BaseRequest(String baseUrl, String endpoint) {
        // Base URL for the API
        this.baseUrl = baseUrl;
        // Endpoint path for the request
        this.endpoint = endpoint;
    }
    /**
     * Abstract method to execute the request.
     * Subclasses must implement this method to define request logic (GET, POST, etc.)
     *
     * @return response of type T (can be raw response, model, or validation result)
     */
    public abstract T execute();
    /**
     * Adds a header key-value pair to the request.
     *
     * @param key   header name (e.g., "Content-Type")
     * @param value header value (e.g., "application/json")
     */
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }
    /**
     * Adds a query parameter key-value pair to the request.
     *
     * @param key   query parameter name
     * @param value query parameter value
     */
    public void addQueryParam(String key, String value) {
        queryParams.put(key, value);
    }
    /**
     * Sets the request body as a raw string .
     *
     * @param requestBody request payload (e.g., JSON string)
     */
    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }
}
