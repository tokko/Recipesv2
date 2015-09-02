package com.tokko.recipesv2;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.TextView;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Grocery;
import com.tokko.recipesv2.groceries.GroceryAdapter;
import com.tokko.recipesv2.groceries.GroceryDetailFragment;
import com.tokko.recipesv2.groceries.MockGroceryLoader;
import com.tokko.recipesv2.masterdetail.AbstractLoader;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.masterdetail.ItemListActivity;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import roboguice.RoboGuice;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
public class ItemListActivityTests extends ActivityInstrumentationTestCase2<ItemListActivity> {
    private ItemListActivity activity;

    public ItemListActivityTests() {
        super(ItemListActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        Context context = getInstrumentation().getTargetContext().getApplicationContext();
        Intent startIntent = new Intent(context, ItemListActivity.class);
        startIntent.putExtra(ItemListActivity.EXTRA_ENTITY_CLASS, Grocery.class);
        RoboGuice.overrideApplicationInjector(((Application) context.getApplicationContext()), new AbstractModule() {
            @Override
            protected void configure() {
                bind(new TypeLiteral<AbstractLoader<Grocery>>() {
                }).to(MockGroceryLoader.class);
                bind(RecipeApi.class).toInstance((RecipeApi) ApiFactory.createApi(RecipeApi.Builder.class));
                bind(new TypeLiteral<StringifyableAdapter<Grocery>>() {
                }).to(GroceryAdapter.class);
                bind(new TypeLiteral<ItemDetailFragment<Grocery>>() {
                }).to(GroceryDetailFragment.class);
            }
        });
        setActivityIntent(startIntent);
        activity = getActivity();

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        RoboGuice.Util.reset();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(activity);
    }

    @Test
    public void testOnListItemClick_opensDetailFragment() {
        onData(anything()).atPosition(2).perform(click());
        onView(withId(R.id.grocery_title)).check(matches(isDisplayed()));
    }
}
