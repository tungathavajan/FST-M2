package activities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Activity2 {

    RequestSpecification requestSpecification;

    String reqBody;

    int petId;

    @BeforeClass
    public void setUp(){
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/user")
                .addHeader("Content-Type", "application/json")
                .build();
    }

    @Test(priority = 1)
    public void addNewUser() throws IOException {

        File file = new File("src/test/resources/input.json");
        FileInputStream inputJSON = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        inputJSON.read(bytes);
        String reqBody = new String(bytes, "UTF-8");
        System.out.println(reqBody);
        Response response = given().spec(requestSpecification)
                .body(reqBody)
                .when().post();

        System.out.println(response.getBody().asPrettyString());
        response.then().statusCode(200);
        response.then().body("message", equalTo("198716"));

    }

    @Test(priority = 2)
    public void getUser(){

        Response response = given().spec(requestSpecification)
                .pathParam("username", "tungathinc")
                .when().get("/{username}");
        System.out.println(response.getBody().asPrettyString());
        File outputJSON = new File("src/test/resources/output.json");
        try {
            // Create JSON file
            outputJSON.createNewFile();
            // Write response body to external file
            FileWriter writer = new FileWriter(outputJSON.getPath());
            writer.write(response.getBody().asPrettyString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.then().statusCode(200);
        response.then().body("id", equalTo(198716));
        response.then().body("username", equalTo("tungathinc"));
        response.then().body("firstName", equalTo("tungath"));
        response.then().body("lastName", equalTo("fst"));
        response.then().body("email", equalTo("tungathfst@mail.com"));
        response.then().body("password", equalTo("tungath123"));
        response.then().body("phone", equalTo("9812763450"));
     

    }

    @Test(priority = 3)
    public void removeUser(){

        Response response = given().spec(requestSpecification)
                .pathParam("username", "tungathinc")
                .when().delete("/{username}");

        response.then().statusCode(200);
        response.then().body("message", equalTo("tungathinc"));
    }

}
