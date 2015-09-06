package unit.managers;

import com.tokko.recipesv2.backend.engines.MessagingEngine;
import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.RecipeUser;
import com.tokko.recipesv2.backend.managers.GroceryManager;
import com.tokko.recipesv2.backend.resourceaccess.GroceryRa;
import com.tokko.recipesv2.backend.resourceaccess.RecipeUserRa;

import org.junit.Test;

import unit.resourceaccess.TestsWithObjectifyStorage;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GroceryManagerTests extends TestsWithObjectifyStorage {

    @Test
    public void testGroceryCommited_MessageSent() throws Exception {
        RecipeUserRa rura = mock(RecipeUserRa.class);
        GroceryRa gra = mock(GroceryRa.class);
        MessagingEngine me = mock(MessagingEngine.class);

        String email = "email";
        RecipeUser user = new RecipeUser(email);
        doReturn(user).when(rura).getUserByEmail(email);

        Grocery g = new Grocery(1L, "title", user);
        doReturn(g).when(gra).save(g, user);
        GroceryManager gm = new GroceryManager(rura, gra, me);

        gm.commitGrocery(g, user);
        verify(me).sendMessage(g, user);
    }
}
