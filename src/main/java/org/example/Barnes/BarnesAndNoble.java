// src/main/java/org/example/Barnes/BarnesAndNoble.java
package org.example.Barnes;

import java.util.Map;

public class BarnesAndNoble {

    private final BookDatabase bookDatabase;
    private final BuyBookProcess process;

    public BarnesAndNoble(BookDatabase bookDatabase, BuyBookProcess process) {
        this.bookDatabase = bookDatabase;
        this.process = process;
    }

    private void retrieveBook(String ISBN, int quantity, PurchaseSummary purchaseSummary) {
        Book book = bookDatabase.findByISBN(ISBN);
        if (book.getQuantity() < quantity) {
            // record how many were unavailable and cap quantity to available stock
            purchaseSummary.addUnavailable(book, quantity - book.getQuantity());
            quantity = book.getQuantity();
        }
        // accumulate price and perform the buy action
        purchaseSummary.addToTotalPrice(quantity * book.getPrice());
        process.buyBook(book, quantity);
    }

    /** Calculate the total price for a cart of ISBN -> quantity. */
    public PurchaseSummary getPriceForCart(Map<String, Integer> order) {
        if (order == null) return null;

        PurchaseSummary summary = new PurchaseSummary();
        for (Map.Entry<String,Integer> e : order.entrySet()) {
            retrieveBook(e.getKey(), e.getValue(), summary);
        }
        return summary;
    }
}
