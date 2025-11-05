package org.example.Barnes;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class BarnesAndNobleTest {

    private BookDatabase database;
    private BuyBookProcess buyProcess;

    @BeforeEach
    void setUp() {
        database = new BookDatabase();
        buyProcess = new BuyBookProcess(database);
    }

    // SPECIFICATION-BASED TESTS
    // These test the requirements/specifications without looking at code

    @Test
    @DisplayName("specification-based")
    void testBuyBook_ValidISBN_ReturnsPurchaseSummary() {
        // Test boundary: valid ISBN
        Book book = new Book("1234567890", "Test Book", 29.99);
        database.addBook(book);

        PurchaseSummary summary = buyProcess.buyBook("1234567890");

        assertNotNull(summary);
        assertEquals("Test Book", summary.getBookTitle());
        assertEquals(29.99, summary.getPrice());
    }

    @Test
    @DisplayName("specification-based")
    void testBuyBook_InvalidISBN_ReturnsNull() {
        // Test boundary: invalid/non-existent ISBN
        PurchaseSummary summary = buyProcess.buyBook("9999999999");

        assertNull(summary);
    }

    @Test
    @DisplayName("specification-based")
    void testBuyBook_NullISBN_ThrowsException() {
        // Test boundary: null input
        assertThrows(IllegalArgumentException.class, () -> {
            buyProcess.buyBook(null);
        });
    }

    @Test
    @DisplayName("specification-based")
    void testBuyBook_EmptyISBN_ReturnsNull() {
        // Test boundary: empty string
        PurchaseSummary summary = buyProcess.buyBook("");

        assertNull(summary);
    }

    // STRUCTURAL-BASED TESTS
    // These test based on code structure (branches, paths, statements)

    @Test
    @DisplayName("structural-based")
    void testBuyBook_DatabaseLookupPath() {
        // Test the path where database.findBook() is called
        Book book = new Book("1111111111", "Structural Test", 19.99);
        database.addBook(book);

        PurchaseSummary summary = buyProcess.buyBook("1111111111");

        assertNotNull(summary);
        assertTrue(summary.getPrice() > 0);
    }

    @Test
    @DisplayName("structural-based")
    void testBuyBook_NullBookReturnPath() {
        // Test the branch where findBook returns null
        PurchaseSummary summary = buyProcess.buyBook("0000000000");

        assertNull(summary);
    }

    @Test
    @DisplayName("structural-based")
    void testBookDatabase_AddAndRetrieve() {
        // Test internal database operations
        Book book1 = new Book("1234", "Book One", 10.99);
        Book book2 = new Book("5678", "Book Two", 20.99);

        database.addBook(book1);
        database.addBook(book2);

        assertEquals(book1, database.findBook("1234"));
        assertEquals(book2, database.findBook("5678"));
    }

    @Test
    @DisplayName("structural-based")
    void testPurchaseSummary_AllFields() {
        // Test all getters/setters to achieve statement coverage
        PurchaseSummary summary = new PurchaseSummary("Test Book", 25.50, "9876543210");

        assertEquals("Test Book", summary.getBookTitle());
        assertEquals(25.50, summary.getPrice());
        assertEquals("9876543210", summary.getIsbn());
    }
}