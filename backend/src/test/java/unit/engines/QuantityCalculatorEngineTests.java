package unit.engines;

import com.tokko.recipesv2.backend.engines.QuantityCalculatorEngine;
import com.tokko.recipesv2.backend.units.Quantity;
import com.tokko.recipesv2.backend.units.Units;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class QuantityCalculatorEngineTests {

    private QuantityCalculatorEngine engine;

    @Before
    public void setUp() throws Exception {
        engine = new QuantityCalculatorEngine();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testgetBaseQuantity_UnsupportedUnit_ThrowsException() throws Exception {
        engine.getBaseQuantity(new Quantity("holabaloo"));
    }

    @Test
    public void testGetBaseQuantity_KiloGrams() throws Exception {
        Quantity baseQuantity = engine.getBaseQuantity(new Quantity(Units.KG, 1.5));
        assertEquals(1500, baseQuantity.getQuantity(), 0);
        assertEquals(Units.G, baseQuantity.getUnit());
    }

    @Test
    public void testGetBaseQuantity_Grams() throws Exception {
        Quantity baseQuantity = engine.getBaseQuantity(new Quantity(Units.G, 500));
        assertEquals(baseQuantity.getQuantity(), 500, 0);
        assertEquals(Units.G, baseQuantity.getUnit());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUpUnit_UnsuppordedUnit_ThrowsException() throws Exception {
        engine.upQuantity(new Quantity("holabaloo"));
    }

    @Test
    public void testUpQuantity_Grams() throws Exception {
        Quantity uppedQuantity = engine.upQuantity(new Quantity(Units.G, 1345));
        assertEquals(1.345, uppedQuantity.getQuantity(), 0);
        assertEquals(Units.KG, uppedQuantity.getUnit());
    }

    @Test
    public void testUpQuantity_kiloGrams() throws Exception {
        Quantity uppedQuantity = engine.upQuantity(new Quantity(Units.KG, 1.234));
        assertEquals(1.234, uppedQuantity.getQuantity(), 0);
        assertEquals(Units.KG, uppedQuantity.getUnit());
    }
}
