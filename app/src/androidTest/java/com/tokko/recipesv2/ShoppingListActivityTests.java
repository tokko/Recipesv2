package com.tokko.recipesv2;

import android.app.Application;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.CollectionResponseGrocery;
import com.tokko.recipesv2.backend.entities.recipeApi.model.CollectionResponseString;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Grocery;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ShoppingList;
import com.tokko.recipesv2.groceries.GroceryAdapter;
import com.tokko.recipesv2.groceries.GroceryLoader;
import com.tokko.recipesv2.masterdetail.AbstractLoader;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;
import com.tokko.recipesv2.recipes.IngredientDetailFragment;
import com.tokko.recipesv2.shoppinglist.ShoppingListActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;

import roboguice.RoboGuice;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ShoppingListActivityTests extends ActivityInstrumentationTestCase2<ShoppingListActivity> {

    private ShoppingListActivity activity;

    public ShoppingListActivityTests() {
        super(ShoppingListActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        Context context = getInstrumentation().getTargetContext().getApplicationContext();

        RecipeApi api = mock(RecipeApi.class);

        RecipeApi.GetShoppinhList getShoppinhList = mock(RecipeApi.GetShoppinhList.class);
        ShoppingList sl = new ShoppingList();
        sl.setItems(new ArrayList<>());
        sl.setId(1L);
        doReturn(getShoppinhList).when(api).getShoppinhList();
        doReturn(sl).when(getShoppinhList).execute();

        RecipeApi.ListGroceries listGroceries = mock(RecipeApi.ListGroceries.class);
        CollectionResponseGrocery collectionResponseGrocery = new CollectionResponseGrocery();
        doReturn(listGroceries).when(api).listGroceries();
        doReturn(collectionResponseGrocery).when(listGroceries).execute();

        RecipeApi.ListUnits listUnits = mock(RecipeApi.ListUnits.class);
        CollectionResponseString units = new CollectionResponseString();
        units.setItems(Collections.singletonList("g"));
        doReturn(listUnits).when(api).listUnits();
        doReturn(units).when(listUnits).execute();

        RoboGuice.overrideApplicationInjector((Application) context.getApplicationContext(), new AbstractModule() {
            @Override
            protected void configure() {
                bind(RecipeApi.class).toInstance(api);
                bind(IngredientDetailFragment.class);
                bind(new TypeLiteral<StringifyableAdapter<Grocery>>() {
                }).to(GroceryAdapter.class);
                bind(new TypeLiteral<AbstractLoader<Grocery>>() {
                }).to(GroceryLoader.class);
            }
        });

        activity = getActivity();
    }

    @Test
    public void testAddButtonClick_OpensIngredientFragment() throws Exception {
        onView(withId(R.id.shoppingListAddbutton)).perform(click());

        onView(withId(R.id.ingredientdetail_grocery)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddButtonClick_AddIngredient_IsAddedToList() throws Exception {
        onView(withId(R.id.shoppingListAddbutton)).perform(click());

        onView(withId(R.id.ingredientdetail_grocery)).perform(typeText("grocery"), closeSoftKeyboard());
        onView(withId(R.id.buttonbar_ok)).perform(click());
        onView(withId(R.id.buttonbar_ok)).perform(click());

        onView(withText("grocery")).check(matches(isDisplayed()));
    }
}
