package com.tokko.recipesv2.backend.engines;


import com.tokko.recipesv2.backend.entities.ShoppingListItem;

import java.util.List;
import java.util.function.Consumer;

public class ShoppingListGeneralListPreparerEngine {

    public List<ShoppingListItem> markItemsAsGeneralList(List<ShoppingListItem> items) {
        items.stream().forEach(new Consumer<ShoppingListItem>() {
            @Override
            public void accept(ShoppingListItem shoppingListItem) {
                shoppingListItem.generated = false;
            }
        });
        return items;
    }
}
