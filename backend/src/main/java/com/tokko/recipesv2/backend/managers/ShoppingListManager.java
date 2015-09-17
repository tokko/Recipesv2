package com.tokko.recipesv2.backend.managers;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.endpoints.Constants;
import com.tokko.recipesv2.backend.entities.RecipeUser;
import com.tokko.recipesv2.backend.entities.ShoppingList;
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

    public ShoppingList getGeneralList(String email) {
        RecipeUser user = recipeUserRa.getUserByEmail(email);
        ShoppingList shoppingList = shoppingListRa.getShoppingList(user, Constants.GENERAL_LIST_ID);
        return shoppingList;
    }

    public void commitShoppingList(ShoppingList shoppingList, String email) {
        RecipeUser user = recipeUserRa.getUserByEmail(email);
        shoppingList.setUser(user);
        shoppingListRa.commitShoppingList(shoppingList);
    }
}
