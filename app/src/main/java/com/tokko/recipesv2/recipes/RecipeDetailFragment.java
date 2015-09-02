package com.tokko.recipesv2.recipes;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.google.inject.Inject;
import com.tokko.recipesv2.R;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Ingredient;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Recipe;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.views.EditTextViewSwitchable;
import com.tokko.recipesv2.views.EditableListView;

import java.io.IOException;

import roboguice.inject.InjectView;

public class RecipeDetailFragment extends ItemDetailFragment<Recipe> {

    @InjectView(R.id.recipe_title)
    private EditTextViewSwitchable title;

    @InjectView(R.id.ingredient_list)
    private EditableListView<Ingredient> list;

    @InjectView(R.id.instructionList)
    private EditableListView<String> instructions;

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
    public ItemDetailFragment<Recipe> newInstance(Bundle args) {
        RecipeDetailFragment f = new RecipeDetailFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    protected void bindView(Recipe entity) {
        title.setData(entity.getTitle());
        list.setData(entity.getIngredients());
        instructions.setData(entity.getInstructions());
    }

    @Override
    protected Recipe getEntity() {
        entity.setTitle(title.getData());
        entity.setIngredients(list.getData());
        entity.setInstructions(instructions.getData());
        return entity;
    }

    @Override
    protected boolean onOk() {
        AsyncTask.execute(() -> {
            try {
                api.commitRecipe(entity).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return true;
    }

    @Override
    protected boolean onDelete() {
        AsyncTask.execute(() -> {
            try {
                api.deleteRecipe(entity.getId()).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return true;
    }
}
