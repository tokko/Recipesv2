package com.tokko.recipesv2.views;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Ingredient;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.recipes.IngredientDetailFragment;

import java.io.IOException;

public class EditableIngredientList extends EditableListView<Ingredient> implements IngredientDetailFragment.IngredientDetailFragmentCallbacks {
    public EditableIngredientList(Context context, AttributeSet attrs) {
        super(context, attrs);
        Bundle b = new Bundle();
        b.putSerializable(ItemDetailFragment.EXTRA_CLASS, Ingredient.class);
        try {
            b.putString(ItemDetailFragment.EXTRA_ENTITY, new AndroidJsonFactory().toPrettyString(new Ingredient()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        detailFragment.setArguments(b);
    }

    @Override
    protected void prepare(ItemDetailFragment<Ingredient> detailFragment) {
        ((IngredientDetailFragment) detailFragment).setIngredientDetailFragmentCallbacks(this);
    }

    @Override
    public void ingredientAdded(Ingredient ingredient) {
        if (ingredient.getId() == null)
            adapter.addItem(ingredient);
        else {
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).getId().equals(ingredient.getId())) {
                    adapter.removeItem(i);
                    adapter.addItem(ingredient);
                }

            }
        }
    }

    @Override
    public void ingredientDeleted(Ingredient entity) {
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(entity))
                adapter.removeItem(i);
        }
    }
}
