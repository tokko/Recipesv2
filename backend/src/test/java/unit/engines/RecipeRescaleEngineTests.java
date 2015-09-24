package unit.engines;

import com.tokko.recipesv2.backend.engines.RecipeRescaleEngine;
import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.units.Quantity;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RecipeRescaleEngineTests {

    @Test
    public void testRescaleIngredients_toDouble() throws Exception {
        List<Ingredient> ingredients = getIngredientList(2.2, 1.3);

        RecipeRescaleEngine engine = new RecipeRescaleEngine();

        List<Ingredient> rescaledIngredients = engine.rescaleIngredientsToNumberOfHelpings(ingredients, 1, 2);

        assertIngredientQuantity(rescaledIngredients, 4.4, 2.6);
    }

    private void assertIngredientQuantity(List<Ingredient> rescaledIngredients, double... expected) {
        assertNotNull(rescaledIngredients);
        assertEquals(expected.length, rescaledIngredients.size());
        for (int i = 0; i < expected.length; i++) {
            double q = expected[i];
            assertEquals(q, rescaledIngredients.get(i).getQuantity().getQuantity(), 0);
        }
    }

    private List<Ingredient> getIngredientList(double... quantities) {
        List<Ingredient> ret = new ArrayList<>();
        for (int i = 0; i < quantities.length; i++) {
            double q = quantities[i];
            Ingredient i1 = new Ingredient();
            i1.setQuantity(new Quantity(q));
            ret.add(i1);
        }
        return ret;
    }

    @Test
    public void testRescaleIngredients_toThirds() throws Exception {
        List<Ingredient> ingredients = getIngredientList(3, 6);

        RecipeRescaleEngine engine = new RecipeRescaleEngine();

        List<Ingredient> rescaledIngredients = engine.rescaleIngredientsToNumberOfHelpings(ingredients, 3, 1);

        assertNotNull(rescaledIngredients);
        assertEquals(ingredients.size(), rescaledIngredients.size());
        assertEquals(1, rescaledIngredients.get(0).getQuantity().getQuantity(), 0);
        assertEquals(2, rescaledIngredients.get(1).getQuantity().getQuantity(), 0);
    }

}
