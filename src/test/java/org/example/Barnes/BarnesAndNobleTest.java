package org.example.Barnes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BarnesAndNobleTest {

    private BookDatabase bookDatabase;
    private BuyBookProcess process;
    private BarnesAndNoble bn;

    private Book bookA;
    private Book bookB;

    @BeforeEach
    void setUp() {
        // These interfaces/abstracts must be mocked
        bookDatabase = mock(BookDatabase.class);
        process = mock(BuyBookProcess.class);

        bn = new BarnesAndNoble(bookDatabase, process);

        // Mock books returned by DB
        bookA = mock(Book.class);
        bookB = mock(Book.class);

        when(bookA.getQuantity()).thenReturn(5);
        when(bookA.getPrice()).thenReturn(10);   // price type in your code is int
        when(bookB.getQuantity()).thenReturn(2);
        when(bookB.getPrice()).thenReturn(25);

        when(bookDatabase.findByISBN("111")).thenReturn(bookA);
        when(bookDatabase.findByISBN("222")).thenReturn(bookB);
    }

    // ===== SPECIFICATION-BASED =====
    @Test
    @DisplayName("specification-based")
    void priceForCart_validOrder_returnsSummary_and_buysEach() {
        Map<String, Integer> order = Map.of("111", 3, "222", 2);

        PurchaseSummary summary = bn.getPriceForCart(order);

        assertNotNull(summary);                   // returns some summary object
        verify(process).buyBook(bookA, 3);       // integration with process
        verify(process).buyBook(bookB, 2);
        verifyNoMoreInteractions(process);
    }

    @Test
    @DisplayName("specification-based")
    void priceForCart_nullOrder_returnsNull() {
        assertNull(bn.getPriceForCart(null));
        verifyNoInteractions(process);
    }

    // ===== STRUCTURAL-BASED =====
    @Test
    @DisplayName("structural-based")
    void whenOrderExceedsInventory_onlyAvailableQtyIsBought() {
        // Ask for more than stock; implementation should clamp to available
        Map<String, Integer> order = Map.of("111", 10);

        PurchaseSummary summary = bn.getPriceForCart(order);

        assertNotNull(summary);
        verify(process).buyBook(bookA, 5);  // clamped to available qty
        verifyNoMoreInteractions(process);
    }
}
