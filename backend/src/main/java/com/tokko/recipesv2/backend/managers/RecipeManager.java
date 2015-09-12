package com.tokko.recipesv2.backend.managers;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.resourceaccess.IngredientRa;
import com.tokko.recipesv2.backend.engines.IngredientEngine;
import com.tokko.recipesv2.backend.engines.MessagingEngine;
import com.tokko.recipesv2.backend.resourceaccess.RecipeRa;
import com.tokko.recipesv2.backend.resourceaccess.RecipeUserRa;
import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import java.util.List;

public class RecipeManager {
    private RecipeUserRa recipeUserRa;
    private RecipeRa recipeRa;
    private MessagingEngine messagingEngine;
    private IngredientRa ingredientRa;
    private IngredientEngine ingredientEngine;
    private GroceryManager groceryManager;

    @Inject
    public RecipeManager(RecipeUserRa recipeUserRa,
                         RecipeRa recipeRa,
                         MessagingEngine messagingEngine,
                         IngredientRa ingredientRa,
                         IngredientEngine ingredientEngine,
                         GroceryManager groceryManager) {
        this.recipeUserRa = recipeUserRa;
        this.recipeRa = recipeRa;
        this.messagingEngine = messagingEngine;
        this.ingredientRa = ingredientRa;
        this.ingredientEngine = ingredientEngine;
        this.groceryManager = groceryManager;
    }

    public List<Recipe> listRecipesForUser(String email) {
        RecipeUser user = recipeUserRa.getUserByEmail(email);
        return recipeRa.listRecipesForUser(user);
    }

    public Recipe commitRecipe(Recipe recipe, String email) {
        RecipeUser user = recipeUserRa.getUserByEmail(email);
        List<Grocery> groceries = ingredientEngine.getGroceries(recipe.getIngredients());
        groceryManager.commitGroceries(groceries, user.getEmail());
        ingredientRa.commitIngredients(recipe.getIngredients(), user);
        if(recipe.getId() != null){
            Recipe existing = recipeRa.getRecipe(user, recipe.getId());
            List<Ingredient> toDelete = ingredientRa.getIngredientsToDelete(recipe, existing);
            ingredientRa.deleteIngredients(toDelete);
        }
        Recipe save = recipeRa.commitRecipe(recipe, user);
        if (save != null)
            messagingEngine.sendMessage(save, user);
        return save;
    }
}
