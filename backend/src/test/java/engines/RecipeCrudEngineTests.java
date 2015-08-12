package engines;

import com.tokko.recipesv2.backend.engines.RecipeCrudEngine;
import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import util.TestsWithObjectifyStorage;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RecipeCrudEngineTests extends TestsWithObjectifyStorage {

    private RecipeUser user;
    private RecipeCrudEngine recipeCrudEngine;

    @Before
    public void setUp() throws Exception {
        super.setup();
        user = new RecipeUser("email");
        ofy().save().entities(user).now();

        recipeCrudEngine = new RecipeCrudEngine();
    }

    @Test
    public void testListRecipesForUser() throws Exception {
        Recipe recipe = new Recipe("recipe", user);
        ofy().save().entities(recipe).now();
        List<Recipe> recipes = recipeCrudEngine.listRecipesForUser(user);
        assertNotNull(recipes);
        assertEquals(1, recipes.size());
        assertEquals(recipe.getId(), recipes.get(0).getId());
    }
}
