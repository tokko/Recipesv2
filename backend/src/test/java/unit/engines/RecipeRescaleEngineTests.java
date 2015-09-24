package unit.engines;

import com.tokko.recipesv2.backend.engines.RecipeRescaleEngine;
import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.units.Quantity;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RecipeRescaleEngineTests {

    @Test
    public void testRescaleIngredientsToNumberOfHelpings() throws Exception {
        Ingredient i1 = new Ingredient();
        i1.setQuantity(new Quantity(2.2));
        Ingredient i2 = new Ingredient();
        i2.setQuantity(new Quantity(1.3));
        List<Ingredient> ingredients = Arrays.asList(i1, i2);

        RecipeRescaleEngine engine = new RecipeRescaleEngine();

        List<Ingredient> rescaledIngredients = engine.rescaleIngredientsToNumberOfHelpings(ingredients, 1, 2);

        assertNotNull(rescaledIngredients);
        assertEquals(ingredients.size(), rescaledIngredients.size());
        assertEquals(4.4, rescaledIngredients.get(0).getQuantity().getQuantity(), 0);
        assertEquals(2.6, rescaledIngredients.get(1).getQuantity().getQuantity(), 0);
    }

    @Test
    public void testRescaleIngredients_toThirds() throws Exception {
        Ingredient i1 = new Ingredient();
        i1.setQuantity(new Quantity(3));
        Ingredient i2 = new Ingredient();
        i2.setQuantity(new Quantity(6));
        List<Ingredient> ingredients = Arrays.asList(i1, i2);

        RecipeRescaleEngine engine = new RecipeRescaleEngine();

        List<Ingredient> rescaledIngredients = engine.rescaleIngredientsToNumberOfHelpings(ingredients, 3, 1);

        assertNotNull(rescaledIngredients);
        assertEquals(ingredients.size(), rescaledIngredients.size());
        assertEquals(1, rescaledIngredients.get(0).getQuantity().getQuantity(), 0);
        assertEquals(2, rescaledIngredients.get(1).getQuantity().getQuantity(), 0);
    }
}
