package com.tokko.recipesv2.backend.managers;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.resourceaccess.GroceryRa;
import com.tokko.recipesv2.backend.engines.MessagingEngine;
import com.tokko.recipesv2.backend.resourceaccess.RecipeUserRa;
import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import java.util.List;

public class GroceryManager {
    private RecipeUserRa recipeUserRa;
    private GroceryRa groceryRa;
    private MessagingEngine messagingEngine;

    @Inject
    public GroceryManager(RecipeUserRa recipeUserRa, GroceryRa groceryRa, MessagingEngine messagingEngine) {
        this.recipeUserRa = recipeUserRa;
        this.groceryRa = groceryRa;
        this.messagingEngine = messagingEngine;
    }

    public List<Grocery> listGroceries(String email) {
        RecipeUser user = recipeUserRa.getUserByEmail(email);
        return groceryRa.listGroceries(user);
    }

    public Grocery commitGrocery(Grocery grocery, String email) {
        RecipeUser user = recipeUserRa.getUserByEmail(email);
        return commitGrocery(grocery, user);
    }

    public Grocery commitGrocery(Grocery grocery, RecipeUser user) {
        Grocery save = groceryRa.save(grocery, user);
        if (save != null)
            messagingEngine.sendMessage(save, user);
        return save;
    }

    public void commitGroceries(Iterable<Grocery> groceries, String email) {
        RecipeUser user = recipeUserRa.getUserByEmail(email);
        for (Grocery g : groceries) {
            commitGrocery(g, user);
        }
    }
}
