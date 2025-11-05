package org.example.Amazon;

import org.example.Amazon.Cost.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AmazonUnitTest {

    @Mock
    private Database mockDatabase;

    @Mock
    private ShoppingCart mockCart;

    @InjectMocks
    private Amazon amazon;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // SPECIFICATION-BASED UNIT TESTS WITH MOCKS

    @Test
    @DisplayName("specification-based")
    void testFindItem_ValidId_ReturnsItem() {
        // Mock database behavior
        Item expectedItem = new Item("Test Item", 25.00, "TEST");
        when(mockDatabase.getItem("123")).thenReturn(expectedItem);

        Item result = amazon.findItem("123");

        assertNotNull(result);
        assertEquals("Test Item", result.getName());
        verify(mockDatabase, times(1)).getItem("123");
    }

    @Test
    @DisplayName("specification-based")
    void testCalculateCost_EmptyCart_ReturnsZero() {
        // Mock empty cart
        when(mockCart.isEmpty()).thenReturn(true);
        when(mockCart.getTotalPrice()).thenReturn(0.0);

        double cost = amazon.calculateTotalCost(mockCart);

        assertEquals(0.0, cost);
        verify(mockCart, times(1)).isEmpty();
    }

    @Test
    @DisplayName("specification-based")
    void testAddItem_NullItem_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            amazon.addItemToDatabase(null);
        });
    }

    // STRUCTURAL-BASED UNIT TESTS WITH MOCKS

    @Test
    @DisplayName("structural-based")
    void testDeliveryPriceCalculation_StandardDelivery() {
        // Mock item for delivery calculation
        Item item = spy(new Item("Heavy Item", 100.00, "FURNITURE"));
        DeliveryPrice deliveryPrice = new DeliveryPrice(item);

        double price = deliveryPrice.calculatePrice();

        assertTrue(price > 100.00); // Should include delivery fee
        verify(item, atLeastOnce()).getCategory();
    }

    @Test
    @DisplayName("structural-based")
    void testRegularCost_VerifyCalculationPath() {
        // Test the regular cost calculation branch
        Item mockItem = mock(Item.class);
        when(mockItem.getPrice()).thenReturn(50.00);

        RegularCost regularCost = new RegularCost(mockItem);
        double cost = regularCost.calculatePrice();

        assertEquals(50.00, cost);
        verify(mockItem, times(1)).getPrice();
    }

    @Test
    @DisplayName("structural-based")
    void testExtraCostForElectronics_AddsWarranty() {
        // Test electronics-specific cost path
        Item electronics = mock(Item.class);
        when(electronics.getCategory()).thenReturn("ELECTRONICS");
        when(electronics.getPrice()).thenReturn(200.00);

        ExtraCostForElectronics extraCost = new ExtraCostForElectronics(electronics);
        double totalCost = extraCost.calculatePrice();

        assertTrue(totalCost > 200.00);
        verify(electronics, atLeastOnce()).getCategory();
    }

    @Test
    @DisplayName("structural-based")
    void testShoppingCart_MockedBehavior() {
        // Test cart operations with mocked dependencies
        when(mockCart.getItemCount()).thenReturn(3);
        when(mockCart.getTotalPrice()).thenReturn(150.00);

        int count = mockCart.getItemCount();
        double total = mockCart.getTotalPrice();

        assertEquals(3, count);
        assertEquals(150.00, total);
        verify(mockCart, times(1)).getItemCount();
        verify(mockCart, times(1)).getTotalPrice();
    }
}