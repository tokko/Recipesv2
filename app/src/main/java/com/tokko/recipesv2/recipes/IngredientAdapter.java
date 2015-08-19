package com.tokko.recipesv2.recipes;

import android.content.Context;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Grocery;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Ingredient;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;

public class IngredientAdapter extends StringifyableAdapter<Ingredient> {

    @Inject
    public IngredientAdapter(Context context) {
        super(context);
    }

    @Override
    protected String getItemString(int position) {
        Grocery grocery = getItem(position).getGrocery();
        return grocery.getTitle();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
