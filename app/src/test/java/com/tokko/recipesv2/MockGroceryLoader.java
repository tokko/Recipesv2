package com.tokko.recipesv2;

import android.content.Context;

import com.tokko.recipesv2.backend.entities.groceryApi.GroceryApi;
import com.tokko.recipesv2.backend.entities.groceryApi.model.Grocery;
import com.tokko.recipesv2.groceries.GroceryLoader;

import java.util.List;

/**
 * Created by Andreas on 1/08/2015.
 */
public class MockGroceryLoader extends GroceryLoader {

    private final List<Grocery> groceries;

    public MockGroceryLoader(Context context, GroceryApi api, List<Grocery> groceries) {
        super(context, api);
        this.groceries = groceries;
    }

    @Override
    public List<Grocery> loadInBackground() {
        return groceries;
    }
}
