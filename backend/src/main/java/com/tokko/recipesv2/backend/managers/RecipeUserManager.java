package com.tokko.recipesv2.backend.managers;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.endpoints.Constants;
import com.tokko.recipesv2.backend.entities.RecipeUser;
import com.tokko.recipesv2.backend.entities.ShoppingList;
import com.tokko.recipesv2.backend.resourceaccess.RecipeUserRa;
import com.tokko.recipesv2.backend.resourceaccess.ShoppingListRa;

public class RecipeUserManager {
    private RecipeUserRa recipeUserRa;
    private ShoppingListRa shoppingListRa;

    @Inject
    public RecipeUserManager(RecipeUserRa recipeUserRa, ShoppingListRa shoppingListRa) {
        this.recipeUserRa = recipeUserRa;
        this.shoppingListRa = shoppingListRa;
    }

    public void addRegistrationIdToRecipeUser(String email, String registrationId) {
        RecipeUser user = recipeUserRa.getUserByEmail(email);
        if (user == null) {
            user = recipeUserRa.createUser(email);
            ShoppingList sl = new ShoppingList();
            sl.setId(Constants.GENERAL_LIST_ID);
            shoppingListRa.commitShoppingList(sl, user);
        }
        user.getRegistrationIds().add(registrationId);
        recipeUserRa.persistUser(user);
    }
}
