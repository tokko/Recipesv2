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
        addConversion(Unit.G, Unit.KG, 1000);
        addConversion(Unit.ML, Unit.DL, 100);
    }

    private static void addConversion(String src, String dest, double factor){
        if(!units.containsKey(src)){
            units.put(src, new Unit(src, null, null, factor));
        }
        if(!units.containsKey(dest)){
            units.put(dest, new Unit(dest, null, null, factor));
        }
        Unit srcUnit = units.get(src);
        Unit dstUnit = units.get(dest);
        srcUnit.setUp(dest);
        dstUnit.setDown(src);
    }
    public List<String> listUnits() {
        return new ArrayList<>(units.keySet());
    }

    public Quantity getBaseQuantity(Quantity q) {
        if(!units.containsKey(q.getUnit())) throw new UnsupportedOperationException("Unsupported unit");
        Unit unit = units.get(q.getUnit());
        if(unit.getDown() == null) return q; //are at lowest
        double newQuantity = q.getQuantity() * unit.getFactor();
        if (newQuantity < 1) return q;
        Quantity q1 = new Quantity(unit.getDown());
        q1.setQuantity(newQuantity);
        return getBaseQuantity(q1);
    }

    public Quantity upQuantity(Quantity q) {
        if(!units.containsKey(q.getUnit())) throw new UnsupportedOperationException("Unsupported unit");
        Unit unit = units.get(q.getUnit());
        if(unit.getUp() == null) return q; //are at highest
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
