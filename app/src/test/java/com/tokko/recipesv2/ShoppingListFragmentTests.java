package com.tokko.recipesv2;

import com.google.inject.AbstractModule;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Grocery;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Ingredient;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ShoppingList;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ShoppingListItem;
import com.tokko.recipesv2.shoppinglist.ShoppingListFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Collection;
import java.util.Collections;

import roboguice.RoboGuice;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.robolectric.util.FragmentTestUtil.startVisibleFragment;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/app/src/main/AndroidManifest.xml")
public class ShoppingListFragmentTests {

    private ShoppingListFragment fragment;

    @Before
    public void setUp() throws Exception {
        ShoppingList sl = new ShoppingList();
        Grocery g = new Grocery();
        g.setTitle("grocery");
        g.setId(1L);
        Ingredient i = new Ingredient();
        i.setGrocery(g);
        i.setId(2L);
        ShoppingListItem sli = new ShoppingListItem();
        sli.setIngredient(i);
        sl.setItems(Collections.singletonList(sli));

        RecipeApi api = mock(RecipeApi.class);
        RecipeApi.GetShoppinhList getShoppinhList = mock(RecipeApi.GetShoppinhList.class);
        doReturn(getShoppinhList).when(api).getShoppinhList();
        doReturn(sl).when(getShoppinhList).execute();
        RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application, new AbstractModule() {
            @Override
            protected void configure() {
                bind(RecipeApi.class).toInstance(api);
            }
        });
        fragment = RoboGuice.getInjector(RuntimeEnvironment.application).getInstance(ShoppingListFragment.class);
        startVisibleFragment(fragment);
    }

    @Test
    public void testListView_DisplayItems() throws Exception {
        int count = fragment.getListView().getAdapter().getCount();
        assertEquals(1, count);
    }
}
