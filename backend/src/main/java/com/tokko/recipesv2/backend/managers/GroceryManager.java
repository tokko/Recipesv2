package com.tokko.recipesv2.backend.managers;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.engines.GroceryCrudEngine;
import com.tokko.recipesv2.backend.engines.RecipeUserCrudEngine;
import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import java.util.List;

public class GroceryManager {
    private RecipeUserCrudEngine recipeUserCrudEngine;
    private GroceryCrudEngine groceryCrudEngine;

    @Inject
    public GroceryManager(RecipeUserCrudEngine recipeUserCrudEngine, GroceryCrudEngine groceryCrudEngine) {
        this.recipeUserCrudEngine = recipeUserCrudEngine;
        this.groceryCrudEngine = groceryCrudEngine;
    }

    public List<Grocery> listGroceries(String email) {
        RecipeUser user = recipeUserCrudEngine.getUserByEmail(email);
        return groceryCrudEngine.listGroceries(user);
    }

    public Grocery commitGrocery(Grocery grocery, String email) {
        RecipeUser user = recipeUserCrudEngine.getUserByEmail(email);
        return groceryCrudEngine.save(grocery, user);
    }
}
