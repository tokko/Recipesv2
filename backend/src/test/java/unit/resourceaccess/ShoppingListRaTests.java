package unit.resourceaccess;

import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.entities.RecipeUser;
import com.tokko.recipesv2.backend.entities.ShoppingList;
import com.tokko.recipesv2.backend.entities.ShoppingListItem;
import com.tokko.recipesv2.backend.resourceaccess.ShoppingListRa;

import org.junit.Before;
import org.junit.Test;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ShoppingListRaTests extends TestsWithObjectifyStorage {

    private RecipeUser user;
    private ShoppingListRa shoppingListRastRa;

    @Before
    public void setUp() throws Exception {
        super.setup();
        user = new RecipeUser("email");
        ofy().save().entity(user).now();
        shoppingListRastRa = new ShoppingListRa();
    }

    @Test
    public void testCommitShoppingList_DoesNotExist_IsSaved() throws Exception {
        Grocery g = new Grocery();
        g.setUser(user);
        g.setTitle("grocery");
        g.prepare();
        ofy().save().entity(g).now();
        g.load();

        Ingredient i = new Ingredient();
        i.setUser(user);
        i.setGrocery(g);
        i.prepare();
        ofy().save().entity(i).now();
        i.load();

        ShoppingListItem sli = new ShoppingListItem();
        sli.ingredient = i;

        ShoppingList sl = new ShoppingList();
        sl.setDate(new DateTime().withDate(2015, 8, 5));
        sl.addItem(sli);

        shoppingListRastRa.commitShoppingList(sl, user);

        ShoppingList saved = ofy().load().type(ShoppingList.class).parent(user).id(sl.getId()).now();
        assertNotNull(saved);
        assertEquals(1, saved.getItems().size());
        assertEquals(i.getId(), saved.getItems().get(0).ingredient.getId());
    }
}
