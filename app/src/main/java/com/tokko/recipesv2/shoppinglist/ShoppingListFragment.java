package com.tokko.recipesv2.shoppinglist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.inject.Inject;
import com.tokko.recipesv2.R;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ShoppingList;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;

import butterknife.OnClick;
import roboguice.RoboGuice;
import roboguice.fragment.RoboListFragment;

public class ShoppingListFragment extends RoboListFragment implements ShoppingListDownloader.ShoppingListDownloadedCallbacks {

    @Inject
    private ShoppingListAdapter adapter;
    private ShoppingList list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shoppinglistfragment, null);
    }

    @OnClick(R.id.shoppingListAddbutton)
    public void onAdd(){

    }

    @Override
    public void onShoppingListDownloaded(ShoppingList list) {
        this.list = list;
        adapter.replaceData(list.getItems());
    }
}
