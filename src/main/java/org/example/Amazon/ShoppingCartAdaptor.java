package org.example.Amazon;

import java.util.ArrayList;
import java.util.List;

/** Adaptor that persists and loads items from the shoppingcart table. */
public class ShoppingCartAdaptor implements ShoppingCart {

    private final Database connection;

    public ShoppingCartAdaptor(Database connection) {
        this.connection = connection;
    }

    @Override
    public void add(Item item) {
        connection.withSql(() -> {
            try (var ps = connection.getConnection().prepareStatement(
                    "insert into shoppingcart (name, type, quantity, priceperunit) values (?,?,?,?)")) {

                ps.setString(1, item.getName());
                ps.setString(2, item.getType());       // <-- type is a String
                ps.setInt(3, item.getQuantity());
                ps.setDouble(4, item.getPricePerUnit());
                ps.executeUpdate();

                connection.getConnection().commit();
            }
            return null;
        });
    }

    @Override
    public List<Item> getItems() {
        return connection.withSql(() -> {
            try (var ps = connection.getConnection().prepareStatement(
                    "select name, type, quantity, priceperunit from shoppingcart");
                 var rs = ps.executeQuery()) {

                List<Item> items = new ArrayList<>();
                while (rs.next()) {
                    items.add(new Item(
                            rs.getString("type"),          // <-- pass String, not ItemType
                            rs.getString("name"),
                            rs.getInt("quantity"),
                            rs.getDouble("priceperunit")));
                }
                return items;
            }
        });
    }

    @Override
    public int numberOfItems() {
        return connection.withSql(() -> {
            try (var ps = connection.getConnection().prepareStatement(
                    "select count(*) from shoppingcart");
                 var rs = ps.executeQuery()) {

                return rs.next() ? rs.getInt(1) : 0;   // <-- correct way to read count(*)
            }
        });
    }
}
