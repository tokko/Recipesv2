package unit.resourceaccess;

import com.googlecode.objectify.Key;
import com.tokko.recipesv2.backend.resourceaccess.IngredientRa;
import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.entities.RecipeUser;
import com.tokko.recipesv2.backend.units.Quantity;

import org.junit.Before;
import org.junit.Test;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IngredientRaTests extends TestsWithObjectifyStorage {

    private RecipeUser user;
    private IngredientRa ingredientRa;

    @Before
    public void setUp() throws Exception {
        super.setup();
        user = new RecipeUser("email");
        ofy().save().entity(user).now();
        ingredientRa = new IngredientRa();
    }

    @Test
    public void testCommitIngredient_IsCommitted() throws Exception {
        Grocery g = new Grocery("g", user);
        Quantity q = new Quantity();
        q.setQuantity(1);
        q.setUnit("hg");
        ofy().save().entity(g).now();
        Ingredient ingredient = new Ingredient();
        ingredient.setGrocery(g);
        ingredient.setQuantity(q);
        ingredientRa.commitIngredient(ingredient, user);
        Ingredient saved = ofy().load().key(Key.create(Key.create(RecipeUser.class, user.getEmail()), Ingredient.class, ingredient.getId())).now();
        assertNotNull(saved);
        assertEquals(g.getId(), saved.getGrocery().getId());
    }

}
