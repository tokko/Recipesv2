package com.tokko.recipesv2.recipes;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.tokko.recipesv2.ApiFactory;
import com.tokko.recipesv2.BuildConfig;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Ingredient;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Recipe;
import com.tokko.recipesv2.masterdetail.AbstractLoader;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;

public class RecipeGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<StringifyableAdapter<Recipe>>() {
        }).to(RecipeAdapter.class);
        bind(new TypeLiteral<StringifyableAdapter<Ingredient>>() {
        }).to(IngredientAdapter.class);
        bind(new TypeLiteral<ItemDetailFragment<Recipe>>() {
        }).to(RecipeDetailFragment.class);
        bind(new TypeLiteral<ItemDetailFragment<Ingredient>>() {
        }).to(IngredientDetailFragment.class);
        bind(RecipeApi.class).toInstance((RecipeApi) ApiFactory.createApi(RecipeApi.Builder.class));

        if (BuildConfig.BUILD_TYPE.equals("mock")) {
            bind(new TypeLiteral<AbstractLoader<Recipe>>() {
            }).to(MockRecipeLoader.class);
        } else
            bind(new TypeLiteral<AbstractLoader<Recipe>>() {
            }).to(RecipeLoader.class);
    }
}
