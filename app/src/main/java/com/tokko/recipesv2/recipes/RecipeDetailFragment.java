package com.tokko.recipesv2.recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.inject.Inject;
import com.tokko.recipesv2.R;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Recipe;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.views.EditTextViewSwitchable;

public class RecipeDetailFragment extends ItemDetailFragment<Recipe> {

    @Inject
    private EditTextViewSwitchable title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recipedetailfragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title.setHint("Title");
    }

    @Override
    protected void bindView(Recipe entity) {
        title.setData(entity.getTitle());
    }

    @Override
    protected Recipe getEntity() {
        entity.setTitle(title.getData());
        return entity;
    }

    @Override
    protected void onOk() {

    }

    @Override
    protected void onDelete() {

    }
}
