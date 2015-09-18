package com.tokko.recipesv2.shoppinglist;

import android.content.Context;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Grocery;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Ingredient;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ShoppingListItem;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;

public class ShoppingListAdapter extends StringifyableAdapter<ShoppingListItem> {
    @Inject
    public ShoppingListAdapter(Context context) {
        super(context);
    }

    @Override
    protected String getItemString(int position) {
        ShoppingListItem item = getItem(position);
        Ingredient ingredient = item.getIngredient();
        Grocery grocery = ingredient.getGrocery();
        String title = grocery.getTitle();
        return title;
    }

    @Override
    public long getItemId(int position) {
        ShoppingListItem item = getItem(position);
        Ingredient ingredient = item.getIngredient();
        Grocery grocery = ingredient.getGrocery();
        Long id = grocery.getId();
        return id != null ? id : -1;
    }
}
