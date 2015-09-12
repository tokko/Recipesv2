package unit.managers;

import com.google.inject.Guice;
import com.googlecode.objectify.Key;
import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.entities.RecipeUser;
import com.tokko.recipesv2.backend.managers.RecipeManager;
import com.tokko.recipesv2.backend.units.Quantity;
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
    @Ignore("Value by reference bs with localstorage. Shit is cray")
    public void testCommitRecipe_WithIngredientEdited_IngredientEdited() throws Exception {
        RecipeUser user = new RecipeUser("emial");
        ofy().save().entity(user).now();

        Grocery g = new Grocery("g", user);
        g.prepare();
        ofy().save().entity(g).now();
        g.load();

        Ingredient i = new Ingredient();
        i.setUser(user);
        i.setGrocery(g);
        i.prepare();
        ofy().save().entity(i).now();
        i.load();

        Recipe recipe = new Recipe();
        recipe.setRecipeUser(user);
        recipe.ingredients = new ArrayList<>();
        recipe.ingredients.add(i);
        recipe.setTitle("recipe");
        recipe.prepare();
        ofy().save().entity(recipe).now();

        recipe.load();
        i.setQuantity(new Quantity(2));
        recipeManager.commitRecipe(recipe, user.getEmail());

        Recipe saved = ofy().load().type(Recipe.class).parent(user).id(recipe.getId()).now();
        assertNotNull(saved);
        assertEquals(recipe.getTitle(), saved.getTitle());
        saved.load();
        assertEquals(1, saved.getIngredients().size());
        assertEquals(0, 2, i.getQuantity().getQuantity());
    }

    @Test
    @Ignore("Value by reference bs with localstorage. Shit is cray")
    public void testCommitRecipe_WithIngredientRemoved_IngredientRemoved() throws Exception {
        RecipeUser user = new RecipeUser("emial");
        ofy().save().entity(user).now();

        Grocery g = new Grocery("g");
        g.setUser(user);
        g.prepare();
        ofy().save().entity(g).now();

        Ingredient i = new Ingredient();
        i.setGrocery(g);
        i.setUser(user);
        i.prepare();
        ofy().save().entity(i).now();

        Recipe recipe = new Recipe();
        recipe.ingredients = new ArrayList<>();
        recipe.ingredients.add(i);
        recipe.setTitle("recipe");
        recipe.setRecipeUser(user);
        recipe.prepare();

        ofy().save().entities(recipe).now();

        recipe.load();
        recipe.getIngredients().clear();
        recipeManager.commitRecipe(recipe, user.getEmail());

        Recipe saved = ofy().load().type(Recipe.class).parent(user).id(recipe.getId()).now();
        saved.load();

        assertNotNull(saved);
        assertEquals(recipe.getTitle(), saved.getTitle());

        recipe.load();
        assertEquals(0, saved.ingredients.size());

        Ingredient savedIngredient = ofy().load().type(Ingredient.class).parent(user).id(i.getId()).now();
        savedIngredient.load();
        assertNull(savedIngredient);

        Grocery savedGrocery = ofy().load().type(Grocery.class).parent(user).id(g.getId()).now();
        savedGrocery.load();
        assertNotNull(savedGrocery);
    }
}
