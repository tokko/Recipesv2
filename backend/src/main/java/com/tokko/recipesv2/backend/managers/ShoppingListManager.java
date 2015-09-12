package com.tokko.recipesv2.backend.managers;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.resourceaccess.RecipeUserRa;
import com.tokko.recipesv2.backend.resourceaccess.ShoppingListRa;

public class ShoppingListManager {

    private ShoppingListRa shoppingListRa;
    private RecipeUserRa recipeUserRa;

    @Inject
    public ShoppingListManager(ShoppingListRa shoppingListRa, RecipeUserRa recipeUserRa) {
        this.shoppingListRa = shoppingListRa;
        this.recipeUserRa = recipeUserRa;
    }
}
