package engines;

import com.googlecode.objectify.Key;
import com.tokko.recipesv2.backend.engines.GroceryCrudEngine;
import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import util.TestsWithObjectifyStorage;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GroceryCrudEngineTest extends TestsWithObjectifyStorage {
    RecipeUser user;
    List<Grocery> groceryList;
    private GroceryCrudEngine groceryCrudEngine;

    @Before
    public void setUp() throws Exception {
        super.setup();
        user = new RecipeUser("email");
        ofy().save().entity(user).now();
        groceryList = Arrays.asList(new Grocery("Grocery1", user), new Grocery("Grocery2", user), new Grocery("Grocery3", user));
        ofy().save().entities(groceryList).now();

        RecipeUser otherUser = new RecipeUser("email1");
        ofy().save().entities(new Grocery("Grocery4", otherUser)).now();
        groceryCrudEngine = new GroceryCrudEngine();
    }

    @Test
    public void testListGroceries_AllUsersGroceriesReturned() throws Exception {
        List<Grocery> groceries = groceryCrudEngine.listGroceries(user);
        assertEquals(groceryList.size(), groceries.size());
        assertEquals(groceryList, groceries);
    }

    @Test
    public void testListGroceries_OnlyUsersGroceriesReturned() {
        List<Grocery> groceries = groceryCrudEngine.listGroceries(user);
        for (Grocery g : groceries) {
            assertTrue(groceryList.contains(g));
        }
    }

    @Test
    public void testSaveGrocery_newGrocery_IsPersisted() throws Exception {
        Grocery g = new Grocery("title");

        groceryCrudEngine.save(g, user);

        Grocery persisted = ofy().load().key(Key.create(Key.create(RecipeUser.class, user.getEmail()), Grocery.class, g.getId())).now();

        assertNotNull(persisted);
        assertTrue(persisted.getId() > 0);
        assertEquals(g.getTitle(), persisted.getTitle());
        assertEquals(user.getEmail(), persisted.getUser().email);
    }
}