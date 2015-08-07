package com.tokko.recipesv2.backend.util;

import com.google.android.gcm.server.Sender;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.tokko.recipesv2.backend.engines.GroceryCrudEngine;
import com.tokko.recipesv2.backend.engines.MessagingEngine;
import com.tokko.recipesv2.backend.engines.RecipeUserCrudEngine;
import com.tokko.recipesv2.backend.managers.GroceryManager;
import com.tokko.recipesv2.backend.managers.RecipeUserManager;

public class GuiceModule extends AbstractModule {
    public static void inject(Object o) {
        Guice.createInjector(new GuiceModule()).injectMembers(o);
    }

    @Override
    protected void configure() {
        bind(RecipeUserManager.class);
        bind(RecipeUserCrudEngine.class);
        bind(GroceryCrudEngine.class);
        bind(GroceryManager.class);
        bind(MessagingEngine.class);
        bind(Sender.class).toInstance(new Sender(MessagingEngine.API_KEY));
    }
}
