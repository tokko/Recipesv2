package com.tokko.recipesv2.groceries;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.google.inject.Inject;
import com.tokko.recipesv2.R;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Grocery;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.views.EditTextViewSwitchable;

import java.io.IOException;

import roboguice.inject.InjectView;

public class GroceryDetailFragment extends ItemDetailFragment<Grocery> {

    @InjectView(R.id.grocery_title)
    private EditTextViewSwitchable titleTextView;
    @Inject
    private RecipeApi api;

    @Override
    protected int getLayoutResource() {
        return R.layout.grocerydetailfragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleTextView.setHint("Title");
    }

    @Override
    protected void bindView(Grocery entity) {
        titleTextView.setData(entity.getTitle());
    }

    @Override
    protected Grocery getEntity() {
        entity.setTitle(titleTextView.getText());
        return entity;
    }

    @Override
    protected void onOk() {
        AsyncTask.execute(() -> {
            try {
                api.commitGrocery(entity).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onDelete() {
        AsyncTask.execute(() -> {
            try {
                api.deleteGrocery(entity.getId()).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
