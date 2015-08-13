package com.tokko.recipesv2.recipes;

import android.content.Context;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.CollectionResponseRecipe;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Recipe;
import com.tokko.recipesv2.masterdetail.AbstractLoader;

import java.io.IOException;
import java.util.List;

public class RecipeLoader extends AbstractLoader<Recipe> {
    private final RecipeApi api;

    @Inject
    public RecipeLoader(Context context, RecipeApi api) {
        super(context, Recipe.class);
        this.api = api;
    }

    @Override
    public List<Recipe> loadInBackground() {
        try {
            RecipeApi.List list = api.list();
            CollectionResponseRecipe execute = list.execute();
            List<Recipe> items = execute.getItems();
            return items;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
