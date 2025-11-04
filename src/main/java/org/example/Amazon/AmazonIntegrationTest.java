package org.example.Amazon;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class AmazonIntegrationTest {

    private Database database;
    private Amazon amazon;
    private ShoppingCart cart;

    @BeforeEach
    void setUp() {
        // Reset database before each test
        database = new Database();
        database.clear(); // Implement if needed
        amazon = new Amazon(database);
        cart = new ShoppingCart();
    }

    // SPECIFICATION-BASED INTEGRATION TESTS

    @Test
    @DisplayName("specification-based")
    void testCompleteCheckoutProcess_MultipleItems() {
        // Test the full workflow: add items -> calculate cost -> checkout
        Item item1 = new Item("Laptop", 999.99, "ELECTRONICS");
        Item item2 = new Item("Mouse", 29.99, "ELECTRONICS");

        database.addItem(item1);
        database.addItem(item2);

        cart.addItem(item1);
        cart.addItem(item2);

        double total = amazon.calculateTotalCost(cart);

        assertTrue(total > 0);
        assertEquals(2, cart.getItemCount());
    }

    @Test
    @DisplayName("specification-based")
    void testDatabasePersistence_ItemRetrieval() {
        // Test that items persist in database across operations
        Item item = new Item("Book", 19.99, "BOOKS");
        database.addItem(item);

        Item retrieved = database.getItem(item.getId());

        assertNotNull(retrieved);
        assertEquals("Book", retrieved.getName());
        assertEquals(19.99, retrieved.getPrice());
    }

    // STRUCTURAL-BASED INTEGRATION TESTS

    @Test
    @DisplayName("structural-based")
    void testShoppingCartAdaptor_Integration() {
        // Test the adaptor pattern integration
        ShoppingCartAdaptor adaptor = new ShoppingCartAdaptor(cart);
        Item item = new Item("Test", 50.00, "TEST");

        adaptor.add(item);

        assertTrue(adaptor.getCart().contains(item));
    }

    @Test
    @DisplayName("structural-based")
    void testCostCalculation_WithDifferentPriceRules() {
        // Test integration of cost calculation with various rules
        Item electronics = new Item("Phone", 500.00, "ELECTRONICS");
        cart.addItem(electronics);

        // Test different cost calculation paths
        double regularCost = amazon.calculateRegularCost(cart);
        double deliveryCost = amazon.calculateWithDelivery(cart);

        assertTrue(deliveryCost >= regularCost);
    }
}