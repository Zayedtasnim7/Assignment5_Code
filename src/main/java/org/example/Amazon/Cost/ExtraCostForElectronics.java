package org.example.Amazon.Cost;

import org.example.Amazon.Item;
import java.util.List;

public class ExtraCostForElectronics implements PriceRule {

    private final double warrantyRate;

    public ExtraCostForElectronics() { this(0.10); }

    public ExtraCostForElectronics(double warrantyRate) {
        this.warrantyRate = warrantyRate;
    }

    @Override
    public double priceToAggregate(List<Item> items) {
        double extra = 0.0;
        for (Item it : items) {
            if ("ELECTRONICS".equalsIgnoreCase(it.getType())) {
                extra += it.getQuantity() * it.getPricePerUnit() * warrantyRate;
            }
        }
        return extra;
    }
}
