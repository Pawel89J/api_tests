package qaa.shop_api;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

public class ReqresInTest {

    private static String targetGetPathWithParams = "https://reqres.in/api/users?page=2";

    @Test
    public void shouldGetUsersIds() {
        when().get(targetGetPathWithParams)
                .then().log().all().body("data.find {}", hasItems(7,8));
    }

    @Test
    public void shouldReturnUsersWithRequestSpecification(){
        ResponseSpecBuilder builder  = new ResponseSpecBuilder();
        builder.expectContentType(ContentType.JSON);
        builder.expectHeader("Connection","keep-alive");
        builder.expectBody("data[2].email", equalTo("tobias.funke@reqres.in"));
        ResponseSpecification spec = builder.build();
        when().get(targetGetPathWithParams)
                .then()
                .spec(spec); // may be in given()
    }
}
