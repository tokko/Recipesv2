package engines;

import com.googlecode.objectify.Key;
import com.tokko.recipesv2.backend.engines.IngredientCrudEngine;
import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import org.junit.Before;
import org.junit.Test;

import util.TestsWithObjectifyStorage;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IngredientCrudEngineTests extends TestsWithObjectifyStorage {

    private RecipeUser user;
    private IngredientCrudEngine ingredientCrudEngine;

    @Before
    public void setUp() throws Exception {
        super.setup();
        user = new RecipeUser("email");
        ofy().save().entity(user).now();
        ingredientCrudEngine = new IngredientCrudEngine();
    }

    @Test
    public void testCommitIngredient_IsCommitted() throws Exception {
        Grocery g = new Grocery("g", user);
        ofy().save().entity(g).now();
        Ingredient ingredient = new Ingredient();
        ingredient.setGrocery(g);
        ingredientCrudEngine.commitIngredient(ingredient, user);
        Ingredient saved = ofy().load().key(Key.create(Key.create(RecipeUser.class, user.getEmail()), Ingredient.class, ingredient.getId())).now();
        assertNotNull(saved);
        assertEquals(g.getId(), saved.getGrocery().getId());
    }

}
