package com.tokko.recipesv2.recipes;

import android.content.Context;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Grocery;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Ingredient;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Recipe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MockRecipeLoader extends RecipeLoader {

    @Inject
    public MockRecipeLoader(Context context, RecipeApi api) {
        super(context, api);
    }

    @Override
    public List<Recipe> loadInBackground() {
        Recipe r = new Recipe();
        r.setId(1L);
        r.setTitle("Recipe1");
        Grocery g = new Grocery();
        g.setTitle("foo");
        Ingredient i = new Ingredient();
        i.setGrocery(g);
        r.setIngredients(Collections.singletonList(i));
        Recipe r1 = new Recipe();
        r1.setId(2L);
        r1.setTitle("Recipe2");
        Recipe r2 = new Recipe();
        r2.setId(3L);
        r2.setTitle("Recipe3");
        return Arrays.asList(r, r1, r2);
    }
}
