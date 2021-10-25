package qaa.shop_api;

import org.testng.Assert;
import org.testng.annotations.Test;
import qaa.shop_api.models.Customer;
import qaa.shop_api.models.OrderItem;
import qaa.shop_api.models.Product;

import java.util.Arrays;

import static io.restassured.RestAssured.*;

public class CustomerComplexTest {

    private String basePath ="http://localhost:3000/customers";
    private String addItem = "/{customerId}/cart";
    private String addItemToCartPath = "http://localhost:3000/customers/{customerId}/cart" +
            "?quantity={quantity}&productId={productId}";
    private String separator = "/";

    @Test
    public void customerShoppingCartItemShouldHaveLength0() {
        Customer customer = when().get(basePath + separator +"3").as(Customer.class);
        Assert.assertEquals(customer.getShoppingCart().getItems().length, 0);
    }

    @Test
    public void shouldVerifyItemsInShoppingCart(){
        when().put(basePath + "/2/cart?quantity=5&productId=2")
                .then().statusCode(200);
        Customer customer = when().get(basePath+separator+"2").as(Customer.class);
        String actualItem = Arrays.stream(customer.getShoppingCart().getItems())
                .map(OrderItem::getProduct)
                .map(Product::getDescription)
                .findAny().get();

        Assert.assertEquals(actualItem,"Banana");
    }

    @Test
    public void customerShoppingCartHaveExpectedLength() {
        String customerId = "3";
        getEmptyCartForCustomer(customerId);
        putProductToCartForCustomer(customerId,2,2);
        Customer customer = when().get(basePath+separator+customerId).as(Customer.class);

        Assert.assertEquals(customer.getShoppingCart().getItems().length, 1);
    }

    @Test
    public void shouldVerifyEmptyCartAfterAddingItem() {
        String customerId = "2";
        putProductToCartForCustomerPathParams(customerId,2,100);
        getEmptyCartForCustomer(customerId);
        Customer customer = when().get(basePath+separator+customerId).as(Customer.class);

        Assert.assertEquals(customer.getShoppingCart().getItems().length, 0);
    }

    private void getEmptyCartForCustomer(String customerId) {
        when().delete(basePath+separator+customerId+separator+"cart/empty")
                .then().statusCode(200);
    }

    private void putProductToCartForCustomer(String customerId, int productId, int productQuantity) {
        given().queryParam("quantity",productQuantity)
                .queryParam("productId",productId)
                .when().put(basePath+addItem).then().log().all();
    }

    private void putProductToCartForCustomerPathParams(String customerId, int productId, int productQuantity) {
        given().pathParam("customerId",customerId)
                .pathParam("quantity",productQuantity)
                .pathParam("productId",productId)
                .when().put(addItemToCartPath).then().log().all();
    }
}
