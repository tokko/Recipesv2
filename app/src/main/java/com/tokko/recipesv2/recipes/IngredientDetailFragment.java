package com.tokko.recipesv2.recipes;

import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
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
import roboguice.RoboGuice;
import roboguice.inject.InjectView;

public class IngredientDetailFragment extends ItemDetailFragment<Ingredient> implements LoaderManager.LoaderCallbacks<List<Grocery>>,UnitDownloader.UnitDownloaderCallback {

    @Inject
    private AbstractLoader<Grocery> loader;
    @Inject
    private RecipeApi api;
    @Inject
    private StringifyableAdapter<Grocery> adapter;
    @Inject
    private GroceryDetailFragment groceryDetailFragment;

    @InjectView(R.id.ingredientdetail_grocery)
    private AutoCompleteTextView groceryAutoCompleteTextView;
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
        groceryAutoCompleteTextView.setAdapter(adapter);
        groceryAutoCompleteTextView.setOnItemClickListener((av, v, pos, id) -> selectedGrocery = adapter.getItem(pos));
        groceryAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                selectedGrocery = null;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resolveChosenGrocery();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        if (getDialog() != null)
            getDialog().setTitle("Ingredient");
        unitSpinner.setAdapter(unitAdapter);
        enterEditMode();
        deleteButton.setVisibility(View.GONE);
    }

    @Override
    public ItemDetailFragment<Ingredient> newInstance(Bundle args) {
        IngredientDetailFragment f = new IngredientDetailFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void clear() {
        selectedGrocery = null;
        super.clear();
    }

    private void resolveChosenGrocery() {
        CharSequence s = groceryAutoCompleteTextView.getText();
        if (s != null && s.length() > 0) {
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).getTitle().equals(s.toString())) {
                    groceryAutoCompleteTextView.setSelection(i);
                    selectedGrocery = adapter.getItem(i);
                    break;
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getLoaderManager().initLoader(0, null, this);
        RoboGuice.getInjector(getActivity()).getInstance(UnitDownloader.class).setCallbacks(this).execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        showButtonBar();
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().getLoaderManager().destroyLoader(0);
        if (br != null) {
            getActivity().unregisterReceiver(br);
            br = null;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.ingredientdetailfragment;
    }

    @Override
    protected void bindView(Ingredient entity) {
        if (entity == null) return;
        if (entity.getGrocery() != null)
            groceryAutoCompleteTextView.setText(entity.getGrocery().getTitle());
        else
            groceryAutoCompleteTextView.setText("");
        if (entity.getQuantity() != null)
            quantityEditText.setText(entity.getQuantity().getQuantity().toString());
        else
            quantityEditText.setText("");
        resolveChosenGrocery();
        groceryAutoCompleteTextView.dismissDropDown();
    }

    @Override
    protected Ingredient getEntity() {
        Quantity q = new Quantity();
        q.setQuantity(Double.valueOf(quantityEditText.getText().toString()));
        q.setUnit((String) unitSpinner.getSelectedItem());
        entity.setQuantity(q);
        return entity;
    }

    public void setIngredientDetailFragmentCallbacks(IngredientDetailFragmentCallbacks callbacks) {
        ingredientCallbacks = callbacks;
    }

    @Override
    protected boolean onOk() {
        Ingredient ingredient = getEntity();
        if (selectedGrocery != null) {
            ingredient.setGrocery(selectedGrocery);
            Quantity q = new Quantity();
            q.setQuantity(Double.valueOf(quantityEditText.getText().toString()));
            q.setUnit((String) unitSpinner.getSelectedItem());
            ingredient.setQuantity(q);
            ingredientCallbacks.ingredientAdded(ingredient);
            dismiss();
            return true;
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
            g.setTitle(groceryAutoCompleteTextView.getText().toString());
            Bundle b = new Bundle();
            b.putSerializable(ItemDetailFragment.EXTRA_CLASS, Grocery.class);
            b.putBoolean(GroceryDetailFragment.EXTRA_PERSIST_GROCERY, false);
            b.putString(IngredientDetailFragment.EXTRA_ENTITY, new Gson().toJson(g));
            groceryDetailFragment.setArguments(b);
            groceryDetailFragment.show(getActivity().getFragmentManager(), "tag2");
            return false;
        }
    }

    @Override
    @OnClick(R.id.buttonbar_cancel)
    public void onCancelButtonClick(View v) {
        super.onCancelButtonClick(v);
        dismiss();
    }

    @Override
    protected boolean onDelete() {
        ingredientCallbacks.ingredientDeleted(entity);
        dismiss();
        return true;
    }

    @Override
    public Loader<List<Grocery>> onCreateLoader(int id, Bundle args) {
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Grocery>> loader, List<Grocery> data) {
        adapter.replaceData(data);
        resolveChosenGrocery();
    }

    @Override
    public void onLoaderReset(Loader<List<Grocery>> loader) {
        adapter.replaceData(null);
    }

    @Override
    public void onUnitDownloaded(List<String> units) {
        unitAdapter.clear();
        unitAdapter.addAll(units);
        unitAdapter.notifyDataSetChanged();
        if (entity != null && entity.getQuantity() != null) {
            for (int i = 0; i < unitAdapter.getCount(); i++) {
                if (unitAdapter.getItem(i).equals(entity.getQuantity().getUnit())) {
                    unitSpinner.setSelection(i);
                    break;
                }
            }
        }
    }

    public interface IngredientDetailFragmentCallbacks {
        void ingredientAdded(Ingredient ingredient);

        void ingredientDeleted(Ingredient entity);
    }


}
