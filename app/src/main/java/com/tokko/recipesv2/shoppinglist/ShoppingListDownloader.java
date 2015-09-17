package com.tokko.recipesv2.shoppinglist;

import android.os.AsyncTask;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ShoppingList;

import java.io.IOException;

public class ShoppingListDownloader extends AsyncTask<Void, Void, ShoppingList> {
    private RecipeApi api;


    private ShoppingListDownloadedCallbacks callbacks;

    @Inject
    public ShoppingListDownloader(RecipeApi api) {
        this.api = api;
    }

    @Override
    protected ShoppingList doInBackground(Void... params) {
        try {
            RecipeApi.GetShoppingList shoppingList = api.getShoppingList();
            return shoppingList.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ShoppingList shoppingList) {
        if (shoppingList != null)
            callbacks.onShoppingListDownloaded(shoppingList);
    }

    public void setCallbacks(ShoppingListDownloadedCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    public interface ShoppingListDownloadedCallbacks {
        void onShoppingListDownloaded(ShoppingList list);
    }
}
