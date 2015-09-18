package unit.engines;

import com.tokko.recipesv2.backend.engines.ShoppingListGeneralListPreparerEngine;
import com.tokko.recipesv2.backend.entities.ShoppingListItem;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ShoppingListGeneralListPreparerEngineTests {

    private ShoppingListGeneralListPreparerEngine shoppingListGeneralListPreparerEngine;

    @Before
    public void setUp() throws Exception {
        shoppingListGeneralListPreparerEngine = new ShoppingListGeneralListPreparerEngine();
    }


    @Test
    public void testMarkItemsAsGeneralList_AllItemsAreMarked() throws Exception {
        List<ShoppingListItem> items = Arrays.asList(new ShoppingListItem(), new ShoppingListItem(), new ShoppingListItem());

        List<ShoppingListItem> newItems = shoppingListGeneralListPreparerEngine.markItemsAsGeneralList(items);
        assertNotNull(newItems);
        assertEquals(items.size(), newItems.size());
        boolean allIsMarked = newItems.stream().allMatch(new Predicate<ShoppingListItem>() {
            @Override
            public boolean test(ShoppingListItem shoppingListItem) {
                return !shoppingListItem.generated;
            }
        });
        assertTrue(allIsMarked);
    }
}
