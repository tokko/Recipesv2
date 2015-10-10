package com.tokko.recipesv2.backend.engines;

import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.entities.ShoppingListItem;
import com.tokko.recipesv2.backend.units.Quantity;
import com.tokko.recipesv2.backend.units.Unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class QuantityCalculatorEngine {
    private static final HashMap<String, Unit> units = new HashMap<>();

    static {
        units.put(Unit.G, new Unit(Unit.G, Unit.KG, null, 1000));
        units.put(Unit.KG, new Unit(Unit.KG, null, Unit.G, 1000));
    }

    public List<String> listUnits() {
        return new ArrayList<>(units.keySet());
    }

    public Quantity getBaseQuantity(Quantity q) {
        if (q.getUnit().equals(Unit.G)) {
            return q;
        }
        if (q.getUnit().equals(Unit.KG)) {
            Quantity q1 = new Quantity(Unit.G);
            q1.setQuantity(q.getQuantity() * 1000);
            return getBaseQuantity(q1);
        }
        throw new UnsupportedOperationException("Unsupported unit");
    }

    public Quantity upQuantity(Quantity q) {
        if(!units.containsKey(q.getUnit())) throw new UnsupportedOperationException("Unsupported unit");
        Unit unit = units.get(q.getUnit());
        double newQuantity = q.getQuantity() / unit.getFactor();
        if (newQuantity < 1) return q;
        Quantity q1 = new Quantity(unit.getUp());
        q1.setQuantity(newQuantity);
        return upQuantity(q1);
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
