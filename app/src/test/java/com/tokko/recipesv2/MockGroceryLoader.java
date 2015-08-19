package com.tokko.recipesv2;

import android.content.Context;

import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Grocery;
import com.tokko.recipesv2.groceries.GroceryLoader;

import java.util.List;

public class MockGroceryLoader extends GroceryLoader {

    private final List<Grocery> groceries;

    public MockGroceryLoader(Context context, RecipeApi api, List<Grocery> groceries) {
        super(context, api);
        this.groceries = groceries;
    }

    @Override
    public List<Grocery> loadInBackground() {
        return groceries;
    }
}
