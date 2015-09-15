package com.tokko.recipesv2.shoppinglist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.inject.Inject;
import com.tokko.recipesv2.R;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ShoppingList;
import com.tokko.recipesv2.recipes.IngredientDetailFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import roboguice.RoboGuice;
import roboguice.fragment.provided.RoboListFragment;

public class ShoppingListFragment extends RoboListFragment implements ShoppingListDownloader.ShoppingListDownloadedCallbacks {

    @Inject
    private ShoppingListAdapter adapter;
    private ShoppingList list;

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
    public void onStart() {
        super.onStart();
        ShoppingListDownloader listDownloader = RoboGuice.getInjector(getActivity()).getInstance(ShoppingListDownloader.class);
        listDownloader.setCallbacks(this);
        listDownloader.execute();
    }

    @OnClick(R.id.shoppingListAddbutton)
    public void onAdd(){
        IngredientDetailFragment ingredientDetailFragment = RoboGuice.getInjector(getActivity()).getInstance(IngredientDetailFragment.class);
        ingredientDetailFragment.show(getFragmentManager(), "tag");
    }

    @Override
    public void onShoppingListDownloaded(ShoppingList list) {
        this.list = list;
        adapter.replaceData(list.getItems());
    }
}
