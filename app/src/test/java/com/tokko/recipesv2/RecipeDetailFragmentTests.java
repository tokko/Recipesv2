package com.tokko.recipesv2;

import android.os.Bundle;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Recipe;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.recipes.RecipeDetailFragment;
import com.tokko.recipesv2.views.EditTextViewSwitchable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import roboguice.RoboGuice;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.robolectric.util.FragmentTestUtil.startVisibleFragment;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/src/main/AndroidManifest.xml")
public class RecipeDetailFragmentTests {
    @Inject
    ItemDetailFragment<Recipe> fragment;
    private ItemDetailFragment.Callbacks callbacks;
    private Recipe recipe;

    @Before
    public void setUp() throws Exception {
        recipe = new Recipe();
        recipe.setTitle("Recipe");
        RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application, new AbstractModule() {
            @Override
            protected void configure() {
                bind(new TypeLiteral<ItemDetailFragment<Recipe>>() {
                }).to(RecipeDetailFragment.class);
            }
        });
        RoboGuice.getInjector(RuntimeEnvironment.application).injectMembers(this);
        callbacks = mock(ItemDetailFragment.Callbacks.class);
        fragment.setCallbacks(callbacks);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ItemDetailFragment.EXTRA_CLASS, Recipe.class);
        bundle.putString(ItemDetailFragment.EXTRA_ENTITY, new AndroidJsonFactory().toString(recipe));

        fragment.setArguments(bundle);
        startVisibleFragment(fragment);
        fragment.enterEditMode();
    }

    @Test
    public void testRecipeIsDisplayed() throws Exception {
        assertEquals(((EditTextViewSwitchable) fragment.getView().findViewById(R.id.recipe_title)).getData(), recipe.getTitle());
    }
}
