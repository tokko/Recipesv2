package engines;

import com.tokko.recipesv2.backend.engines.RecipeUserCrudEngine;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

import util.TestsWithObjectifyStorage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RecipeUserCrudEngineTest extends TestsWithObjectifyStorage {

    RecipeUser exists;
    private RecipeUserCrudEngine recipeUserCrudEngine;
    private RecipeUser doesNotExist;

    @Before
    public void setUp() throws Exception {
        super.setup();
        recipeUserCrudEngine = new RecipeUserCrudEngine();
        exists = new RecipeUser();
        exists.setEmail("email@domain.toplevel");
        exists.setRegistrationIds(new ArrayList<String>() {{
            add("regid");
        }});
        ofy.save().entity(exists).now();
        doesNotExist = new RecipeUser();
        doesNotExist.setEmail("email2@domain.toplevel");
    }

    @Test
    public void testGetUserByEmail_userExists_returnsUser() throws Exception {
        RecipeUser user = recipeUserCrudEngine.getUserByEmail(exists.getEmail());
        assertNotNull(user);
        assertEquals(exists.getEmail(), user.getEmail());
    }

    @Test
    @Ignore("Not implementsed yet")
    public void testCreateUser() throws Exception {

    }

    @Test
    @Ignore("Not implementsed yet")
    public void testAddRegistrationIdToUser() throws Exception {

    }

    @Test
    @Ignore("Not implementsed yet")
    public void testPersistUser() throws Exception {

    }
}