package activities;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Activity3 {

    RequestSpecification requestSpecification;

    ResponseSpecification responseSpecification;

    @BeforeClass
    public void setUp() {
        // Create request specification
        requestSpecification = new RequestSpecBuilder()
                // Set content type
                .setContentType(ContentType.JSON)
                // Set base URL
                .setBaseUri("https://petstore.swagger.io/v2/pet")
                // Build request specification
                .build();

        responseSpecification = new ResponseSpecBuilder()
                // Check status code in response
                .expectStatusCode(200)
                // Check response content type
                .expectContentType("application/json")
                // Check if response contains name property
                .expectBody("status", equalTo("alive"))
                // Build response specification
                .build();
    }

    @DataProvider
    public Object[][] petDetails() {
        // Setting parameters to pass to test case
        Object[][] testData = new Object[][] {
                { 151987, "beforeBrowne", "alive" },
                { 171987, "afterBrowne", "alive" }
        };
        return testData;
    }

    @Test(priority=1)
     public void addPets() {
        Response response1, response2;
        String reqBody = "{\"id\": 151987, \"name\": \"beforeBrowne\", \"status\": \"alive\"}";
        response1 = given().spec(requestSpecification)
                .body(reqBody)
                .when().post();
        response1.then().spec(responseSpecification);

        reqBody = "{\"id\": 171987, \"name\": \"afterBrowne\", \"status\": \"alive\"}";
        response2 = given().spec(requestSpecification)
                .body(reqBody)
                .when().post();
        response2.then().spec(responseSpecification);
    }


    @Test(dataProvider = "petDetails", priority=2)
    public void getPets(int id, String name, String status) {
        Response response = given().spec(requestSpecification)
                .pathParam("petId", id)
                .when().get("/{petId}");
        System.out.println(response.asPrettyString());
        response.then()
                .spec(responseSpecification) // Use responseSpec
                .body("name", equalTo(name)); // Additional Assertion
    }

    // Test case using a DataProvider
    @Test(dataProvider = "petDetails", priority=3)
    public void removePets(int id, String name, String status) {
        Response response = given().spec(requestSpecification)
                .pathParam("petId", id)
                .when().delete("/{petId}");
        response.then().body("code", equalTo(200));
    }

}
