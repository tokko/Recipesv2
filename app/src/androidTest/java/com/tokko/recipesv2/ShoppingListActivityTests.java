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
import com.tokko.recipesv2.backend.entities.recipeApi.model.Ingredient;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Quantity;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ShoppingList;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ShoppingListItem;
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
import java.util.UUID;

import roboguice.RoboGuice;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ShoppingListActivityTests extends ActivityInstrumentationTestCase2<ShoppingListActivity> {

    private ShoppingListActivity activity;
    private Grocery grocery;
    private Ingredient ingredient;

    public ShoppingListActivityTests() {
        super(ShoppingListActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        Context context = getInstrumentation().getTargetContext().getApplicationContext();

        grocery = new Grocery();
        grocery.setId(1L);
        grocery.setTitle("MockedGrocery");

        ingredient = new Ingredient();
        ingredient.setId(2L);
        ingredient.setGrocery(grocery);
        Quantity quantity = new Quantity();
        quantity.setUnit("g");
        quantity.setQuantity(2.0);
        ingredient.setQuantity(quantity);

        ShoppingListItem sli = new ShoppingListItem();
        sli.setIngredient(ingredient);

        RecipeApi api = mock(RecipeApi.class);

        RecipeApi.GetShoppingList shoppingList = mock(RecipeApi.GetShoppingList.class);
        ShoppingList sl = new ShoppingList();
        sl.setItems(new ArrayList<>());
        sl.getItems().add(sli);
        sl.setId(1L);
        doReturn(shoppingList).when(api).getShoppingList();
        doReturn(sl).when(shoppingList).execute();

        RecipeApi.ListGroceries listGroceries = mock(RecipeApi.ListGroceries.class);
        CollectionResponseGrocery collectionResponseGrocery = new CollectionResponseGrocery();
        collectionResponseGrocery.setItems(Collections.singletonList(grocery));
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
    public void testAddButtonClick_OpensIngredientFragmentWithDeleteButtonHidden() throws Exception {
        onView(withId(R.id.shoppingListAddbutton)).perform(click());

        onView(withId(R.id.ingredientdetail_grocery)).check(matches(isDisplayed()));

        onView(withId(R.id.buttonbar_delete)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testAddButtonClick_AddIngredient_IsAddedToList() throws Exception {
        onView(withId(R.id.shoppingListAddbutton)).perform(click());

        String grocery = UUID.randomUUID().toString();
        onView(withId(R.id.ingredientdetail_grocery)).perform(typeText(grocery), closeSoftKeyboard());
        Thread.sleep(200);

        onView(withId(R.id.ingredient_quantity)).perform(typeText("12"), closeSoftKeyboard());
        Thread.sleep(200);

        onView(withId(R.id.buttonbar_ok)).perform(click());
        Thread.sleep(200);

        onView(withId(R.id.buttonbar_ok)).perform(click());
        Thread.sleep(200);

        onView(withText(containsString(grocery))).check(matches(isDisplayed()));
    }

    @Test
    public void testAddButtonClick_DeleteIngredient_IsDeletedFromList() throws Exception {
        onView(allOf(hasSibling(withText(containsString(grocery.getTitle()))), withId(R.id.deleteImageButton))).perform(click());

        onView(withText(containsString(grocery.getTitle()))).check(doesNotExist());
    }

    @Test
    public void testListItemClick_OpensIngredientFragmentWithDeleteButtonHidden() throws Exception {
        onView(withText(containsString(grocery.getTitle()))).perform(click());

        onView(withId(R.id.ingredientdetail_grocery)).check(matches(isDisplayed()));

        onView(withId(R.id.buttonbar_delete)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testListItemClick_EditedIngredient_IsEditing() throws Exception {
        String title = ingredient.getGrocery().getTitle();
        Double quantity = ingredient.getQuantity().getQuantity();

        onView(withText(containsString(title))).perform(click());

        onView(withText(containsString(title))).check(matches(isDisplayed()));
        onView(withText(containsString((String.valueOf(quantity))))).check(matches(isDisplayed()));
    }

    @Test
    public void testListItemClick_EditedIngredient_IsEdited() throws Exception {
        String title = grocery.getTitle();
        onView(withText(containsString(title))).perform(click());
        Thread.sleep(500);
        String grocery = UUID.randomUUID().toString();
        Thread.sleep(500);
        onView(withId(R.id.ingredientdetail_grocery)).perform(clearText(), typeText(grocery), closeSoftKeyboard());
        Thread.sleep(500);
        onView(withId(R.id.buttonbar_ok)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.buttonbar_ok)).perform(click());
        Thread.sleep(500);
        onView(withText(containsString(title))).check(doesNotExist());
        Thread.sleep(500);
        onView(withText(containsString(grocery))).check(matches(isDisplayed()));
    }
}
