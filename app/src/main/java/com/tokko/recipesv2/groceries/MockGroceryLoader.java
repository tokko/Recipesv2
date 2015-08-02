package com.tokko.recipesv2.groceries;

import android.content.Context;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.entities.groceryApi.GroceryApi;
import com.tokko.recipesv2.backend.entities.groceryApi.model.Grocery;

import java.util.Arrays;
import java.util.List;

public class MockGroceryLoader extends GroceryLoader {

    private final List<Grocery> groceries;

    @Inject
    public MockGroceryLoader(Context context, GroceryApi api) {
        super(context, api);
        Grocery g1 = new Grocery();
        Grocery g2 = new Grocery();
        Grocery g3 = new Grocery();
        g1.setId(1L);
        g2.setId(2L);
        g3.setId(3L);
        g1.setTitle("Grocery1");
        g2.setTitle("Grocery2");
        g3.setTitle("Grocery3");
        groceries = Arrays.asList(g1, g2, g3);
    }

    public MockGroceryLoader(Context context, GroceryApi api, List<Grocery> groceries) {
        super(context, api);
        this.groceries = groceries;
    }

    @Override
    public List<Grocery> loadInBackground() {
        return groceries;
    }
}
