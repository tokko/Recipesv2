package com.tokko.recipesv2.backend.engines;

import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.entities.ShoppingListItem;
import com.tokko.recipesv2.backend.units.Quantity;
import com.tokko.recipesv2.backend.units.Unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuantityCalculatorEngine {
    private static final HashMap<String, Unit> units = new HashMap<>();

    static {
        addConversion(Unit.G, Unit.KG, 1000);
        addConversion(Unit.ML, Unit.TEASPOON, 4.92892);
        addConversion(Unit.TEASPOON, Unit.TBLSPOON, 3);
        addConversion(Unit.TBLSPOON, Unit.DL, 6.7628);
        addConversion(Unit.DL, Unit.L, 10);
    }

    private static void addConversion(String src, String dest, double factorUp){
        addConversion(src, dest, factorUp, factorUp);
    }

    private static void addConversion(String src, String dest, double factorUp, double factorDown){
        if(!units.containsKey(src)){
            units.put(src, new Unit(src, null, null, factorUp, factorDown));
        }
        if(!units.containsKey(dest)){
            units.put(dest, new Unit(dest, null, null, factorUp, factorDown));
        }

        Unit srcUnit = units.get(src);
        srcUnit.setUp(dest);
        srcUnit.setFactorUp(factorUp);

        Unit dstUnit = units.get(dest);
        dstUnit.setDown(src);
        dstUnit.setFactorDown(factorDown);
    }
    public List<String> listUnits() {
        return new ArrayList<>(units.keySet());
    }

    public Quantity getBaseQuantity(Quantity q) {
        if(!units.containsKey(q.getUnit())) throw new UnsupportedOperationException("Unsupported unit");
        Unit unit = units.get(q.getUnit());
        if(unit.getDown() == null) return q; //are at lowest
        double newQuantity = q.getQuantity() * unit.getFactorDown();
        if (newQuantity < 1) return q; //TODO: is this necessary?
        Quantity q1 = new Quantity(unit.getDown());
        q1.setQuantity(newQuantity);
        return getBaseQuantity(q1);
    }

    public Quantity upQuantity(Quantity q) {
        if(!units.containsKey(q.getUnit())) throw new UnsupportedOperationException("Unsupported unit");
        Unit unit = units.get(q.getUnit());
        if(unit.getUp() == null) return q; //are at highest
        double newQuantity = q.getQuantity() / unit.getFactorUp();
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
