package com.tokko.recipesv2.shoppinglist;

import android.os.AsyncTask;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ShoppingList;

import org.joda.time.DateTime;

import java.io.IOException;

public class ShoppingListDownloader extends AsyncTask<Boolean, Void, ShoppingList> {
    private RecipeApi api;


    private ShoppingListDownloadedCallbacks callbacks;

    @Inject
    public ShoppingListDownloader(RecipeApi api) {
        this.api = api;
    }

    @Override
    protected ShoppingList doInBackground(Boolean... params) {
        try {
            if (!params[0]) {
                RecipeApi.GetShoppingList shoppingList = api.getShoppingList();
                return shoppingList.execute();
            } else {
                RecipeApi.GenerateShoppingList shoppingList = api.generateShoppingList(new DateTime().withTime(0, 0, 0, 0).getMillis());
                return shoppingList.execute();
            }
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
