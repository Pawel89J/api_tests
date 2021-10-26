package qaa.shop_api.apis;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import qaa.shop_api.environments.Environment;
import qaa.shop_api.environments.EnvironmentManager;
import qaa.shop_api.models.Customer;

import java.util.List;

public class BasicCustomerVerificationsWithAPIsTest {

    private CustomerAPI customerAPI;

    @BeforeClass
    public void setUp() {
        String env = System.getProperty("env");
        Environment currentEnv = EnvironmentManager.getEnvironment(env);
        customerAPI = CustomerAPI.get(currentEnv);
    }

    @Test
    public void shouldGetListOfExistingCustomers() {
        List<Customer> customers = customerAPI.getAllCustomers();
        Assert.assertTrue(customers.size() > 0,
                "Customer list size is 0. Check logs for additional details");
    }

    @Test
    public void shouldUpdateCustomerEmailWithNewOne() {
        String customerId = "3";
        String newEmail = "alex.kowalsky@changedDomain.com";
        Customer alex = customerAPI.updateEmailAddressForCustomer(customerId, newEmail);
        Assert.assertEquals(newEmail, alex.getPerson().getEmail(),
                "Alex's email not updated. Check logs for additional details");
    }

    @Test
    public void checkIfItemWithIdWasAddedToCart() {
        String itemId = "3";
        String quantity = "3";
        String customerId = "4";
        customerAPI.addItemToCustomerCart(customerId, itemId, quantity);
        Customer jo = customerAPI.getCustomersById(customerId);
        Assert.assertTrue(jo.getShoppingCart().getItems().length > 0,
                "Items was not added to cart. Check logs for additional details");
    }
}
