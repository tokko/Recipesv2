package com.tokko.recipesv2;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.inject.Inject;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Grocery;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Ingredient;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Recipe;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.views.EditTextViewSwitchable;
import com.tokko.recipesv2.views.EditableListView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.List;

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
        Grocery g = new Grocery();
        g.setTitle("foo");
        Ingredient i = new Ingredient();
        i.setGrocery(g);
        recipe.setIngredients(Collections.singletonList(i));
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
        assertEquals(recipe.getTitle(), ((EditTextViewSwitchable) fragment.getView().findViewById(R.id.recipe_title)).getData());
    }

    @Test
    public void testIngredientListIsDisplayed() throws Exception {
        assertEquals(1, ((EditableListView<List<Ingredient>>) fragment.getView().findViewById(R.id.ingredient_list)).getData().size());
    }
}
