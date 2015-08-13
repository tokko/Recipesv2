package com.tokko.recipesv2.recipes;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Recipe;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;

public class RecipeGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<StringifyableAdapter<Recipe>>() {
        }).to(RecipeAdapter.class);
    }
}
