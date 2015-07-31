package engines;

import com.tokko.recipesv2.backend.engines.RecipeUserCrudEngine;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import util.TestsWithObjectifyStorage;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        ofy().save().entity(exists).now();
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
    public void testGetUserByEmail_userNotExists_returnsNull() throws Exception {
        RecipeUser user = recipeUserCrudEngine.getUserByEmail(doesNotExist.getEmail());
        assertNull(user);
    }

    @Test
    public void testCreateUser() throws Exception {
        RecipeUser newUser = recipeUserCrudEngine.createUser(doesNotExist.getEmail());
        assertNotNull(newUser);
        assertEquals(doesNotExist.getEmail(), newUser.getEmail());
        assertEquals(doesNotExist.getRegistrationIds(), newUser.getRegistrationIds());
    }

    @Test
    public void testPersistUser() throws Exception {
        String newRegId = "newRegId";
        exists.getRegistrationIds().add(newRegId);
        recipeUserCrudEngine.persistUser(exists);
        RecipeUser updated = ofy().load().type(RecipeUser.class).id(exists.getEmail()).now();
        assertNotNull(updated);
        assertTrue(updated.getRegistrationIds().contains(newRegId));
    }
}