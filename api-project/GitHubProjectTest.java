package GitHubProject;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static io.restassured.RestAssured.given;

public class GitHubProjectTest {

    RequestSpecification requestSpecification;
    int SSHId;

    @BeforeClass
    public void setUp(){
        requestSpecification = new RequestSpecBuilder()
                .addHeader("Content-type", "application/json")
                .addHeader("Authorization", "token")
                .setBaseUri("https://api.github.com")
                .build();
    }

    @Test(priority = 1)
    public void addSSHKey() throws IOException {
        // Import JSON file
        File file = new File("src/test/resources/input.json");
        FileInputStream inputJSON = new FileInputStream(file);
        // Get all bytes from JSON file
        byte[] bytes = new byte[(int) file.length()];
        inputJSON.read(bytes);
        // Read JSON file as String
        String reqBody = new String(bytes, "UTF-8");
        System.out.println(reqBody);

        Response response = given().spec(requestSpecification)
                .body(reqBody)
                .basePath("/user/keys")
                .when().post();

        System.out.println(response.getBody().asPrettyString());
        SSHId = response.then().extract().path("id");
        response.then().statusCode(201);
    }
    @Test(priority = 2)
    public void getSSHKey(){
        Response response = given().spec(requestSpecification)
                .pathParam("keyId", SSHId)
                .when().get("/user/keys/{keyId}");
        System.out.println(response.getBody().asPrettyString());
        response.then().statusCode(200);
    }
    @Test(priority = 3)
    public void deleteSSHKey(){
        Response response = given().spec(requestSpecification)
                .pathParam("keyId", SSHId)
                .when().delete("/user/keys/{keyId}");
        System.out.println(response.getBody().asPrettyString());
        response.then().statusCode(204);
    }
}