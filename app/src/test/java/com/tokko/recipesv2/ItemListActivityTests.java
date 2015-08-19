package com.tokko.recipesv2;

import android.app.Fragment;
import android.content.Intent;
import android.widget.ListView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Grocery;
import com.tokko.recipesv2.groceries.GroceryAdapter;
import com.tokko.recipesv2.masterdetail.AbstractLoader;
import com.tokko.recipesv2.masterdetail.ItemListActivity;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import roboguice.RoboGuice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/app/src/main/AndroidManifest.xml")
public class ItemListActivityTests {

    private ItemListActivity activity;
    private List<Grocery> groceries;
    private ListView listView;

    @Before
    public void setUp() throws Exception {
        Grocery g1 = new Grocery();
        Grocery g2 = new Grocery();
        Grocery g3 = new Grocery();
        g1.setId(1L);
        g2.setId(2L);
        g3.setId(3L);
        g1.setTitle("Grocery1");
        g2.setTitle("Grocery2");
        g3.setTitle("Grocery3");
        groceries = Arrays.asList(g1, g2, g3);
        // why wont this work?
        //stubbing seems to be ignored for some reason even though the mock is injected properyl.
        //GroceryLoader mockLoader = spy(new GroceryLoader(RuntimeEnvironment.application, null));
        //doReturn(groceries).when(mockLoader).loadInBackground();
        RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application, new AbstractModule() {
            @Override
            protected void configure() {
                bind(new TypeLiteral<AbstractLoader<Grocery>>() {
                }).toInstance(new MockGroceryLoader(RuntimeEnvironment.application, null, groceries));
                bind(RecipeApi.class).toInstance((RecipeApi) ApiFactory.createApi(RecipeApi.Builder.class));
                bind(new TypeLiteral<StringifyableAdapter<Grocery>>() {
                }).to(GroceryAdapter.class);
            }
        });
        activity = Robolectric.buildActivity(ItemListActivity.class)
                .withIntent(new Intent(RuntimeEnvironment.application.getApplicationContext(), ItemListActivity.class).putExtra(ItemListActivity.EXTRA_ENTITY_CLASS, Grocery.class))
                .create().start().resume().visible().get();

        listView = (ListView) activity.findViewById(android.R.id.list);
    }

    @Test
    public void testPreconditions() throws Exception {
        assertNotNull(activity);
        assertNotNull(listView);
        Fragment fragmentById = activity.getFragmentManager().findFragmentById(R.id.item_list);
        assertNotNull(fragmentById);
        assertTrue(fragmentById.isVisible());
    }

    @Test
    public void testListContainsCorrectItems() throws Exception {
        StringifyableAdapter<Grocery> adapter = (StringifyableAdapter<Grocery>) listView.getAdapter();
        assertNotNull(adapter);
        assertEquals(groceries.size(), adapter.getCount());
        List<Long> fromAdapter = Stream.of(adapter).map(Grocery::getId).collect(Collectors.toList());
        List<Long> actualIds = Stream.of(groceries).map(Grocery::getId).collect(Collectors.toList());
        for (Long id : fromAdapter) {
            assertTrue(actualIds.contains(id));
        }

    }
}
