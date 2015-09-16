package com.tokko.recipesv2.shoppinglist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.inject.Inject;
import com.tokko.recipesv2.R;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Ingredient;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ShoppingList;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ShoppingListItem;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.recipes.IngredientDetailFragment;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import roboguice.RoboGuice;
import roboguice.fragment.provided.RoboListFragment;

public class ShoppingListFragment extends RoboListFragment implements ShoppingListDownloader.ShoppingListDownloadedCallbacks, IngredientDetailFragment.IngredientDetailFragmentCallbacks {

    @Inject
    private ShoppingListAdapter adapter;
    private ShoppingList list;
    private ShoppingListItem editing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shoppinglistfragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, getActivity());
        adapter.setDeleteEnabled(true);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        IngredientDetailFragment ingredientDetailFragment = RoboGuice.getInjector(getActivity()).getInstance(IngredientDetailFragment.class);
        try {
            ShoppingListItem item = adapter.getItem(position);
            Bundle b = new Bundle();
            b.putSerializable(ItemDetailFragment.EXTRA_CLASS, item.getIngredient().getClass());
            editing = item;
            b.putString(ItemDetailFragment.EXTRA_ENTITY, new AndroidJsonFactory().toPrettyString(item.getIngredient()));
            ingredientDetailFragment.setArguments(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ingredientDetailFragment.show(getFragmentManager(), "tag");
    }

    @Override
    public void onStart() {
        super.onStart();
        ShoppingListDownloader listDownloader = RoboGuice.getInjector(getActivity()).getInstance(ShoppingListDownloader.class);
        listDownloader.setCallbacks(this);
        listDownloader.execute();
    }

    @OnClick(R.id.shoppingListAddbutton)
    public void onAdd(){
        IngredientDetailFragment ingredientDetailFragment = RoboGuice.getInjector(getActivity()).getInstance(IngredientDetailFragment.class);
        Bundle b = new Bundle();
        b.putSerializable(ItemDetailFragment.EXTRA_CLASS, Ingredient.class);
        ingredientDetailFragment.setArguments(b);
        ingredientDetailFragment.setIngredientDetailFragmentCallbacks(this);
        ingredientDetailFragment.setDeletable(false);
        ingredientDetailFragment.show(getFragmentManager(), "tag");
    }

    @Override
    public void onShoppingListDownloaded(ShoppingList list) {
        this.list = list;
        adapter.replaceData(list.getItems());
    }

    @Override
    public void ingredientAdded(Ingredient ingredient) {
        if (editing != null) {
            editing.setIngredient(ingredient);
            editing = null;
            return;
        }
        ShoppingListItem sli = new ShoppingListItem();
        sli.setIngredient(ingredient);
        adapter.addItem(sli);
    }

    @Override
    public void ingredientDeleted(Ingredient entity) {

    }
}
