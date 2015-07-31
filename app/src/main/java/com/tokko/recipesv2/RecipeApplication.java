package com.tokko.recipesv2;

import android.app.Application;

import com.tokko.recipesv2.groceries.GroceryGuiceModule;

import roboguice.RoboGuice;

public class RecipeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RoboGuice.overrideApplicationInjector(this, new GroceryGuiceModule());
    }
}
