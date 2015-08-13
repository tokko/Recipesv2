package com.tokko.recipesv2.backend.managers;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.engines.GroceryCrudEngine;
import com.tokko.recipesv2.backend.engines.MessagingEngine;
import com.tokko.recipesv2.backend.engines.RecipeUserCrudEngine;
import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import java.util.List;

public class GroceryManager {
    private RecipeUserCrudEngine recipeUserCrudEngine;
    private GroceryCrudEngine groceryCrudEngine;
    private MessagingEngine messagingEngine;

    @Inject
    public GroceryManager(RecipeUserCrudEngine recipeUserCrudEngine, GroceryCrudEngine groceryCrudEngine, MessagingEngine messagingEngine) {
        this.recipeUserCrudEngine = recipeUserCrudEngine;
        this.groceryCrudEngine = groceryCrudEngine;
        this.messagingEngine = messagingEngine;
    }

    public List<Grocery> listGroceries(String email) {
        RecipeUser user = recipeUserCrudEngine.getUserByEmail(email);
        return groceryCrudEngine.listGroceries(user);
    }

    public Grocery commitGrocery(Grocery grocery, String email) {
        RecipeUser user = recipeUserCrudEngine.getUserByEmail(email);
        Grocery save = groceryCrudEngine.save(grocery, user);
        if (save != null)
            messagingEngine.sendMessage(save, user);
        return save;
    }
}
