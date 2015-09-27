package com.tokko.recipesv2.backend.engines;

import com.tokko.recipesv2.backend.entities.ShoppingListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingListAggregatorEngine {

    public List<ShoppingListItem> aggregateIngredients(List<ShoppingListItem> items) {
        HashMap<Long, ShoppingListItem> ings = new HashMap<>();
        for (ShoppingListItem item : items) {
            if (ings.containsKey(item.ingredient.getGrocery().getId())) {
                ings.get(item.ingredient.getGrocery().getId()).addIngredientQuantity(item);
            } else {
                ings.put(item.ingredient.getGrocery().getId(), item);
            }
        }

        List<ShoppingListItem> aggregatedItems = new ArrayList<>();
        aggregatedItems.addAll(ings.values());
        return aggregatedItems;
    }

}
