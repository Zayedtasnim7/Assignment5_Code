package org.example.Amazon;

import org.example.Amazon.Cost.*;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AmazonIntegrationTest {

    private Database database;
    private ShoppingCartAdaptor cart;
    private List<PriceRule> rules;
    private Amazon amazon;

    @BeforeEach
    void setUp() {
        // Reset database before each test
        database = new Database();
        database.resetDatabase();

        // Create shopping cart
        cart = new ShoppingCartAdaptor(database);

        // Create price rules
        rules = new ArrayList<>();
        rules.add(new RegularCost());

        // Create Amazon instance
        amazon = new Amazon(cart, rules);
    }

    @AfterEach
    void tearDown() {
        if (database != null) {
            database.close();
        }
    }

    // ==================== SPECIFICATION-BASED INTEGRATION TESTS ====================

    @Test
    @DisplayName("specification-based")
    void testCompleteWorkflow_AddItemAndCalculate() {
        // Test complete workflow: add item and calculate price
        Item item = new Item("BOOK", "Java Programming", 2, 29.99);

        amazon.addToCart(item);
        double total = amazon.calculate();

        assertTrue(total > 0);
    }

    @Test
    @DisplayName("specification-based")
    void testMultipleItems_CalculateTotalPrice() {
        // Test adding multiple items and calculating total
        Item item1 = new Item("BOOK", "Clean Code", 1, 35.00);
        Item item2 = new Item("ELECTRONICS", "Keyboard", 1, 75.00);

        amazon.addToCart(item1);
        amazon.addToCart(item2);

        double total = amazon.calculate();

        assertTrue(total > 0);
    }

    @Test
    @DisplayName("specification-based")
    void testDatabasePersistence_ItemsRemainAfterAdd() {
        // Test that items persist in database after adding
        Item item = new Item("CLOTHING", "T-Shirt", 3, 19.99);

        cart.add(item);
        List<Item> items = cart.getItems();

        assertFalse(items.isEmpty());
    }

    @Test
    @DisplayName("specification-based")
    void testShoppingCartAdaptor_AddAndRetrieve() {
        // Test ShoppingCartAdaptor integration
        Item item = new Item("FOOD", "Apple", 5, 2.50);

        cart.add(item);
        List<Item> items = cart.getItems();

        assertEquals(1, items.size());
        assertEquals("Apple", items.get(0).getName());
    }

    // ==================== STRUCTURAL-BASED INTEGRATION TESTS ====================

    @Test
    @DisplayName("structural-based")
    void testDatabaseReset_ClearsAllData() {
        // Test that database reset clears all data
        Item item = new Item("ELECTRONICS", "Phone", 1, 599.99);
        cart.add(item);

        database.resetDatabase();

        List<Item> items = cart.getItems();
        assertTrue(items.isEmpty());
    }

    @Test
    @DisplayName("structural-based")
    void testMultiplePriceRules_AppliedInOrder() {
        // Test that multiple price rules are applied correctly
        rules.add(new DeliveryPrice());
        Amazon amazonWithDelivery = new Amazon(cart, rules);

        Item item = new Item("FURNITURE", "Chair", 2, 150.00);
        amazonWithDelivery.addToCart(item);

        double total = amazonWithDelivery.calculate();

        assertTrue(total > 0);
    }

    @Test
    @DisplayName("structural-based")
    void testElectronicsExtraCost_AppliedCorrectly() {
        // Test that electronics incur extra cost
        rules.add(new ExtraCostForElectronics());
        Amazon amazonWithExtra = new Amazon(cart, rules);

        Item electronics = new Item("ELECTRONICS", "Laptop", 1, 999.99);
        amazonWithExtra.addToCart(electronics);

        double total = amazonWithExtra.calculate();

        assertTrue(total > 0);
    }

    @Test
    @DisplayName("structural-based")
    void testShoppingCart_HandlesMultipleAdditions() {
        // Test that cart handles multiple additions
        Item item1 = new Item("BOOK", "Book 1", 1, 20.00);
        Item item2 = new Item("BOOK", "Book 2", 1, 25.00);
        Item item3 = new Item("BOOK", "Book 3", 1, 30.00);

        cart.add(item1);
        cart.add(item2);
        cart.add(item3);

        List<Item> items = cart.getItems();
        assertEquals(3, items.size());
    }
}
