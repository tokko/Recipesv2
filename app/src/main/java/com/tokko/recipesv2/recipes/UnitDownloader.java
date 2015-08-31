package com.tokko.recipesv2.recipes;

import android.os.AsyncTask;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.CollectionResponseString;

import java.io.IOException;
import java.util.List;

public class UnitDownloader extends AsyncTask<Void, Void, List<String>> {

    private RecipeApi api;
    private UnitDownloaderCallback callbacks;

    @Inject
    public UnitDownloader(RecipeApi api) {
        this.api = api;
    }

    public interface UnitDownloaderCallback{
        void onUnitDownloaded(List<String> units);
    }

    public UnitDownloader setCallbacks(UnitDownloaderCallback callbacks){
        this.callbacks = callbacks;
        return this;
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        try {
            CollectionResponseString execute = api.listUnits().execute();
            return execute.getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<String> units) {
        if (units != null) {
           callbacks.onUnitDownloaded(units);
        }
        super.onPostExecute(units);
    }
}