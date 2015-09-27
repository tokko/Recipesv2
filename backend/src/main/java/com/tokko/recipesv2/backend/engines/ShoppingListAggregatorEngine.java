package com.tokko.recipesv2.backend.engines;

import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.entities.ScheduleEntry;
import com.tokko.recipesv2.backend.entities.ShoppingListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingListAggregatorEngine {

    public List<ShoppingListItem> aggregateIngredients(List<ShoppingListItem> items) {
        HashMap<Long, ShoppingListItem> ings = new HashMap<>();
        for (ShoppingListItem item : items) {
            if (ings.containsKey(item.ingredient.getGrocery().getId())) {
                ings.get(item.ingredient.getGrocery().getId()).addIngredientQuantity(item);
            } else {
                ings.put(item.ingredient.getGrocery().getId(), item);
            }
        }

        List<ShoppingListItem> aggregatedItems = new ArrayList<>();
        aggregatedItems.addAll(ings.values());
        return aggregatedItems;
    }

    public List<Recipe> getRecipesFromScheduleEntries(List<ScheduleEntry> scheduleEntries) {
        List<Recipe> recipes = new ArrayList<>();
        for (ScheduleEntry entry : scheduleEntries) {
            recipes.addAll(entry.getRecipes());
        }
        return recipes;
    }

    public List<Ingredient> getIngredientsFromRecipes(List<Recipe> recipes) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (Recipe recipe : recipes) {
            ingredients.addAll(recipe.getIngredients());
        }
        return ingredients;
    }
}
