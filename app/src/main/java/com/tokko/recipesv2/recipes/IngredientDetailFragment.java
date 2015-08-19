package com.tokko.recipesv2.recipes;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.google.inject.Inject;
import com.tokko.recipesv2.R;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Grocery;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Ingredient;
import com.tokko.recipesv2.masterdetail.AbstractLoader;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;

import java.util.List;

import butterknife.OnClick;
import roboguice.inject.InjectView;

public class IngredientDetailFragment extends ItemDetailFragment<Ingredient> implements LoaderManager.LoaderCallbacks<List<Grocery>> {

    @Inject
    AbstractLoader<Grocery> loader;

    @Inject
    StringifyableAdapter<Grocery> adapter;

    @InjectView(R.id.ingredientdetail_grocery)
    private AutoCompleteTextView grocery;
    private IngredientDetailFragmentCallbacks ingredientCallbacks;
    private Grocery selectedGrocery;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        grocery.setAdapter(adapter);
        grocery.setOnItemClickListener((av, v, pos, id) -> selectedGrocery = adapter.getItem(pos));
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().getLoaderManager().destroyLoader(0);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.ingredientdetailfragment;
    }

    @Override
    protected void bindView(Ingredient entity) {

    }

    @Override
    protected Ingredient getEntity() {
        return null;
    }

    public void setIngredientDetailFragmentCallbacks(IngredientDetailFragmentCallbacks callbacks) {
        ingredientCallbacks = callbacks;
    }

    @Override
    protected void onOk() {
        Ingredient ingredient = new Ingredient();
        ingredient.setGrocery(selectedGrocery);
        ingredientCallbacks.ingredientAdded(ingredient);
        dismiss();
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

    @Override
    public Loader<List<Grocery>> onCreateLoader(int id, Bundle args) {
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Grocery>> loader, List<Grocery> data) {
        adapter.replaceData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Grocery>> loader) {
        adapter.replaceData(null);
    }

    public interface IngredientDetailFragmentCallbacks {
        void ingredientAdded(Ingredient ingredient);

    }
}
