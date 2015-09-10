package com.tokko.recipesv2.backend.engines;

import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.units.Quantity;
import com.tokko.recipesv2.backend.units.Units;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

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
            return getBaseQuantity(q1);
        }
        throw new UnsupportedOperationException("Unsupported unit");
    }

    public Quantity upQuantity(Quantity q) {
        if (q.getUnit().equals(Units.G)) {
            Quantity q1 = new Quantity(Units.KG);
            q1.setQuantity(q.getQuantity() / 1000);
            return upQuantity(q1);
        }
        if (q.getUnit().equals(Units.KG)) {
            return q;
        }
        throw new UnsupportedOperationException("Unsupported unit");
    }

    public List<Ingredient> toBaseQuantities(List<Ingredient> ingredients) {
        ingredients.stream().forEach(new Consumer<Ingredient>() {
            @Override
            public void accept(Ingredient ingredient) {
                ingredient.setQuantity(getBaseQuantity(ingredient.getQuantity()));
            }
        });
        return ingredients;
    }

    public List<Ingredient> upQuantities(List<Ingredient> ingredients) {
        ingredients.stream().forEach(new Consumer<Ingredient>() {
            @Override
            public void accept(Ingredient ingredient) {
                ingredient.setQuantity(upQuantity(ingredient.getQuantity()));
            }
        });
        return ingredients;
    }
}
