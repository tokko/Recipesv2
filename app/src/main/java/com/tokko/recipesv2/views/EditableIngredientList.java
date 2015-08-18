package com.tokko.recipesv2.views;

import android.content.Context;
import android.util.AttributeSet;

import com.tokko.recipesv2.backend.entities.recipeApi.model.Ingredient;

public class EditableIngredientList extends EditableListView<Ingredient> {
    public EditableIngredientList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
