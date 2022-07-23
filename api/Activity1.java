package activities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Activity1 {

    RequestSpecification requestSpecification;

    String reqBody;

    int petId;

    @BeforeClass
    public void setUp(){
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/pet")
                .addHeader("Content-Type", "application/json")
                .build();
        reqBody = "{\"id\": 161987, \"name\": \"Browne\", \"status\": \"alive\"}";
    }

    @Test(priority = 1)
    public void addPet(){
        Response response = given().spec(requestSpecification)
                .body(reqBody)
                .when().post();

        petId = response.then().extract().path("id");

        response.then().statusCode(200);
        response.then().body("name", equalTo("Browne"));

    }

    @Test(priority = 2)
    public void getPet(){

        Response response = given().spec(requestSpecification)
                .pathParam("petId", petId)
                .when().get("/{petId}");

        response.then().statusCode(200);
        response.then().body("name", equalTo("Browne"));
    }

    @Test(priority = 3)
    public void removePet(){

        Response response = given().spec(requestSpecification)
                .pathParam("petId", petId)
                .when().delete("/{petId}");

        response.then().statusCode(200);
        response.then().body("message", equalTo("" +petId));
    }

}
