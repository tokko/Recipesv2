package com.tokko.recipesv2.backend.engines;

import com.tokko.recipesv2.backend.entities.Ingredient;

import java.util.List;

public class RecipeRescaleEngine {

    public List<Ingredient> rescaleIngredientsToNumberOfHelpings(List<Ingredient> ingredients, int fromHelpings, int toHelpings) {
        double fraction = ((double) toHelpings) / ((double) fromHelpings);
        for (Ingredient ingredient : ingredients) {
            ingredient.getQuantity().setQuantity(ingredient.getQuantity().getQuantity() * fraction);
        }
        return ingredients;
    }
}
