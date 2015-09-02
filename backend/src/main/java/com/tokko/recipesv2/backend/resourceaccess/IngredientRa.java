package com.tokko.recipesv2.backend.resourceaccess;

import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import java.util.ArrayList;
import java.util.List;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;

public class IngredientRa {
    public Ingredient commitIngredient(Ingredient ingredient, RecipeUser user) {
        ingredient.setUser(user);
        ingredient.prepare();
        ofy().save().entity(ingredient).now();
        return ingredient;
    }

    public void commitIngredients(Iterable<Ingredient> ingredients, RecipeUser user) {
        for (Ingredient i : ingredients) {
            commitIngredient(i, user);
        }
    }

    public List<Ingredient> getIngredientsToDelete(Recipe recipe, Recipe existing) {
        List<Ingredient> toDelete = new ArrayList<>();
        for (Ingredient existingIngredient : existing.getIngredients()) {
            if (!recipe.getIngredients().contains(existingIngredient))
                toDelete.add(existingIngredient);
        }
        return toDelete;
    }

    public void deleteIngredients(Iterable<Ingredient> toDelete) {
        ofy().delete().entities(toDelete).now();
    }
}
