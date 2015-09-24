package unit.engines;

import com.tokko.recipesv2.backend.engines.RecipeRescaleEngine;
import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.units.Quantity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(value = Parameterized.class)
public class RecipeRescaleEngineTests {

    private final int from;
    private final int to;
    private final double[] params;
    private final double[] expected;

    public RecipeRescaleEngineTests(int from, int to, double[] params, double[] expected) {
        this.from = from;
        this.to = to;
        this.params = params;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1, 2, new double[]{2.2, 1.3}, new double[]{4.4, 2.6}},
                {3, 1, new double[]{3, 6}, new double[]{1, 2}},
                {2, 3, new double[]{2, 3}, new double[]{3, 4.5}},
        });
    }

    @Test
    public void testRescaleIngredients() throws Exception {
        List<Ingredient> ingredients = getIngredientList(params);

        RecipeRescaleEngine engine = new RecipeRescaleEngine();

        List<Ingredient> rescaledIngredients = engine.rescaleIngredientsToNumberOfHelpings(ingredients, from, to);

        assertIngredientQuantity(rescaledIngredients, expected);
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
/*
    @Test
    public void testRescaleIngredients_toThirds() throws Exception {
        List<Ingredient> ingredients = getIngredientList(3, 6);

        RecipeRescaleEngine engine = new RecipeRescaleEngine();

        List<Ingredient> rescaledIngredients = engine.rescaleIngredientsToNumberOfHelpings(ingredients, 3, 1);

        assertIngredientQuantity(rescaledIngredients, 1, 2);
    }
*/
}
