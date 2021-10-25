package qaa.shop_api;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class BasicCustomerAPIVerificationTest {

        private final String BASE_PATH = "http://localhost:3000/";
        private final String CUSTOMER_PATH = "/customers";

    @Test
    public void should200WhenFetchingCustomersList() {
        Header h1 = new Header("h1","v1");
        given().header(h1).log().headers()
                .when()
                .get(BASE_PATH + CUSTOMER_PATH)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    public void shouldReturnCustomerInfoForIdEquals2() {
        Response response = given().cookie("cookie","key1/value1,key2/value2").log().cookies()
                .when().get(BASE_PATH + CUSTOMER_PATH + "/2")
                .then().extract().response();

        System.out.println(response.getBody().prettyPrint());
        System.out.println(response.getStatusCode());
        System.out.println(response.getStatusLine());
        System.out.println(response.getCookies());
        System.out.println(response.getHeaders().asList());
    }

    @Test
    public void shouldGetPhoneVerifyForCustomer2() {
        when().get(BASE_PATH+CUSTOMER_PATH+ "/2")
                .then().statusCode(200)
                .body("address.phone",is(equalTo("33 55 789 123")));
    }

    @Test
    public void shouldVerifyCityForCustomerId3() {
        String query = "find {it.id == '3'}.address.city";
        String expectedCity = "Auckland";
        when().get(BASE_PATH + CUSTOMER_PATH)
                .then().log().all().body(query, equalTo(expectedCity));
    }
}
