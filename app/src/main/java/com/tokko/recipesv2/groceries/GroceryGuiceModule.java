package com.tokko.recipesv2.groceries;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.tokko.recipesv2.backend.entities.groceryApi.model.Grocery;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;

public class GroceryGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<StringifyableAdapter.Stringifier<Grocery>>() {
        }).toInstance(r -> r.getTitle());
        bind(new TypeLiteral<StringifyableAdapter.IdGetter<Grocery>>() {
        }).toInstance(r -> r.getId());
    }
}
