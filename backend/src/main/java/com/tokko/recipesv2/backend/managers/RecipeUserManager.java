package com.tokko.recipesv2.backend.managers;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.resourceaccess.RecipeUserRa;
import com.tokko.recipesv2.backend.entities.RecipeUser;

public class RecipeUserManager {
    private RecipeUserRa recipeUserRa;

    @Inject
    public RecipeUserManager(RecipeUserRa recipeUserRa) {
        this.recipeUserRa = recipeUserRa;
    }

    public void addRegistrationIdToRecipeUser(String email, String registrationId) {
        RecipeUser user = recipeUserRa.getUserByEmail(email);
        if (user == null)
            user = recipeUserRa.createUser(email);
        user.getRegistrationIds().add(registrationId);
        recipeUserRa.persistUser(user);
    }
}
