package com.tokko.recipesv2.recipes;

import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.tokko.recipesv2.R;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Grocery;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Ingredient;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Quantity;
import com.tokko.recipesv2.groceries.GroceryDetailFragment;
import com.tokko.recipesv2.masterdetail.AbstractLoader;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;

import java.io.IOException;
import java.util.List;

import butterknife.OnClick;
import roboguice.inject.InjectView;

public class IngredientDetailFragment extends ItemDetailFragment<Ingredient> implements LoaderManager.LoaderCallbacks<List<Grocery>> {

    @Inject
    private AbstractLoader<Grocery> loader;
    @Inject
    private RecipeApi api;
    @Inject
    private StringifyableAdapter<Grocery> adapter;
    @Inject
    private GroceryDetailFragment groceryDetailFragment;

    @InjectView(R.id.ingredientdetail_grocery)
    private AutoCompleteTextView grocery;
    @InjectView(R.id.ingredient_quantity)
    private EditText quantityEditText;
    @InjectView(R.id.ingredient_unit)
    private Spinner unitSpinner;

    private IngredientDetailFragmentCallbacks ingredientCallbacks;
    private Grocery selectedGrocery;
    private BroadcastReceiver br;

    private ArrayAdapter<String> unitAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unitAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        grocery.setAdapter(adapter);
        grocery.setOnItemClickListener((av, v, pos, id) -> selectedGrocery = adapter.getItem(pos));
        grocery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                selectedGrocery = null;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        if (getDialog() != null)
            getDialog().setTitle("Ingredient");
        unitSpinner.setAdapter(unitAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getLoaderManager().initLoader(0, null, this);
        new UnitDownloader().execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().getLoaderManager().destroyLoader(0);
        if (br != null)
            getActivity().unregisterReceiver(br);
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
        if (selectedGrocery != null) {
            ingredient.setGrocery(selectedGrocery);
            Quantity q = new Quantity();
            q.setQuantity(Double.valueOf(quantityEditText.getText().toString()));
            q.setUnit((String) unitSpinner.getSelectedItem());
            ingredient.setQuantity(q);
            ingredientCallbacks.ingredientAdded(ingredient);
            dismiss();
        } else {
            br = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                        ingredient.setGrocery(new AndroidJsonFactory().fromString(intent.getStringExtra(GroceryDetailFragment.EXTRA_GROCERY), Grocery.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ingredientCallbacks.ingredientAdded(ingredient);
                    groceryDetailFragment.dismiss();
                    IngredientDetailFragment.this.dismiss();
                }
            };
            getActivity().registerReceiver(br, new IntentFilter(GroceryDetailFragment.ACTION_GROCERY_COMMITED));
            Grocery g = new Grocery();
            g.setTitle(grocery.getText().toString());
            Bundle b = new Bundle();
            b.putSerializable(ItemDetailFragment.EXTRA_CLASS, Grocery.class);
            b.putString(IngredientDetailFragment.EXTRA_ENTITY, new Gson().toJson(g));
            groceryDetailFragment.setArguments(b);
            groceryDetailFragment.show(getActivity().getFragmentManager(), "tag2");
        }
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

    private class UnitDownloader extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return api.listUnits().execute().getItems();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> units) {
            if (units != null) {
                unitAdapter.clear();
                unitAdapter.addAll(units);
                unitAdapter.notifyDataSetChanged();
            }
            super.onPostExecute(units);
        }
    }
}
