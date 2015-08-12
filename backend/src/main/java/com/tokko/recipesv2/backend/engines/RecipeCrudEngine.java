package com.tokko.recipesv2.backend.engines;

import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import java.util.List;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;

public class RecipeCrudEngine {
    public List<Recipe> listRecipesForUser(RecipeUser user) {
        return ofy().load().type(Recipe.class).ancestor(user).list();
    }
}
