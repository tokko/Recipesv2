package com.tokko.recipesv2.backend.engines;

import com.tokko.recipesv2.backend.units.Quantity;
import com.tokko.recipesv2.backend.units.Units;

import java.util.Arrays;
import java.util.List;

public class QuantityCalculatorEngine {

    public List<String> listUnits() {
        return Arrays.asList(Units.G, Units.KG);
    }

    public Quantity getBaseQuantity(Quantity q) {
        if (q.getUnit().equals(Units.G)) {
            return q;
        }
        if (q.getUnit().equals(Units.KG)) {
            Quantity q1 = new Quantity(Units.G);
            q1.setQuantity(q.getQuantity() * 1000);
            return q1;
        }
        throw new UnsupportedOperationException("Unsupported unit");
    }

    public Quantity upQuantity(Quantity q) {
        if (q.getUnit().equals(Units.G)) {
            Quantity q1 = new Quantity(Units.KG);
            q1.setQuantity(q.getQuantity() / 1000);
            return q1;
        }
        throw new UnsupportedOperationException("Unsupported unit");
    }
}
