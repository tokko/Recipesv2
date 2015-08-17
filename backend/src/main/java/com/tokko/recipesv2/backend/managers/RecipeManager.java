package com.tokko.recipesv2.backend.managers;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.engines.IngredientCrudEngine;
import com.tokko.recipesv2.backend.engines.IngredientEngine;
import com.tokko.recipesv2.backend.engines.MessagingEngine;
import com.tokko.recipesv2.backend.engines.RecipeCrudEngine;
import com.tokko.recipesv2.backend.engines.RecipeUserCrudEngine;
import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import java.util.List;

public class RecipeManager {
    private RecipeUserCrudEngine recipeUserCrudEngine;
    private RecipeCrudEngine recipeCrudEngine;
    private MessagingEngine messagingEngine;
    private IngredientCrudEngine ingredientCrudEngine;
    private IngredientEngine ingredientEngine;
    private GroceryManager groceryManager;

    @Inject
    public RecipeManager(RecipeUserCrudEngine recipeUserCrudEngine,
                         RecipeCrudEngine recipeCrudEngine,
                         MessagingEngine messagingEngine,
                         IngredientCrudEngine ingredientCrudEngine,
                         IngredientEngine ingredientEngine,
                         GroceryManager groceryManager) {
        this.recipeUserCrudEngine = recipeUserCrudEngine;
        this.recipeCrudEngine = recipeCrudEngine;
        this.messagingEngine = messagingEngine;
        this.ingredientCrudEngine = ingredientCrudEngine;
        this.ingredientEngine = ingredientEngine;
        this.groceryManager = groceryManager;
    }

    public List<Recipe> listRecipesForUser(String email) {
        RecipeUser user = recipeUserCrudEngine.getUserByEmail(email);
        return recipeCrudEngine.listRecipesForUser(user);
    }

    public Recipe commitRecipe(Recipe recipe, String email) {
        RecipeUser user = recipeUserCrudEngine.getUserByEmail(email);
        List<Grocery> groceries = ingredientEngine.getGroceries(recipe.getIngredients());
        groceryManager.commitGroceries(groceries, user.getEmail());
        ingredientCrudEngine.commitIngredients(recipe.ingredients, user);
/*
        if(recipe.getId() != null){
            Recipe existing = recipeCrudEngine.getRecipe(user, recipe.getId());
            List<Ingredient> toDelete = ingredientCrudEngine.getIngredientsToDelete(recipe, existing);
        }
        */
        Recipe save = recipeCrudEngine.commitRecipe(recipe, user);
        if (save != null)
            messagingEngine.sendMessage(save, user);
        return save;
    }
}
