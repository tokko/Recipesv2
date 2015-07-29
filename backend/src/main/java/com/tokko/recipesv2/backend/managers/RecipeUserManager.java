package com.tokko.recipesv2.backend.managers;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.engines.RecipeUserCrudEngine;
import com.tokko.recipesv2.backend.entities.RecipeUser;

public class RecipeUserManager {
    private RecipeUserCrudEngine recipeUserCrudEngine;

    @Inject
    public RecipeUserManager(RecipeUserCrudEngine recipeUserCrudEngine) {
        this.recipeUserCrudEngine = recipeUserCrudEngine;
    }

    public void addRegistrationIdToRecipeUser(String email, String registrationId) {
        recipeUserCrudEngine.startTransaction();
        RecipeUser user = recipeUserCrudEngine.getUserByEmail(email);
        if (user == null)
            user = recipeUserCrudEngine.createUser(email);
        recipeUserCrudEngine.addRegistrationIdToUser(user, registrationId);
        recipeUserCrudEngine.persistUser(user);
        recipeUserCrudEngine.commitTransaction();
    }
}
