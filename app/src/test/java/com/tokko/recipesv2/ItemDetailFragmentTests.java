package com.tokko.recipesv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Grocery;
import com.tokko.recipesv2.groceries.GroceryDetailFragment;
import com.tokko.recipesv2.masterdetail.ItemDetailActivity;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.schedule.ScheduleFragment;
import com.tokko.recipesv2.views.EditTextViewSwitchable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import roboguice.RoboGuice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.util.FragmentTestUtil.startVisibleFragment;

@SuppressWarnings("ConstantConditions")
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/app/src/main/AndroidManifest.xml")
public class ItemDetailFragmentTests {

    @Inject
    ItemDetailFragment<Grocery> fragment;
    private Grocery g;
    private RecipeApi api;
    private ItemDetailFragment.Callbacks callbacks;

    @Before
    public void setUp() throws Exception {
        Intent i = new Intent(RuntimeEnvironment.application, ItemDetailActivity.class);
        g = new Grocery();
        g.setTitle("Title");
        i.putExtra(ItemDetailFragment.EXTRA_CLASS, Grocery.class);
        i.putExtra(ItemDetailFragment.EXTRA_ENTITY, new AndroidJsonFactory().toString(g));
        api = mock(RecipeApi.class);
        RecipeApi.CommitGrocery ins = mock(RecipeApi.CommitGrocery.class);
        when(api.commitGrocery(any())).thenReturn(ins);
        RecipeApi.DeleteGrocery rem = mock(RecipeApi.DeleteGrocery.class);
        when(api.deleteGrocery(any())).thenReturn(rem);
        callbacks = mock(ItemDetailFragment.Callbacks.class);
        RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application, new AbstractModule() {
            @Override
            protected void configure() {
                bind(RecipeApi.class).toInstance(api);
                bind(new TypeLiteral<ItemDetailFragment<Grocery>>() {
                }).to(GroceryDetailFragment.class);
            }
        });
        RoboGuice.getInjector(RuntimeEnvironment.application).injectMembers(this);
        fragment.setCallbacks(callbacks);
        fragment.setArguments(i.getExtras());
        startVisibleFragment(fragment);
        fragment.enterEditMode();
    }
    @After
    public void tearDown() throws Exception {
        RoboGuice.Util.reset();
    }
    @Test
    public void testEdit_IsEditing_DeleteButtonIsEnabled() throws Exception {
        ItemDetailFragment<Grocery> fragment = new GroceryDetailFragment();
        Grocery g = new Grocery();
        g.setId(1L);
        Bundle b = new Bundle();
        b.putString(ItemDetailFragment.EXTRA_ENTITY, new AndroidJsonFactory().toString(g));
        b.putSerializable(ItemDetailFragment.EXTRA_CLASS, Grocery.class);
        fragment.setArguments(b);
        startVisibleFragment(fragment);
        fragment.enterEditMode();
        View v = fragment.getView().findViewById(R.id.buttonbar_delete);
        assertTrue(v.isEnabled());
        assertEquals(View.VISIBLE, v.getVisibility());
    }

    @Test
    public void testGroceryDetails_TitleDisplayed() throws Exception {
        String s = ((EditTextViewSwitchable) fragment.getView().findViewById(R.id.grocery_title)).getData();
        assertEquals(g.getTitle(), s);
    }

    @Test
    public void testEdit_ButtonBarVisible() {
        View v = fragment.getView().findViewById(R.id.buttonbar);
        assertEquals(View.VISIBLE, v.getVisibility());
    }

    @Test
    public void testEdit_IsCreating_DeleteButtonIsDisabled() {
        View v = fragment.getView().findViewById(R.id.buttonbar_delete);
        assertFalse(v.isEnabled());
        assertEquals(View.VISIBLE, v.getVisibility());
    }

    @Test
    public void testEdit_IsCreating_CancelButtonIsEnabled() {
        View v = fragment.getView().findViewById(R.id.buttonbar_cancel);
        assertTrue(v.isEnabled());
        assertEquals(View.VISIBLE, v.getVisibility());
    }

    @Test
    public void testEdit_IsCreating_OkButtonIsEnabled() {
        View v = fragment.getView().findViewById(R.id.buttonbar_ok);
        assertTrue(v.isEnabled());
        assertEquals(View.VISIBLE, v.getVisibility());
    }

    @Test
    public void testEdit_OnOkClick_GrocerySaved() throws Exception {
        fragment.getView().findViewById(R.id.buttonbar_ok).performClick();
        Thread.sleep(500);
        verify(api).commitGrocery(any());
    }

    @Test
    public void testEdit_OnDeleteClick_GroceryDeleted() throws Exception {
        fragment.getView().findViewById(R.id.buttonbar_delete).performClick();
        Thread.sleep(500);
        verify(api).deleteGrocery(any());
    }

    @Test
    public void testEdit_DeleteButtonClickNotifiesParent() throws Exception {
        fragment.getView().findViewById(R.id.buttonbar_delete).performClick();
        verify(callbacks).detailFinished();
    }

    @Test
    public void testEdit_EntityIdIsNull_EnterInEditMode() throws Exception{
        assertEquals(View.VISIBLE, fragment.getView().findViewById(R.id.buttonbar).getVisibility());
    }

    @Test
    public void testEdit_EntityIdIsNotNull_EnterInDetailMode() throws  Exception{
        ItemDetailFragment<Grocery> fragment = new GroceryDetailFragment();
        Grocery g = new Grocery();
        g.setId(1L);
        Bundle b = new Bundle();
        b.putString(ItemDetailFragment.EXTRA_ENTITY, new AndroidJsonFactory().toString(g));
        b.putSerializable(ItemDetailFragment.EXTRA_CLASS, Grocery.class);
        fragment.setArguments(b);
        startVisibleFragment(fragment);
        assertEquals(View.GONE, fragment.getView().findViewById(R.id.buttonbar).getVisibility());
    }
}
