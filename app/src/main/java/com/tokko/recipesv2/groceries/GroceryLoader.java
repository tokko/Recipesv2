package com.tokko.recipesv2.groceries;

import android.content.Context;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.CollectionResponseGrocery;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Grocery;
import com.tokko.recipesv2.masterdetail.AbstractLoader;

import java.io.IOException;
import java.util.List;

public class GroceryLoader extends AbstractLoader<Grocery> {

    private final RecipeApi api;

    @Inject
    public GroceryLoader(Context context, RecipeApi api) {
        super(context, Grocery.class);
        this.api = api;
    }

    @Override
    public List<Grocery> loadInBackground() {
        try {
            RecipeApi.ListGroceries list = api.listGroceries();
            CollectionResponseGrocery execute = list.execute();
            List<Grocery> items = execute.getItems();
            return items;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
