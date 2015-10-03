package com.tokko.recipesv2.backend.engines;

import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.entities.ShoppingListItem;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListItemConverterEngine {

    public List<ShoppingListItem> convertIngredientToShoppingListItems(List<Ingredient> ingredients) {
        List<ShoppingListItem> items = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            ShoppingListItem item = new ShoppingListItem(ingredient);
            items.add(item);
        }
        return items;
    }
}
