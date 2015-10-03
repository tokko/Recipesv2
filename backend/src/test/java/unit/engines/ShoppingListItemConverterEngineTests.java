package unit.engines;

import com.tokko.recipesv2.backend.engines.ShoppingListItemConverterEngine;
import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.entities.ShoppingListItem;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ShoppingListItemConverterEngineTests {

    @Test
    public void testConvertIngredientToShoppingListItems() throws Exception {
        Ingredient i = new Ingredient();
        i.setId(1L);
        Ingredient i1 = new Ingredient();
        i1.setId(2L);
        List<Ingredient> ingredients = Arrays.asList(i, i1);

        List<ShoppingListItem> items = new ShoppingListItemConverterEngine().convertIngredientToShoppingListItems(ingredients);

        assertNotNull(items);
        assertEquals(ingredients.size(), items.size());
        assertEquals(1L, items.get(0).ingredient.getId().longValue());
        assertEquals(2L, items.get(1).ingredient.getId().longValue());
    }


}
