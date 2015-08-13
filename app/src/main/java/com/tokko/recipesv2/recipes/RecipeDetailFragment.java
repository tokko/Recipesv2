package com.tokko.recipesv2.recipes;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.google.inject.Inject;
import com.tokko.recipesv2.R;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Recipe;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.views.EditTextViewSwitchable;

import java.io.IOException;

import roboguice.inject.InjectView;

public class RecipeDetailFragment extends ItemDetailFragment<Recipe> {

    @InjectView(R.id.recipe_title)
    private EditTextViewSwitchable title;

    @Inject
    private RecipeApi api;
    @Override
    protected int getLayoutResource() {
        return R.layout.recipedetailfragment;
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
        AsyncTask.execute(() -> {
            try {
                api.insert(entity).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onDelete() {
        AsyncTask.execute(() -> {
            try {
                api.remove(entity.getId()).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
