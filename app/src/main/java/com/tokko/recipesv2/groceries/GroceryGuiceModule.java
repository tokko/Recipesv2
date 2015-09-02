package com.tokko.recipesv2.groceries;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.tokko.recipesv2.BuildConfig;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Grocery;
import com.tokko.recipesv2.masterdetail.AbstractLoader;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;

public class GroceryGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        if (BuildConfig.BUILD_TYPE.equals("mock"))
            bind(new TypeLiteral<AbstractLoader<Grocery>>() {
            }).to(MockGroceryLoader.class);
        else
            bind(new TypeLiteral<AbstractLoader<Grocery>>() {
            }).to(GroceryLoader.class);
        bind(new TypeLiteral<StringifyableAdapter<Grocery>>() {
        }).to(GroceryAdapter.class);
        bind(new TypeLiteral<ItemDetailFragment<Grocery>>() {
        }).to(GroceryDetailFragment.class);
    }
}
