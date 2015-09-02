package com.tokko.recipesv2;

import android.app.Application;
import android.app.KeyguardManager;
import android.content.Context;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.tokko.recipesv2.groceries.GroceryGuiceModule;
import com.tokko.recipesv2.masterdetail.MasterDetailGuiceModule;
import com.tokko.recipesv2.recipes.RecipeGuiceModule;

import roboguice.RoboGuice;

public class RecipeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApiFactory.credential = GoogleAccountCredential.usingAudience(getApplicationContext(), "server:client_id:826803278070-0gs94ct68qhnn9b2tpi1mnuptu5al77n.apps.googleusercontent.com");
        RoboGuice.overrideApplicationInjector(this, new MasterDetailGuiceModule(), new GroceryGuiceModule(), new RecipeGuiceModule());
        if (BuildConfig.DEBUG) {
            KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

            KeyguardManager.KeyguardLock keyguardLock = km.newKeyguardLock("TAG"); //TODO: fix this
            keyguardLock.disableKeyguard();
        }
    }
}
