package com.tokko.recipesv2;

import android.view.View;
import android.widget.ListView;

import com.google.inject.AbstractModule;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.CollectionResponseRecipe;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Recipe;
import com.tokko.recipesv2.recipes.RecipeAdapter;
import com.tokko.recipesv2.schedule.RecipeListFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import roboguice.RoboGuice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.robolectric.util.FragmentTestUtil.startVisibleFragment;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/app/src/main/AndroidManifest.xml")
public class RecipeListTests {

    private RecipeListFragment fragment;

    @Before
    public void setUp() throws Exception {
        Recipe r = new Recipe();
        r.setId(3L);
        r.setTitle("title");
        Recipe r1 = new Recipe();
        r1.setId(4L);
        r1.setTitle("title");


        RecipeApi api = mock(RecipeApi.class);
        RecipeApi.ListRecipes listRecipes = mock(RecipeApi.ListRecipes.class);
        CollectionResponseRecipe recipeResp = new CollectionResponseRecipe();
        recipeResp.setItems(Arrays.asList(r, r1));

        doReturn(listRecipes).when(api).listRecipes();
        doReturn(recipeResp).when(listRecipes).execute();

        RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application, new AbstractModule() {
            @Override
            protected void configure() {
                bind(RecipeApi.class).toInstance(api);
            }
        });

        fragment = RoboGuice.getInjector(RuntimeEnvironment.application).getInstance(RecipeListFragment.class);
        startVisibleFragment(fragment);
    }

    @After
    public void tearDown() throws Exception {
        RoboGuice.Util.reset();
    }

    @Test
    public void testRecipeListDisplayed() throws Exception {
        View view = fragment.getView();
        ListView viewById = (ListView) view.findViewById(android.R.id.list);
        RecipeAdapter adapter = (RecipeAdapter) viewById.getAdapter();
        assertNotNull(adapter);
        assertEquals(2, adapter.getCount());
        assertEquals(3L, adapter.getItemId(0));
        assertEquals(4L, adapter.getItemId(1));
    }

}
