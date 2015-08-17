package com.tokko.recipesv2.backend.engines;

import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;

public class IngredientCrudEngine {
    public Ingredient commitIngredient(Ingredient ingredient, RecipeUser user) {
        ingredient.setUser(user);
        ofy().save().entity(ingredient).now();
        return ingredient;
    }

    public void commitIngredients(Iterable<Ingredient> ingredients, RecipeUser user) {
        for (Ingredient i : ingredients) {
            commitIngredient(i, user);
        }
    }
}
