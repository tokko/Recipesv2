package unit.engines;

import com.tokko.recipesv2.backend.engines.QuantityCalculatorEngine;
import com.tokko.recipesv2.backend.units.Quantity;

import org.junit.Test;


public class QuantityCalculatorEngineTests {

    @Test(expected = UnsupportedOperationException.class)
    public void testgetBaseQuantity_UnsupportedUnit_ThrowsException() throws Exception {
        QuantityCalculatorEngine engine = new QuantityCalculatorEngine();
        engine.getBaseQuantity(new Quantity("holabaloo"));
    }
}
