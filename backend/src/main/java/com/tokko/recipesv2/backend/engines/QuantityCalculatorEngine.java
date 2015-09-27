package com.tokko.recipesv2.backend.engines;

import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.entities.ShoppingListItem;
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
            return getBaseQuantity(q1);
        }
        throw new UnsupportedOperationException("Unsupported unit");
    }

    public Quantity upQuantity(Quantity q) {
        if (q.getUnit().equals(Units.G)) {
            double newQuantity = q.getQuantity() / 1000;
            if (newQuantity < 1) return q;
            Quantity q1 = new Quantity(Units.KG);
            q1.setQuantity(newQuantity);
            return upQuantity(q1);
        }
        if (q.getUnit().equals(Units.KG)) {
            return q;
        }
        throw new UnsupportedOperationException("Unsupported unit");
    }

    public List<Ingredient> toBaseQuantities(List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            ingredient.setQuantity(getBaseQuantity(ingredient.getQuantity()));
        }
        return ingredients;
    }

    public List<Ingredient> upQuantities(List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            ingredient.setQuantity(upQuantity(ingredient.getQuantity()));
        }
        return ingredients;
    }

    public List<ShoppingListItem> toBaseQuantitiesShoppingList(List<ShoppingListItem> ingredients) {
        for (ShoppingListItem ingredient : ingredients) {
            ingredient.ingredient.setQuantity(getBaseQuantity(ingredient.ingredient.getQuantity()));
        }
        return ingredients;
    }

    public List<ShoppingListItem> upQuantitiesShoppingList(List<ShoppingListItem> ingredients) {
        for (ShoppingListItem ingredient : ingredients) {
            ingredient.ingredient.setQuantity(upQuantity(ingredient.ingredient.getQuantity()));
        }
        return ingredients;
    }
}
