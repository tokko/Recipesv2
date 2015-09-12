package com.tokko.recipesv2.shoppinglist;

import android.os.Bundle;

import com.tokko.recipesv2.R;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ShoppingList;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;

import roboguice.RoboGuice;

public class ShoppingListFragment extends ItemDetailFragment<ShoppingList> implements ShoppingListDownloader.ShoppingListDownloadedCallbacks {
    @Override
    protected int getLayoutResource() {
        return R.layout.shoppinglistfragment;
    }

    @Override
    public ItemDetailFragment<ShoppingList> newInstance(Bundle args) {
        ShoppingListFragment f = new ShoppingListFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onStart() {
        super.onStart();
        ShoppingListDownloader downloader = RoboGuice.getInjector(getActivity()).getInstance(ShoppingListDownloader.class);
        downloader.setCallbacks(this);
        downloader.execute();

    }

    @Override
    protected void bindView(ShoppingList entity) {

    }

    @Override
    protected ShoppingList getEntity() {
        return null;
    }

    @Override
    protected boolean onOk() {
        return false;
    }

    @Override
    protected boolean onDelete() {
        return false;
    }

    @Override
    public void onShoppingListDownloaded(ShoppingList list) {
        entity = list;
        bindView(list);
    }
}
