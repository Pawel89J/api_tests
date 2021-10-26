package qaa.shop_api;

import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import qaa.shop_api.models.Product;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BasicProductAPIVerificationTest {

    private final String BASE_PATH = "http://localhost:3000";
    private final String PRODUCT_PATH = "/products";
    private final String SEPARATOR = "/";

    @Test
    public void shouldAddNewProduct() {
        given().body("""
                        {
                            "description": "Yerba Mate",
                            "manufacturer": 8,
                            "price": 6.7
                        }""").log().body()
                .when().post(BASE_PATH + PRODUCT_PATH)
                .then().log().all().statusLine(containsString("OK"));
    }

    @Test
    public void shouldUpdateProductById() {
        Float newPrice = 9.3f;
        HashMap<String,Object> productData = new HashMap<>();
        productData.put("description", "Banana");
        productData.put("id", "2");
        productData.put("manufacturer", 1);
        productData.put("price", newPrice);

        given().contentType(ContentType.JSON)
                .body(productData)
                .when().put(BASE_PATH + PRODUCT_PATH + "/2")
                .then().log().all()
                .body("price", equalTo(newPrice));
    }

    @Test
    public void shouldProductListContainPeachAndStrawberry() {
        String expectedDescription1 = "Peach";
        String expectedDescription2 = "Strawberry";
        when()
                .get(BASE_PATH + PRODUCT_PATH)
                .then()
                .assertThat()
                .body("description",hasItems(expectedDescription1,expectedDescription2));

    }

    @Test
    public void isStrawberryPriceDefined() {
        Float expectedPrice = 18.3f;
        String strawberryId = "/5";
        when()
                .get(BASE_PATH + PRODUCT_PATH + strawberryId)
                .then()
                .assertThat()
                .body("price",equalTo(expectedPrice));
    }

    @Test
    public void shouldRemoveRoastedCoffee() {
        HashMap<String,Object> productData = new HashMap<>();
        productData.put("description", "Roasted Coffee");
        productData.put("manufacturer", 7);
        productData.put("price", 19.9);
        String id = given().contentType(ContentType.JSON).body(productData)
                .when().post(BASE_PATH + PRODUCT_PATH).then().extract()
                .body().jsonPath().getString("id");
        when()
                .delete(BASE_PATH + "/" + id)
                .then()
                .assertThat()
                .body(equalTo(String.valueOf(true)));
    }

    @Test
    public void extractedProductShouldHaveExpectedDescription() {
        String productId = "3";
        String expectedDescription = "Grapes";

        Product grapes = given()
                .when().get(BASE_PATH+PRODUCT_PATH+SEPARATOR+productId)
                .then().extract().body().as(Product.class);
        Assert.assertEquals(expectedDescription, grapes.getDescription());
    }

    @Test
    public void extractedProductShouldHaveExpectedPriceAndDescription() {
        String productId = "7";
        String expectedDescription = "Orange";
        float expectedPrice = 10.5f;


        Product orange = given()
                .when().get(BASE_PATH+PRODUCT_PATH+SEPARATOR+productId)
                .then().extract().body().as(Product.class);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(orange.getDescription(), expectedDescription);
        softAssert.assertEquals(orange.getPrice(), expectedPrice);
        softAssert.assertAll();
    }

    @Test
    public void shouldReturnProductAsAHashMap() {
        String productId = "6";
        Map<String,String> map = given()
                .when().get(BASE_PATH+PRODUCT_PATH+SEPARATOR+productId)
                .then().extract().body().jsonPath().getMap("",String.class, String.class);
        System.out.println(map.get("description"));
        System.out.println(map);
    }

    @Test
    public void shouldCreateProductsUsingClassInstance() {
        Product greenTea = new Product("Green Tea","",9,9);

        given().contentType(ContentType.JSON)
                .body(greenTea).when().post()
                .then().log().all().statusCode(200);
    }
}
