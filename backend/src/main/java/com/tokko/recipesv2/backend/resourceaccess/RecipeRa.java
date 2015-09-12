package com.tokko.recipesv2.backend.resourceaccess;

import com.googlecode.objectify.Key;
import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import java.util.List;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;

public class RecipeRa {
    public List<Recipe> listRecipesForUser(RecipeUser user) {
        List<Recipe> list = ofy().load().type(Recipe.class).ancestor(user).list();
        for (Recipe r : list) {
            r.load();
        }
        return list;
    }

    public Recipe commitRecipe(Recipe recipe, RecipeUser user) {
        recipe.setRecipeUser(user);
        recipe.prepare();
        ofy().save().entity(recipe).now();
        return recipe;
    }

    public Recipe getRecipe(RecipeUser user, Long id) {
        Recipe recipe = ofy().load().type(Recipe.class).parent(user).id(id).now();
        recipe.load();
        return recipe;
    }
}
