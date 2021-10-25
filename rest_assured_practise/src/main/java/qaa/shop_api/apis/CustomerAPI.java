package qaa.shop_api.apis;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import qaa.shop_api.environments.Environment;
import qaa.shop_api.models.Customer;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class CustomerAPI {

    private final String HOST;
    private final Environment env;
    private RequestSpecification reqSpec;

    private final String CUSTOMERS_ENDPOINT = "/customers";
    private final String CHANGE_EMAIL_ENDPOINT = "/email";

    private CustomerAPI(Environment env) {
        this.env = env;
        HOST = env.getHost();
        requestSetUp();
    }

    public static CustomerAPI get(Environment env) {
        return new CustomerAPI(env);
    }

    public List<Customer> getAllCustomers() {
        String query = HOST + CUSTOMERS_ENDPOINT;
        return when().get(query)
                .then().log().body()
                .extract().body().jsonPath().getList("", Customer.class);
    }

    private void requestSetUp() {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setAccept(ContentType.JSON);
//        builder.setCookie("");
//        builder.addHeader();
    }

    public Customer updateEmailAddressForCustomer(String customerId, String newEmail) {
        String address = HOST + CUSTOMERS_ENDPOINT + "/" + customerId + CHANGE_EMAIL_ENDPOINT;
        Customer alex = given().spec(reqSpec)
                .when().patch(address)
                .then().extract().body().as(Customer.class);
        return alex;
    }

    public void addItemToCustomerCart(String customerId, String itemId, String quantity) {
        String endPointForAddingItem = HOST + CUSTOMERS_ENDPOINT +"/"+ customerId + "/cart";
        given().queryParam("quantity", quantity).queryParam("productId", itemId).put(endPointForAddingItem)
                .then().log().all();
    }

    public Customer getCustomersById(String customerId) {
        return when().get(HOST + CUSTOMERS_ENDPOINT+"/"+customerId)
                .then().extract().as(Customer.class);
    }
}
