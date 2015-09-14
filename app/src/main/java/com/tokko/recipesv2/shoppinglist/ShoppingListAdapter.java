package com.tokko.recipesv2.shoppinglist;

import android.content.Context;

import com.tokko.recipesv2.backend.entities.recipeApi.model.ShoppingListItem;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;

public class ShoppingListAdapter extends StringifyableAdapter<ShoppingListItem> {
    public ShoppingListAdapter(Context context) {
        super(context);
    }

    @Override
    protected String getItemString(int position) {
        return getItem(position).getIngredient().getGrocery().getTitle();
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getIngredient().getGrocery().getId();
    }
}
