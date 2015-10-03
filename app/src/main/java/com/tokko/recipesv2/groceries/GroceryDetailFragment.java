package com.tokko.recipesv2.groceries;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.inject.Inject;
import com.tokko.recipesv2.R;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Grocery;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.views.EditTextViewSwitchable;

import java.io.IOException;

import roboguice.inject.InjectView;

public class GroceryDetailFragment extends ItemDetailFragment<Grocery> {
    public static final String ACTION_GROCERY_COMMITED = "grocery_committed";
    public static final String EXTRA_GROCERY = "extra_grocery";
    public static final String EXTRA_PERSIST_GROCERY = "extra_persist";

    @InjectView(R.id.grocery_title)
    private EditTextViewSwitchable titleTextView;
    @InjectView(R.id.buttonbar_cancel)
    private Button cancelButton;
    @Inject
    private RecipeApi api;

    private boolean persist;

    @Override
    protected int getLayoutResource() {
        return R.layout.grocerydetailfragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleTextView.setHint("Title");
        persist = getArguments().getBoolean(EXTRA_PERSIST_GROCERY, true);
        if (getDialog() != null)
            getDialog().setTitle("Grocery");
        cancelButton.setEnabled(false);
    }

    @Override
    public ItemDetailFragment<Grocery> newInstance(Bundle args) {
        GroceryDetailFragment f = new GroceryDetailFragment();
        f.setArguments(args);
        return f;
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
    public boolean onCancel() {
        dismiss();
        return true;
    }

    @Override
    protected EntityGetter<Grocery> getEntityGetter() {
        return (id) -> api.getGrocery(id).execute();
    }

    @Override
    protected boolean onOk() {
        AsyncTask.execute(() -> {
            try {
                if (persist) {
                    Grocery execute = api.commitGrocery(entity).execute();
                    entity.setId(execute.getId());
                }
                getActivity().sendBroadcast(new Intent(ACTION_GROCERY_COMMITED).putExtra(EXTRA_GROCERY, new AndroidJsonFactory().toPrettyString(entity)));
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
                api.deleteGrocery(entity.getId()).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return true;
    }

    private class GroceryDownloader extends AsyncTask<Long, Void, Grocery> {

        @Override
        protected Grocery doInBackground(Long... params) {
            try {
                return api.getGrocery(params[0]).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Grocery grocery) {
            if (grocery != null) {
                entity = grocery;
                bindView(entity);
            }
        }
    }
}
