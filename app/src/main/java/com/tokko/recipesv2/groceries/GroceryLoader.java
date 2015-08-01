package com.tokko.recipesv2.groceries;

import android.content.Context;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.entities.groceryApi.GroceryApi;
import com.tokko.recipesv2.backend.entities.groceryApi.model.CollectionResponseGrocery;
import com.tokko.recipesv2.backend.entities.groceryApi.model.Grocery;
import com.tokko.recipesv2.masterdetail.AbstractLoader;

import java.io.IOException;
import java.util.List;

public class GroceryLoader extends AbstractLoader<Grocery> {

    private final GroceryApi api;

    @Inject
    public GroceryLoader(Context context, GroceryApi api) {
        super(context, Grocery.class);
        this.api = api;
    }

    @Override
    public List<Grocery> loadInBackground() {
        try {
            CollectionResponseGrocery execute = api.list().execute();
            return execute.getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
