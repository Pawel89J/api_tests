package qaa.shop_api.apis;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import qaa.shop_api.environments.Environment;
import qaa.shop_api.environments.EnvironmentManager;
import qaa.shop_api.models.Product;

import java.util.Arrays;
import java.util.List;

public class BasicProductVerificationWithAPIsTest {

    private ProductAPI productAPI;

    @BeforeClass
    public void setUp() {
        String env = System.getProperty("env");
        Environment currentEnv = EnvironmentManager.getEnvironment(env);
        productAPI = productAPI.get(currentEnv);
    }

    @Test
    public void shouldAddNewProduct() {
        String description = "Yerba Matte";
        String id = "";
        int manufacturer = 8;
        float price = 6.7f;
        int expectedSize = productAPI.getAllProduct().size();

        productAPI.addNewProduct(description, id, manufacturer, price);
        List<Product> productList = productAPI.getAllProduct();
        Assert.assertEquals(productList.size() - expectedSize, 1);
    }

    @Test
    public void shouldUpdateProductById() {
        String description = "Banana";
        Product productToBeUpdated = productAPI.getProductDescription(description);
        float newPrice = 9.3f;
        productToBeUpdated.setPrice(newPrice);
        Product updatedProduct = productAPI.updateProduct(productToBeUpdated);
        Assert.assertEquals(updatedProduct.getPrice(), newPrice);
    }

    @Test
    public void shouldProductListContainPeachAndStrawberry() {
        String expectedDescription1 = "Peach";
        String expectedDescription2 = "Strawberry";
        List<String> listOfProductsDescriptions = Arrays.stream(productAPI.getListOfProducts()).map(Product::getDescription).toList();
        Assert.assertTrue(listOfProductsDescriptions.contains(expectedDescription1)
        && listOfProductsDescriptions.contains(expectedDescription2));
    }

    @Test
    public void isStrawberryPriceDefined() {
        String expectedDescription = "Strawberry";
        float expectedPrice = 18.3f;
        Product productToVerify = productAPI.getProductById("5");
        Assert.assertEquals(productToVerify.getDescription(), expectedDescription);
        Assert.assertEquals(productToVerify.getPrice(), expectedPrice);
    }

    @Test
    public void shouldRemoveRoastedCoffee() {
        String productId = productAPI.getProductDescription("Roasted Coffee").getId();
        Assert.assertTrue(productAPI.deleteProductById(productId));
    }
}
