package com.tokko.recipesv2.recipes;

import android.view.View;

import com.tokko.recipesv2.R;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Ingredient;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;

import butterknife.OnClick;

public class IngredientDetailFragment extends ItemDetailFragment<Ingredient> {

    @Override
    protected int getLayoutResource() {
        return R.layout.recipedetailfragment;
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
    @OnClick(R.id.buttonbar_cancel)
    public void onCancelButtonClick(View v) {
        super.onCancelButtonClick(v);
        dismiss();
    }

    @Override
    protected void onDelete() {

    }
}
