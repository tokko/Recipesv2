package com.tokko.recipesv2.groceries;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.tokko.recipesv2.ApiFactory;
import com.tokko.recipesv2.backend.entities.groceryApi.GroceryApi;
import com.tokko.recipesv2.backend.entities.groceryApi.model.Grocery;
import com.tokko.recipesv2.masterdetail.AbstractLoader;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;

public class GroceryGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<AbstractLoader<Grocery>>() {
        }).to(GroceryLoader.class);
        bind(GroceryApi.class).toInstance((GroceryApi) ApiFactory.createApi(GroceryApi.Builder.class));
        bind(new TypeLiteral<StringifyableAdapter<Grocery>>() {
        }).to(GroceryAdapter.class);
    }
}
