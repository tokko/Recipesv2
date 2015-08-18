package com.tokko.recipesv2.recipes;

import com.tokko.recipesv2.backend.entities.recipeApi.model.Ingredient;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;

public class IngredientDetailFragment extends ItemDetailFragment<Ingredient> {

    @Override
    protected int getLayoutResource() {
        return 0;
    }

    @Override
    protected void bindView(Ingredient entity) {

    }

    @Override
    protected Ingredient getEntity() {
        return null;
    }

    @Override
    protected void onOk() {

    }

    @Override
    protected void onDelete() {

    }
}
