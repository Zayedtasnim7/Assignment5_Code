package org.example.Amazon;

import org.example.Amazon.Cost.*;
import org.junit.jupiter.api.*;

import org.mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AmazonUnitTest {

    @Mock
    private ShoppingCart mockCart;

    @Mock
    private PriceRule mockRule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ==================== SPECIFICATION-BASED UNIT TESTS ====================

    @Test
    @DisplayName("specification-based")
    void testItem_ValidConstruction() {
        Item item = new Item("BOOK", "Test Book", 5, 19.99);

        assertEquals("BOOK", item.getType());
        assertEquals("Test Book", item.getName());
        assertEquals(5, item.getQuantity());
        assertEquals(19.99, item.getPricePerUnit(), 1e-9);
    }

    @Test
    @DisplayName("specification-based")
    void testItem_GettersReturnCorrectValues() {
        Item item = new Item("ELECTRONICS", "Headphones", 2, 99.99);

        assertEquals("ELECTRONICS", item.getType());
        assertEquals("Headphones", item.getName());
        assertEquals(2, item.getQuantity());
        assertEquals(99.99, item.getPricePerUnit(), 1e-9);
    }

    @Test
    @DisplayName("specification-based")
    void testAmazon_Calculate_WithMockedRule() {
        List<Item> items = new ArrayList<>();
        items.add(new Item("BOOK", "Book", 1, 25.00));

        when(mockCart.getItems()).thenReturn(items);
        when(mockRule.priceToAggregate(items)).thenReturn(25.00);

        Amazon testAmazon = new Amazon(mockCart, Arrays.asList(mockRule));
        double total = testAmazon.calculate();

        assertEquals(25.00, total, 1e-9);
        verify(mockRule, times(1)).priceToAggregate(items);
    }

    @Test
    @DisplayName("specification-based")
    void testAmazon_AddToCart_CallsCartAdd() {
        Item item = new Item("CLOTHING", "Jacket", 1, 75.00);

        doNothing().when(mockCart).add(item);

        Amazon testAmazon = new Amazon(mockCart, new ArrayList<>());
        testAmazon.addToCart(item);

        verify(mockCart, times(1)).add(item);
    }

    @Test
    @DisplayName("specification-based")
    void testAmazon_Calculate_EmptyCart() {
        when(mockCart.getItems()).thenReturn(new ArrayList<>());
        when(mockRule.priceToAggregate(any())).thenReturn(0.0);

        Amazon testAmazon = new Amazon(mockCart, Arrays.asList(mockRule));
        double total = testAmazon.calculate();

        assertEquals(0.0, total, 1e-9);
    }

    // ==================== STRUCTURAL-BASED UNIT TESTS ====================

    @Test
    @DisplayName("structural-based")
    void testAmazon_Calculate_MultipleRules() {
        PriceRule rule1 = mock(PriceRule.class);
        PriceRule rule2 = mock(PriceRule.class);

        List<Item> items = Arrays.asList(
                new Item("BOOK", "Book", 1, 20.00)
        );

        when(mockCart.getItems()).thenReturn(items);
        when(rule1.priceToAggregate(items)).thenReturn(20.00);
        when(rule2.priceToAggregate(items)).thenReturn(5.00);

        Amazon testAmazon = new Amazon(mockCart, Arrays.asList(rule1, rule2));
        double total = testAmazon.calculate();

        assertEquals(25.00, total, 1e-9);
        verify(rule1, times(1)).priceToAggregate(items);
        verify(rule2, times(1)).priceToAggregate(items);
    }

    @Test
    @DisplayName("structural-based")
    void testItem_AllItemTypes() {
        Item book = new Item("BOOK", "Book", 1, 15.00);
        Item electronics = new Item("ELECTRONICS", "Phone", 1, 500.00);
        Item clothing = new Item( "CLOTHING", "Shirt", 2, 25.00); // <-- fixed spelling
        Item food = new Item("FOOD", "Snack", 5, 3.00);
        Item furniture = new Item("FURNITURE", "Table", 1, 200.00);

        assertEquals("BOOK", book.getType());
        assertEquals("ELECTRONICS", electronics.getType());
        assertEquals("CLOTHING", clothing.getType());
        assertEquals("FOOD", food.getType());
        assertEquals("FURNITURE", furniture.getType());
    }

    @Test
    @DisplayName("structural-based")
    void testAmazon_Calculate_AccumulatesAllRules() {
        PriceRule rule1 = mock(PriceRule.class);
        PriceRule rule2 = mock(PriceRule.class);
        PriceRule rule3 = mock(PriceRule.class);

        List<Item> items = new ArrayList<>();
        when(mockCart.getItems()).thenReturn(items);
        when(rule1.priceToAggregate(items)).thenReturn(10.0);
        when(rule2.priceToAggregate(items)).thenReturn(15.0);
        when(rule3.priceToAggregate(items)).thenReturn(20.0);

        Amazon testAmazon = new Amazon(mockCart, Arrays.asList(rule1, rule2, rule3));
        double total = testAmazon.calculate();

        assertEquals(45.0, total, 1e-9);
    }

    @Test
    @DisplayName("structural-based")
    void testItem_QuantityAndPriceCalculation() {
        Item item = new Item("BOOK", "Programming Book", 3, 35.50);

        double expectedTotal = 3 * 35.50;
        double actualTotal = item.getQuantity() * item.getPricePerUnit();

        assertEquals(expectedTotal, actualTotal, 1e-9);
    }

    @Test
    @DisplayName("structural-based")
    void testAmazon_AddToCart_MultipleItems() {
        Item item1 = new Item("BOOK", "Book 1", 1, 20.00);
        Item item2 = new Item("BOOK", "Book 2", 1, 30.00);

        doNothing().when(mockCart).add(any(Item.class));

        Amazon testAmazon = new Amazon(mockCart, new ArrayList<>());
        testAmazon.addToCart(item1);
        testAmazon.addToCart(item2);

        verify(mockCart, times(2)).add(any(Item.class));
    }

    @Test
    @DisplayName("structural-based")
    void testAmazon_Calculate_WithZeroPrice() {
        List<Item> items = new ArrayList<>();
        when(mockCart.getItems()).thenReturn(items);
        when(mockRule.priceToAggregate(items)).thenReturn(0.0);

        Amazon testAmazon = new Amazon(mockCart, Arrays.asList(mockRule));
        double total = testAmazon.calculate();

        assertEquals(0.0, total, 1e-9);
    }
}
