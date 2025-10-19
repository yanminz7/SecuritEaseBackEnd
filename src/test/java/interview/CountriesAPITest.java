package interview;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.io.InputStream;
import java.util.*;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.api.model.GetRequest;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.*;

public class CountriesAPITest extends BaseTest{

    private static final Logger logger = LogManager.getLogger(CountriesAPITest.class);

    /**
     * TestNG test to validate response to confirm to schema
     * Logs the failure details and point out specific locations (pointers) where the validation failed and marks the test as failed in the report.
     */
    @Test
    public void schemaValidation() throws Exception {
        // Create a new GET request for the endpoint `/name/Canada`
        GetRequest request = new GetRequest(BaseUrl, "/name/Canada");
        // Add required headers and query parameters
        request.addHeader("Content-type", "application/json");
        request.addQueryParam("fullText", "true");
        // Execute the request and store the response
        Response response = request.execute();
        // Assert that the status code is 200 (OK)
        Assert.assertEquals(response.getStatusCode(), 200);
        // Some APIs may include charset in Content-Type, so we use startsWith instead of exact match
        Assert.assertTrue(response.getContentType().startsWith("application/json"));

        // Load the schema
        JSONObject schemaJson;
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream("schema/restcountries_schema.json")) {
            schemaJson = new JSONObject(new JSONTokener(is));
        }
        // Load the JSON schema using the SchemaLoader
        Schema schema = SchemaLoader.load(schemaJson);

        // Parse the actual response string into a JSON object
        Object payload = new JSONTokener(response.asString()).nextValue();
        if (!(payload instanceof JSONArray)) {
            Assert.fail("Expected root JSON array but got: " + payload.getClass().getSimpleName());
        }

        JSONArray arr = (JSONArray) payload;
        // Validate the response JSON array against the schema
        try {
            // Validates each object in the array
            schema.validate(arr);
        } catch (ValidationException e) {
            StringBuilder sb = new StringBuilder("Schema validation failed:\n");
            // Collect detailed error messages if validation fails
            e.getAllMessages().forEach(m -> sb.append(" - ").append(m).append('\n'));
            // Include specific locations (pointers) where the validation failed
            e.getCausingExceptions().forEach(c ->
                    sb.append(" at ").append(c.getPointerToViolation())
                            .append(" : ").append(c.getMessage()).append('\n')
            );
            // Fail the test with the detailed schema validation errors
            Assert.fail(sb.toString());
        }
    }

    /**
     * TestNG test to validate there are 195 countries in the world
     *
     */
    @Test
    void confirmTotalCountriesIs195() {
        // Create a new GET request for the endpoint `/independent`
        GetRequest request = new GetRequest(BaseUrl,"/independent");
        request.addHeader("Content-type","application/json");
        // Execute the request and store the response
        Response response = request.execute();
        logger.debug("Response Payload : "+response.asString());
        // Assert that the status code is 200 (OK)
        Assert.assertEquals(response.getStatusCode(), 200);
        // Get the array number of the returned response
        List<Object> countries = response.jsonPath().getList("$");
        int count = countries != null ? countries.size() : 0;
        // Assert there are 195 countries
        Assert.assertEquals(count,195);
    }
    /**
     * TestNG test to validate South African Sign Language (SASL) is included in the list of South Africa's official languages
     *
     */
    @Test
    void validateSASLInSouthAfricaLanguages() {
        // Create a new GET request for the endpoint `/name/South Africa`
        GetRequest request = new GetRequest(BaseUrl,"/name/South Africa");
        request.addHeader("Content-type","application/json");
        request.addQueryParam("fullText","true");
        // Execute the request and store the response
        Response response = request.execute();
        logger.debug("Response Payload : "+response.asString());
        // Assert that the status code is 200 (OK)
        Assert.assertEquals(response.getStatusCode(), 200);
        JsonPath jp = response.jsonPath();

        // Find the South Africa entry ( common name "South Africa")
        // Filter by 'name.common' equalsIgnoreCase "South Africa"
        List<Map<String, Object>> matches = jp.getList("findAll { it.name?.common == 'South Africa' }");
        // Assert South Africa country is returned in response
        Assert.assertNotNull(matches,"No results list returned for South Africa search.");
        Assert.assertFalse(matches.isEmpty(),"Could not find country with name == 'South Africa'");


        Map<String, Object> southAfrica = matches.get(0);
        Map<String, String> languages = (Map<String, String>) southAfrica.get("languages");

       // Assert SASL language is returned in response
        boolean hasSASL = languages.values().stream()
                .filter(Objects::nonNull)
                .anyMatch(lang -> lang.contains("south african sign language") || lang.equals("SASL") || lang.equals("sasl") );


        Assert.assertTrue(hasSASL, "Expected 'South African Sign Language (SASL)' to be present in South Africa's official languages.");

    }
}
