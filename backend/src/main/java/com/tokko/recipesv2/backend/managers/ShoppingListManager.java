package com.tokko.recipesv2.backend.managers;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.endpoints.Constants;
import com.tokko.recipesv2.backend.engines.ShoppingListGeneralListPreparerEngine;
import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.RecipeUser;
import com.tokko.recipesv2.backend.entities.ShoppingList;
import com.tokko.recipesv2.backend.entities.ShoppingListItem;
import com.tokko.recipesv2.backend.resourceaccess.RecipeUserRa;
import com.tokko.recipesv2.backend.resourceaccess.ShoppingListRa;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListManager {

    private ShoppingListRa shoppingListRa;
    private RecipeUserRa recipeUserRa;
    private ShoppingListGeneralListPreparerEngine shoppingListGeneralListPreparerEngine;
    private GroceryManager groceryManager;

    @Inject
    public ShoppingListManager(ShoppingListRa shoppingListRa, RecipeUserRa recipeUserRa, ShoppingListGeneralListPreparerEngine shoppingListGeneralListPreparerEngine, GroceryManager groceryManager) {
        this.shoppingListRa = shoppingListRa;
        this.recipeUserRa = recipeUserRa;
        this.shoppingListGeneralListPreparerEngine = shoppingListGeneralListPreparerEngine;
        this.groceryManager = groceryManager;
    }

    public ShoppingList getGeneralList(String email) {
        RecipeUser user = recipeUserRa.getUserByEmail(email);
        ShoppingList shoppingList = shoppingListRa.getShoppingList(user, Constants.GENERAL_LIST_ID);
        return shoppingList;
    }

    public void commitShoppingList(final ShoppingList shoppingList, String email) {
        RecipeUser user = recipeUserRa.getUserByEmail(email);
        shoppingList.setUser(user);
        List<Grocery> groceries = new ArrayList<>();
        for (ShoppingListItem item : shoppingList.getItems()) {
            groceries.add(item.ingredient.getGrocery());
        }
        groceryManager.commitGroceries(groceries, email);
        shoppingList.setItems(shoppingListGeneralListPreparerEngine.markItemsAsGeneralList(shoppingList.getItems()));
        shoppingListRa.commitShoppingList(shoppingList);
    }
}
