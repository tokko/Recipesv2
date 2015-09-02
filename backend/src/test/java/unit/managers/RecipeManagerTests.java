package managers;

import com.google.inject.Guice;
import com.googlecode.objectify.Key;
import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.entities.RecipeUser;
import com.tokko.recipesv2.backend.managers.RecipeManager;
import com.tokko.recipesv2.backend.util.GuiceModule;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

import unit.resourceaccess.TestsWithObjectifyStorage;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RecipeManagerTests extends TestsWithObjectifyStorage {
    private RecipeManager recipeManager;

    @Before
    public void setUp() throws Exception {
        super.setup();
        recipeManager = Guice.createInjector(new GuiceModule()).getInstance(RecipeManager.class);
    }

    @Test
    public void testCommitRecipe_WithIngredientAdded_IngredientPersisted() throws Exception {
        Grocery g = new Grocery("g");
        Ingredient i = new Ingredient();
        i.setGrocery(g);
        RecipeUser user = new RecipeUser("emial");
        Recipe recipe = new Recipe();
        recipe.ingredients = new ArrayList<>();
        recipe.ingredients.add(i);
        recipe.setTitle("recipe");
        ofy().save().entity(user).now();

        recipeManager.commitRecipe(recipe, user.getEmail());

        Recipe saved = ofy().load().key(Key.create(Key.create(RecipeUser.class, user.getEmail()), Recipe.class, recipe.getId())).now();
        assertNotNull(saved);
        assertEquals(recipe.getTitle(), saved.getTitle());
        recipe.load();
        assertEquals(1, saved.ingredients.size());
        Ingredient ingredient = saved.ingredients.get(0);
        ingredient.load();
        assertEquals(g.getTitle(), ingredient.getGrocery().getTitle());
    }

    @Test
    public void testCommitRecipe_WithIngredientEdited_IngredientEdited() throws Exception {
        Grocery g = new Grocery("g");
        Ingredient i = new Ingredient();
        i.setGrocery(g);
        RecipeUser user = new RecipeUser("emial");
        Recipe recipe = new Recipe();
        recipe.ingredients = new ArrayList<>();
        recipe.ingredients.add(i);
        recipe.setTitle("recipe");
        ofy().save().entity(user).now();
        recipeManager.commitRecipe(recipe, user.getEmail());
        recipe.load();
        g.setTitle("h");
        i.load();
        recipeManager.commitRecipe(recipe, user.getEmail());

        Recipe saved = ofy().load().key(Key.create(Key.create(RecipeUser.class, user.getEmail()), Recipe.class, recipe.getId())).now();
        assertNotNull(saved);
        assertEquals(recipe.getTitle(), saved.getTitle());
        saved.load();
        assertEquals(1, saved.getIngredients().size());
        assertEquals(g.getTitle(), saved.ingredients.get(0).getGrocery().getTitle());
    }

    @Test
    @Ignore("Temporarily disabled")
    public void testCommitRecipe_WithIngredientRemoved_IngredientRemoved() throws Exception {
        Grocery g = new Grocery("g");
        Ingredient i = new Ingredient();
        i.setGrocery(g);
        RecipeUser user = new RecipeUser("emial");
        Recipe recipe = new Recipe();
        recipe.ingredients = new ArrayList<>();
        recipe.ingredients.add(i);
        recipe.setTitle("recipe");
        ofy().save().entity(user).now();
        recipeManager.commitRecipe(recipe, user.getEmail());

        recipe.getIngredients().clear();
        recipeManager.commitRecipe(recipe, user.getEmail());

        Recipe saved = ofy().load().key(Key.create(Key.create(RecipeUser.class, user.getEmail()), Recipe.class, recipe.getId())).now();
        saved.load();
        assertNotNull(saved);
        assertEquals(recipe.getTitle(), saved.getTitle());
        recipe.load();
        assertEquals(0, saved.ingredients.size());
        Ingredient savedIngredient = ofy().load().key(Key.create(Key.create(RecipeUser.class, user.getEmail()), Ingredient.class, i.getId())).now();
        savedIngredient.load();
        assertNull(savedIngredient);
        Grocery savedGrocery = ofy().load().key(Key.create(Key.create(RecipeUser.class, user.getEmail()), Grocery.class, g.getId())).now();
        savedGrocery.load();
        assertNotNull(savedGrocery);
    }
}
