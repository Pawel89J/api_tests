package qaa.shop_api.apis;

import io.restassured.http.ContentType;
import qaa.shop_api.environments.Environment;
import qaa.shop_api.models.Product;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.*;

public class ProductAPI {

    private final String HOST;
    private final Environment env;

    private final String PRODUCTS = "/products";
    private final String SEPARATOR = "/";

    public ProductAPI(Environment env) {
        this.env = env;
        HOST = env.getHost();
    }

    public static ProductAPI get(Environment env) {
        return new ProductAPI(env);
    }

    public List<Product> getAllProduct() {
        String query = HOST + PRODUCTS;
        return when().get(query)
                .then().log().body()
                .extract().body().jsonPath().getList("", Product.class);
    }

    public void addNewProduct(String description, String id, int manufacturer, float price) {
        String query = HOST + PRODUCTS;
        Product product = new Product(description, id, manufacturer, price);
        given().contentType(ContentType.JSON).body(product)
                .when().post(query)
                .then().log().body();
    }

    public Product getProductDescription(String description) {
        Product[] listOfProducts = getListOfProducts();
        return Arrays.stream(listOfProducts)
                .filter(product -> product.getDescription().equals(description))
                .limit(1)
                .collect(Collectors.toList())
                .get(0);
    }

    public Product[] getListOfProducts() {
        String query = HOST + PRODUCTS;
        return when().get(query)
                .then().extract().body().as(Product[].class);
    }

    public Product updateProduct(Product productToBeUpdated) {
        String query = HOST + PRODUCTS + SEPARATOR + "1";
        return given().contentType(ContentType.JSON).body(productToBeUpdated)
                .when().put(query)
                .then().extract().body().as(Product.class);
    }

    public Product getProductById(String id) {
        return when().get(HOST + PRODUCTS + SEPARATOR + id)
                .then().extract().body().as(Product.class);
    }

    public boolean deleteProductById(String id) {
        String query = HOST + PRODUCTS + SEPARATOR + id;
        return when().delete(query)
                .then().extract().body().as(Boolean.class);
    }
}
