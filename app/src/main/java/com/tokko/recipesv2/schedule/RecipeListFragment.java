package com.tokko.recipesv2.schedule;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Recipe;
import com.tokko.recipesv2.recipes.RecipeAdapter;
import com.tokko.recipesv2.recipes.RecipeLoader;

import java.util.List;

import roboguice.fragment.provided.RoboListFragment;

public class RecipeListFragment extends RoboListFragment implements LoaderManager.LoaderCallbacks<List<Recipe>>{

    @Inject
    private RecipeLoader recipeLoader;
    @Inject
    private RecipeAdapter adapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListAdapter(adapter);
        adapter.setIsDraggable(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getLoaderManager().destroyLoader(0);
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        return recipeLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        adapter.replaceData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {
        adapter.replaceData(null);
    }
}
