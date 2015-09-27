package unit.engines;

import com.tokko.recipesv2.backend.engines.ShoppingListAggregatorEngine;
import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.entities.ShoppingListItem;
import com.tokko.recipesv2.backend.units.Quantity;
import com.tokko.recipesv2.backend.units.Units;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShoppingListAggregatorEngineTests {

    private ShoppingListAggregatorEngine shoppingListAggregatorEngine;

    @Before
    public void setUp() throws Exception {
        shoppingListAggregatorEngine = new ShoppingListAggregatorEngine();

    }

    @Test
    public void testAggregateIngredients() throws Exception {
        Grocery g1 = new Grocery(1L, "g1");
        Grocery g2 = new Grocery(2L, "g2");

        Ingredient i1 = new Ingredient();
        i1.setGrocery(g1);
        i1.setId(1L);
        i1.setQuantity(new Quantity(Units.KG, 1));

        Ingredient i2 = new Ingredient();
        i2.setGrocery(g2);
        i2.setId(2L);
        i2.setQuantity(new Quantity(Units.KG, 2));

        Ingredient i3 = new Ingredient();
        i3.setGrocery(g1);
        i3.setId(3L);
        i3.setQuantity(new Quantity(Units.KG, 0.5));

        List<ShoppingListItem> ingredients = new ArrayList<>(Arrays.asList(new ShoppingListItem(i1), new ShoppingListItem(i2), new ShoppingListItem(i3)));

        List<ShoppingListItem> aggregated = shoppingListAggregatorEngine.aggregateIngredients(ingredients);

        assertEquals(2, aggregated.size());
        assertEquals(1.5, aggregated.get(0).ingredient.getQuantity().getQuantity(), 0);
        assertEquals(Units.KG, aggregated.get(0).ingredient.getQuantity().getUnit());

        assertEquals(2, aggregated.get(1).ingredient.getQuantity().getQuantity(), 0);
        assertEquals(Units.KG, aggregated.get(1).ingredient.getQuantity().getUnit());

    }
}
