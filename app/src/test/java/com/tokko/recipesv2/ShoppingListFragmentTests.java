package com.tokko.recipesv2;

import android.app.FragmentManager;
import android.view.View;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Grocery;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Ingredient;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ShoppingList;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ShoppingListItem;
import com.tokko.recipesv2.groceries.GroceryAdapter;
import com.tokko.recipesv2.groceries.GroceryLoader;
import com.tokko.recipesv2.masterdetail.AbstractLoader;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;
import com.tokko.recipesv2.recipes.IngredientDetailFragment;
import com.tokko.recipesv2.shoppinglist.ShoppingListFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Collections;

import roboguice.RoboGuice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.util.FragmentTestUtil.startVisibleFragment;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/app/src/main/AndroidManifest.xml")
public class ShoppingListFragmentTests {
    private IngredientDetailFragment ingredientDetailFragment;

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
        RecipeApi.GetShoppingList shoppingList = mock(RecipeApi.GetShoppingList.class);
        doReturn(shoppingList).when(api).getShoppingList();
        doReturn(sl).when(shoppingList).execute();
        RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application, new AbstractModule() {


            @Override
            protected void configure() {
                bind(RecipeApi.class).toInstance(api);
                ingredientDetailFragment = mock(IngredientDetailFragment.class);
                bind(new TypeLiteral<StringifyableAdapter<Grocery>>(){}).to(GroceryAdapter.class);
                bind(new TypeLiteral<AbstractLoader<Grocery>>(){}).to(GroceryLoader.class);
                bind(IngredientDetailFragment.class).toInstance(ingredientDetailFragment);
            }
        });
        fragment = RoboGuice.getInjector(RuntimeEnvironment.application).getInstance(ShoppingListFragment.class);
        startVisibleFragment(fragment);
    }

    @Test
    public void testButtonBar_DeleteButtonHidden() throws Exception {
        View viewById = fragment.getView().findViewById(R.id.buttonbar_delete);
        assertEquals(View.GONE, viewById.getVisibility());
    }

    @Test
    public void testButtonBarVisibility_IsVisible() throws Exception {
        View viewById = fragment.getView().findViewById(R.id.buttonbar);
        assertEquals(View.VISIBLE, viewById.getVisibility());
    }

    @Test
    public void testListView_DisplayItems() throws Exception {
        int count = fragment.getListView().getAdapter().getCount();
        assertEquals(1, count);
    }

    @Test
    public void testListView_OnDeleteClicked_IsDeleted() throws Exception {
        View view = fragment.getListView().getAdapter().getView(0, null, fragment.getListView()).findViewById(R.id.deleteImageButton);
        assertNotNull(view);
        assertEquals(View.VISIBLE, view.getVisibility());
        view.performClick();
        int count = fragment.getListView().getAdapter().getCount();
        assertEquals(0, count);
    }

    @Test
    public void testAddButtonClick_ShowsDetailFragment() throws Exception {
        fragment.getView().findViewById(R.id.shoppingListAddbutton).performClick();
        verify(ingredientDetailFragment).show(any(FragmentManager.class), anyString());
    }
}
