package unit.resourceaccess;

import com.tokko.recipesv2.backend.endpoints.Constants;
import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.entities.RecipeUser;
import com.tokko.recipesv2.backend.entities.ShoppingList;
import com.tokko.recipesv2.backend.entities.ShoppingListItem;
import com.tokko.recipesv2.backend.resourceaccess.ShoppingListRa;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ShoppingListRaTests extends TestsWithObjectifyStorage {

    private RecipeUser user;
    private ShoppingListRa shoppingListRastRa;
    private Grocery g;
    private Ingredient i;

    @Before
    public void setUp() throws Exception {
        super.setup();
        user = new RecipeUser("email");
        ofy().save().entity(user).now();
        shoppingListRastRa = new ShoppingListRa();

        g = new Grocery();
        g.setUser(user);
        g.setTitle("grocery");
        g.prepare();
        ofy().save().entity(g).now();
        g.load();

        i = new Ingredient();
        i.setUser(user);
        i.setGrocery(g);
        i.prepare();
        ofy().save().entity(i).now();
        i.load();
    }

    @Test
    public void testCommitShoppingList_DoesNotExist_IsSaved() throws Exception {
        ShoppingListItem sli = new ShoppingListItem();
        sli.ingredient = i;
        ShoppingList sl = new ShoppingList();
        sl.setExpirationDate(new DateTime().withDate(2015, 8, 5).getMillis());
        sl.addItem(sli);
        sl.setUser(user);
        shoppingListRastRa.commitShoppingList(sl);

        ShoppingList saved = ofy().load().type(ShoppingList.class).parent(user).id(sl.getId()).now();
        assertNotNull(saved);
        assertEquals(1, saved.getItems().size());
        assertEquals(i.getId(), saved.getItems().get(0).ingredient.getId());
    }

    @Test
    public void testWithDeletedIngredient_IngredientIsDeleted() throws Exception {
        ShoppingListItem sli = new ShoppingListItem();
        sli.ingredient = i;
        ShoppingList sl = new ShoppingList();
        sl.setExpirationDate(new DateTime().withDate(2015, 8, 5).getMillis());
        sl.addItem(sli);
        sl.prepare();
        sl.setUser(user);
        ofy().save().entity(sl).now();

        sl.getItems().clear();

        shoppingListRastRa.commitShoppingList(sl);

        ShoppingList saved = ofy().load().type(ShoppingList.class).parent(user).id(sl.getId()).now();
        assertTrue(saved.getItems().isEmpty());
    }

    @Test
    public void testDeleteShoppingList_IsDeleted() throws Exception {
        ShoppingListItem sli = new ShoppingListItem();
        sli.ingredient = i;
        ShoppingList sl = new ShoppingList();
        sl.setExpirationDate(new DateTime().withDate(2015, 8, 5).getMillis());
        sl.addItem(sli);
        sl.setUser(user);
        sl.prepare();
        ofy().save().entity(sl).now();

        shoppingListRastRa.deleteShoppingList(sl);

        ShoppingList saved = ofy().load().type(ShoppingList.class).parent(user).id(sl.getId()).now();
        assertNull(saved);
    }

    @Test
    public void testGetLatestShoppingList_ReturnsLatest() throws Exception {
        ShoppingList sl = new ShoppingList();
        sl.setExpirationDate(new DateTime().withDate(2015, 8, 5).getMillis());
        sl.setUser(user);

        ShoppingList sl1 = new ShoppingList();
        sl1.setExpirationDate(new DateTime().withDate(2015, 8, 6).getMillis());
        sl1.setUser(user);

        ofy().save().entities(sl, sl1).now();

        ShoppingList latest = shoppingListRastRa.getLatestShoppingList(user);

        assertNotNull(latest);
        assertEquals(sl1.getId(), latest.getId());
    }

    @Test
    public void testGetLatestShoppingList_DoesNotReturnGeneralList() throws Exception {
        ShoppingList sl = new ShoppingList();
        sl.setExpirationDate(new DateTime().withDate(2015, 8, 5).getMillis());
        sl.setUser(user);
        ShoppingList sl1 = new ShoppingList();
        sl1.setExpirationDate(new DateTime().withDate(2015, 8, 6).getMillis());
        sl1.setUser(user);
        sl1.setId(Constants.GENERAL_LIST_ID);

        ofy().save().entities(sl, sl1).now();

        ShoppingList latest = shoppingListRastRa.getLatestShoppingList(user);

        assertNotNull(latest);
        assertEquals(sl.getId(), latest.getId());
    }

    @Test
    public void testGetShoppingList_ReturnsList() throws Exception {
        ShoppingList ls = new ShoppingList();
        ls.setUser(user);

        ofy().save().entity(ls).now();

        ShoppingList saved = shoppingListRastRa.getShoppingList(user, ls.getId());

        assertNotNull(saved);
        assertEquals(ls.getId(), saved.getId());
    }
}
