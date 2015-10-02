package com.tokko.recipesv2.backend.engines;


import com.tokko.recipesv2.backend.entities.ShoppingListItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ShoppingListGeneralListPreparerEngine {

    public List<ShoppingListItem> markItemsAsGeneralList(List<ShoppingListItem> items) {
        for (ShoppingListItem item : items) {
            item.generated = false;
        }
        return items;
    }

    public List<ShoppingListItem> getGeneralItems(List<ShoppingListItem> items) {
        List<ShoppingListItem> generalItems = new ArrayList<>();
        for (ShoppingListItem item : items) {
            if(!item.generated)
                generalItems.add(item);
        }
        return generalItems;
    }

    public List<ShoppingListItem> getGeneratedItems(List<ShoppingListItem> items) {
        List<ShoppingListItem> generatedItems = new ArrayList<>();
        for (ShoppingListItem item : items) {
            if(item.generated)
                generatedItems.add(item);
        }
        return generatedItems;
    }

}
