package com.tokko.recipesv2.backend.engines;


import com.tokko.recipesv2.backend.entities.ShoppingListItem;

import java.util.List;

public class ShoppingListGeneralListPreparerEngine {

    public List<ShoppingListItem> markItemsAsGeneralList(List<ShoppingListItem> items) {
        for (ShoppingListItem item : items) {
            item.generated = false;
        }
        return items;
    }
}
