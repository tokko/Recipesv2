package unit.resourceaccess;

import com.googlecode.objectify.Key;
import com.tokko.recipesv2.backend.resourceaccess.RecipeRa;
import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RecipeRaTests extends TestsWithObjectifyStorage {

    private RecipeUser user;
    private RecipeRa recipeRa;

    @Before
    public void setUp() throws Exception {
        super.setup();
        user = new RecipeUser("email");
        ofy().save().entities(user).now();

        recipeRa = new RecipeRa();
    }

    @Test
    public void testListRecipesForUser() throws Exception {
        Recipe recipe = new Recipe("recipe", user);
        ofy().save().entities(recipe).now();
        List<Recipe> recipes = recipeRa.listRecipesForUser(user);
        assertNotNull(recipes);
        assertEquals(1, recipes.size());
        assertEquals(recipe.getId(), recipes.get(0).getId());
    }

    @Test
    public void testGetRecipe() throws Exception {
        Recipe r = new Recipe();
        r.setTitle("test");
        r.setRecipeUser(user);

        ofy().save().entity(r).now();

        Recipe saved = recipeRa.getRecipe(user, r.getId());

        assertNotNull(saved);
        assertEquals(r.getId(), saved.getId());
        assertEquals(r.getTitle(), saved.getTitle());
    }

    @Test
    public void testInsertRecipe_DoesNotExist_IsInserted() throws Exception {
        Recipe recipe = new Recipe("recipe");
        Recipe saved = recipeRa.commitRecipe(recipe, user);
        assertNotNull(saved);
        assertNotNull(saved.getId());
        saved = ofy().load().key(Key.create(Key.create(RecipeUser.class, user.getEmail()), Recipe.class, saved.getId())).now();
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(user, saved.getRecipeUser());
        assertEquals(recipe.getTitle(), saved.getTitle());
    }
}
