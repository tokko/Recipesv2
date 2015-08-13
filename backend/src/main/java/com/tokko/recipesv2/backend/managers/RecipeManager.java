package com.tokko.recipesv2.backend.managers;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.engines.MessagingEngine;
import com.tokko.recipesv2.backend.engines.RecipeCrudEngine;
import com.tokko.recipesv2.backend.engines.RecipeUserCrudEngine;
import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import java.util.List;

public class RecipeManager {
    private RecipeUserCrudEngine recipeUserCrudEngine;
    private RecipeCrudEngine recipeCrudEngine;
    private MessagingEngine messagingEngine;

    @Inject
    public RecipeManager(RecipeUserCrudEngine recipeUserCrudEngine, RecipeCrudEngine recipeCrudEngine, MessagingEngine messagingEngine) {
        this.recipeUserCrudEngine = recipeUserCrudEngine;
        this.recipeCrudEngine = recipeCrudEngine;
        this.messagingEngine = messagingEngine;
    }

    public List<Recipe> listRecipesForUser(String email) {
        RecipeUser user = recipeUserCrudEngine.getUserByEmail(email);
        return recipeCrudEngine.listRecipesForUser(user);
    }

    public Recipe commitRecipe(Recipe recipe, String email) {
        RecipeUser user = recipeUserCrudEngine.getUserByEmail(email);
        Recipe save = recipeCrudEngine.commitRecipe(recipe, user);
        if (save != null)
            messagingEngine.sendMessage(save, user);
        return save;
    }
}
