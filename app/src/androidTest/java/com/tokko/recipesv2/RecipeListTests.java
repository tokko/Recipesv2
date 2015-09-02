package com.tokko.recipesv2;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.CollectionResponseString;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Grocery;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Ingredient;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Recipe;
import com.tokko.recipesv2.groceries.GroceryAdapter;
import com.tokko.recipesv2.groceries.GroceryDetailFragment;
import com.tokko.recipesv2.groceries.MockGroceryLoader;
import com.tokko.recipesv2.masterdetail.AbstractLoader;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.masterdetail.ItemListActivity;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;
import com.tokko.recipesv2.recipes.IngredientAdapter;
import com.tokko.recipesv2.recipes.IngredientDetailFragment;
import com.tokko.recipesv2.recipes.InstructionsAdapter;
import com.tokko.recipesv2.recipes.InstructionsDetailFragment;
import com.tokko.recipesv2.recipes.MockRecipeLoader;
import com.tokko.recipesv2.recipes.RecipeAdapter;
import com.tokko.recipesv2.recipes.RecipeDetailFragment;
import com.tokko.recipesv2.recipes.UnitDownloader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import roboguice.RoboGuice;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class RecipeListTests extends ActivityInstrumentationTestCase2<ItemListActivity> {

    private ItemListActivity activity;

    public RecipeListTests() {
        super(ItemListActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        Context context = getInstrumentation().getTargetContext().getApplicationContext();
        Intent startIntent = new Intent(context, ItemListActivity.class);
        startIntent.putExtra(ItemListActivity.EXTRA_ENTITY_CLASS, Recipe.class);
        RoboGuice.overrideApplicationInjector(((Application) context.getApplicationContext()), new AbstractModule() {
            @Override
            protected void configure() {
                bind(UnitDownloader.class).toProvider(() -> new UnitDownloader(null){
                    @Override
                    protected List<String> doInBackground(Void... params) {
                        return Arrays.asList("g", "kg");
                    }
                });
                bind(new TypeLiteral<StringifyableAdapter<Recipe>>() {
                }).to(RecipeAdapter.class);
                bind(new TypeLiteral<StringifyableAdapter<Ingredient>>() {
                }).to(IngredientAdapter.class);
                bind(new TypeLiteral<StringifyableAdapter<String>>() {
                }).to(InstructionsAdapter.class);
                bind(new TypeLiteral<ItemDetailFragment<Recipe>>() {
                }).to(RecipeDetailFragment.class);
                bind(new TypeLiteral<ItemDetailFragment<Ingredient>>() {
                }).to(IngredientDetailFragment.class);
                bind(new TypeLiteral<ItemDetailFragment<String>>() {
                }).to(InstructionsDetailFragment.class);
                bind(RecipeApi.class).toInstance((RecipeApi) ApiFactory.createApi(RecipeApi.Builder.class));
                bind(new TypeLiteral<AbstractLoader<Recipe>>() {
                }).to(MockRecipeLoader.class);

                bind(new TypeLiteral<AbstractLoader<Grocery>>() {
                }).to(MockGroceryLoader.class);
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
    public void testAddTwoIngredients_IngredientDetailsShouldBeCleared() throws Exception {
        createIngredient("grocery", 1, true);

        onView(withText("grocery")).check(matches(isDisplayed()));

        onView(allOf(withId(R.id.editableList_addButton), isDescendantOfA(withId(R.id.ingredient_list)))).perform(click());
        onView(withId(R.id.ingredientdetail_grocery)).check(matches(withText("")));
        onView(withId(R.id.ingredient_quantity)).check(matches(withText("")));
    }

    @Test
    public void testAddIngredients_IsAddedToList() throws Exception {
        createIngredient("grocery", 1, true);

        onView(withText("grocery")).check(matches(isDisplayed()));
    }

    @Test
    public void testEditIngredient_IsUpdated_NotAddingNew() throws Exception{
        createIngredient("grocery", 1, true);

        onView(withText("grocery")).perform(click());
        onView(withId(R.id.ingredientdetail_grocery)).perform(typeText("postfix"), closeSoftKeyboard());
        Thread.sleep(500);

        onView(withId(R.id.buttonbar_ok)).perform(click());
        onView(withId(R.id.buttonbar_ok)).perform(click());
        onView(withText("grocery")).check(doesNotExist());
        onView(withText("grocerypostfix")).check(matches(isDisplayed()));
    }

    @Test
    public void testAddIngredient_Cancel_NotAdded() throws Exception {
        onView(withId(R.id.listad_add)).perform(click());
        onView(allOf(withId(R.id.editableList_addButton), isDescendantOfA(withId(R.id.ingredient_list)))).perform(click());
        onView(withId(R.id.ingredientdetail_grocery)).perform(typeText("grocery"), closeSoftKeyboard());

        onView(withId(R.id.ingredient_quantity)).perform(typeText(String.valueOf(1)), closeSoftKeyboard());

        onView(withId(R.id.buttonbar_cancel)).perform(click());

        onView(withText("grocery")).check(doesNotExist());
    }

    @Test
    public void testEditIngredient_Cancel_NotEdited() throws Exception {
        createIngredient("grocery", 1, true);

        onView(withText("grocery")).perform(click());
        onView(withId(R.id.ingredientdetail_grocery)).perform(typeText("postfix"), closeSoftKeyboard());

        onView(withId(R.id.buttonbar_cancel)).perform(click());
        onView(withText("grocerypostfix")).check(doesNotExist());
        onView(withText("grocery")).check(matches(isDisplayed()));
    }

    @Test
    public void testDeleteIngredient_IsDeleted() throws Exception {
        createIngredient("grocery", 1, true);

        onView(allOf(withId(R.id.deleteImageButton), hasSibling(withText("grocery")))).perform(click());

        onView(withText("grocery")).check(doesNotExist());
    }

    @Test
    public void testDeleteInstruction_IsDeleted() throws Exception {
        createInstruction("instruction text");

        onView(allOf(withId(R.id.deleteImageButton), hasSibling(withText("instruction text")))).perform(click());

        onView(withText("instruction text")).check(doesNotExist());
    }

    @Test
    public void testAddInstruction_InstructionAdded(){
        createInstruction("instruction text");

        onView(withText("instruction text")).check(matches(isDisplayed()));
    }

    @Test
    public void testEditInstruction_IsEdited(){
        String instructionText = "inst";
        String postfix = "post";
        createInstruction(instructionText);
        onView(withText(instructionText)).perform(click());
        onView(withId(R.id.editableStringListInput)).perform(typeText(postfix), closeSoftKeyboard());
        onView(withId(R.id.buttonbar_ok)).perform(click());

        onView(withText(instructionText)).check(doesNotExist());
        onView(withText(instructionText+postfix)).check(matches(isDisplayed()));
    }
    private void createInstruction(String instructionText) {
        onView(withId(R.id.listad_add)).perform(click());
        onView(allOf(withId(R.id.editableList_addButton), isDescendantOfA(withId(R.id.instructionList)))).perform(click());
        onView(withId(R.id.editableStringListInput)).perform(typeText(instructionText), closeSoftKeyboard());
        onView(withId(R.id.buttonbar_ok)).perform(click());
    }

    @Test
    public void testAddInstruction_Cancel_InstructionNotAdded(){
        onView(withId(R.id.listad_add)).perform(click());
        onView(allOf(withId(R.id.editableList_addButton), isDescendantOfA(withId(R.id.instructionList)))).perform(click());
        onView(withId(R.id.editableStringListInput)).perform(typeText("instruction text"), closeSoftKeyboard());
        onView(withId(R.id.buttonbar_cancel)).perform(click());

        onView(withText("instruction text")).check(doesNotExist());
    }

    @Test
    public void testAddIngredient_NewGroceryCancelGroceryDialog_ShouldReturnToIngredientEditDialog(){
        onView(withId(R.id.listad_add)).perform(click());
        onView(allOf(withId(R.id.editableList_addButton), isDescendantOfA(withId(R.id.ingredient_list)))).perform(click());

        onView(withId(R.id.ingredientdetail_grocery)).perform(typeText("grocery"), closeSoftKeyboard());
        onView(withId(R.id.buttonbar_ok)).perform(click());
        onView(withId(R.id.buttonbar_cancel)).perform(click());

        onView(withId(R.id.buttonbar_cancel)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonbar_ok)).check(matches(isDisplayed()));
        onView(withId(R.id.ingredient_quantity)).check(matches(isDisplayed()));
    }

    private void createIngredient(String groceryTitle, int quantity, boolean newGrocery) throws InterruptedException {
        onView(withId(R.id.listad_add)).perform(click());
        onView(allOf(withId(R.id.editableList_addButton), isDescendantOfA(withId(R.id.ingredient_list)))).perform(click());
        onView(withId(R.id.ingredientdetail_grocery)).perform(typeText(groceryTitle), closeSoftKeyboard());
        Thread.sleep(500);
        onView(withId(R.id.ingredient_quantity)).perform(typeText(String.valueOf(quantity)), closeSoftKeyboard());
        Thread.sleep(500);

        onView(withId(R.id.buttonbar_ok)).perform(click());
        if(newGrocery)
            onView(withId(R.id.buttonbar_ok)).perform(click());
    }
}
