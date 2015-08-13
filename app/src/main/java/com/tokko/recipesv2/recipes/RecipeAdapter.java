package com.tokko.recipesv2.recipes;

import android.content.Context;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Recipe;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;

public class RecipeAdapter extends StringifyableAdapter<Recipe> {
    @Inject
    public RecipeAdapter(Context context) {
        super(context);
    }

    @Override
    protected String getItemString(int position) {
        return getItem(position).getTitle();
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }
}
