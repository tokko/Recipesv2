package com.tokko.recipesv2.groceries;

import android.content.Context;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Grocery;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;

public class GroceryAdapter extends StringifyableAdapter<Grocery> {
    @Inject
    public GroceryAdapter(Context context) {
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
