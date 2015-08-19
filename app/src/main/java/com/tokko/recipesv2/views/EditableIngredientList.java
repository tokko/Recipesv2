package com.tokko.recipesv2.views;

import android.content.Context;
import android.util.AttributeSet;

import com.tokko.recipesv2.backend.entities.recipeApi.model.Ingredient;
import com.tokko.recipesv2.recipes.IngredientDetailFragment;

public class EditableIngredientList extends EditableListView<Ingredient> implements IngredientDetailFragment.IngredientDetailFragmentCallbacks {
    public EditableIngredientList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void ingredientAdded(Ingredient ingredient) {
        adapter.addItem(ingredient);
    }
}
