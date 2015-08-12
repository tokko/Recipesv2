package com.tokko.recipesv2.backend.managers;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.engines.RecipeCrudEngine;
import com.tokko.recipesv2.backend.engines.RecipeUserCrudEngine;
import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import java.util.List;

public class RecipeManager {
    private RecipeUserCrudEngine recipeUserCrudEngine;
    private RecipeCrudEngine recipeCrudEngine;

    @Inject
    public RecipeManager(RecipeUserCrudEngine recipeUserCrudEngine, RecipeCrudEngine recipeCrudEngine) {
        this.recipeUserCrudEngine = recipeUserCrudEngine;
        this.recipeCrudEngine = recipeCrudEngine;
    }

    public List<Recipe> listRecipesForUser(String email) {
        RecipeUser user = recipeUserCrudEngine.getUserByEmail(email);
        return recipeCrudEngine.listRecipesForUser(user);
    }
}
