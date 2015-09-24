package com.tokko.recipesv2.backend.managers;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.engines.IngredientEngine;
import com.tokko.recipesv2.backend.engines.MessagingEngine;
import com.tokko.recipesv2.backend.engines.QuantityCalculatorEngine;
import com.tokko.recipesv2.backend.engines.RecipeRescaleEngine;
import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.entities.RecipeUser;
import com.tokko.recipesv2.backend.resourceaccess.IngredientRa;
import com.tokko.recipesv2.backend.resourceaccess.RecipeRa;
import com.tokko.recipesv2.backend.resourceaccess.RecipeUserRa;

import java.util.List;

public class RecipeManager {
    private RecipeUserRa recipeUserRa;
    private RecipeRa recipeRa;
    private MessagingEngine messagingEngine;
    private IngredientRa ingredientRa;
    private IngredientEngine ingredientEngine;
    private GroceryManager groceryManager;
    private RecipeRescaleEngine recipeRescaleEngine;
    private QuantityCalculatorEngine quantityCalculatorEngine;

    @Inject
    public RecipeManager(RecipeUserRa recipeUserRa,
                         RecipeRa recipeRa,
                         MessagingEngine messagingEngine,
                         IngredientRa ingredientRa,
                         IngredientEngine ingredientEngine,
                         GroceryManager groceryManager,
                         RecipeRescaleEngine recipeRescaleEngine,
                         QuantityCalculatorEngine quantityCalculatorEngine) {
        this.recipeUserRa = recipeUserRa;
        this.recipeRa = recipeRa;
        this.messagingEngine = messagingEngine;
        this.ingredientRa = ingredientRa;
        this.ingredientEngine = ingredientEngine;
        this.groceryManager = groceryManager;
        this.recipeRescaleEngine = recipeRescaleEngine;
        this.quantityCalculatorEngine = quantityCalculatorEngine;
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

    public Recipe rescaleRecipe(Recipe recipe, String email) {
        RecipeUser user = recipeUserRa.getUserByEmail(email);
        Recipe old = recipeRa.getRecipe(user, recipe.getId());
        int fromHelpings = old.getHelpings();
        int toHelpings = recipe.getHelpings();
        List<Ingredient> baseLinedIngredients = quantityCalculatorEngine.toBaseQuantities(recipe.getIngredients());
        List<Ingredient> rescaledIngredients = recipeRescaleEngine.rescaleIngredientsToNumberOfHelpings(baseLinedIngredients, fromHelpings, toHelpings);
        List<Ingredient> uppedIngredients = quantityCalculatorEngine.upQuantities(rescaledIngredients);
        recipe.setIngredients(uppedIngredients);
        return recipe;
    }
}
