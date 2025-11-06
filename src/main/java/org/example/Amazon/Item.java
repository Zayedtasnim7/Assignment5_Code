package org.example.Amazon;

import org.example.Amazon.Cost.ItemType;

public class Item {
    private final String type;        // stored as string (e.g., "BOOK")
    private final String name;
    private final int quantity;
    private final double pricePerUnit;

    // Your tests use this one: Item("BOOK", "Name", 1, 9.99)
    public Item(String type, String name, int quantity, double pricePerUnit) {
        if (type == null || name == null) {
            throw new IllegalArgumentException("type and name must not be null");
        }
        this.type = type;
        this.name = name;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }

    // Production code / adaptor can use the enum too
    public Item(ItemType type, String name, int quantity, double pricePerUnit) {
        this(type.name(), name, quantity, pricePerUnit);
    }

    // --- Getters used by tests ---
    public String getType() { return type; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPricePerUnit() { return pricePerUnit; }

    // Optional helper if some code still prefers the enum
    public ItemType getTypeEnum() {
        return ItemType.valueOf(type);
    }
}
