package unit.engines;

import com.tokko.recipesv2.backend.engines.QuantityCalculatorEngine;
import com.tokko.recipesv2.backend.units.Quantity;
import com.tokko.recipesv2.backend.units.Unit;

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
        Quantity baseQuantity = engine.getBaseQuantity(new Quantity(Unit.KG, 1.5));
        assertEquals(Unit.G, baseQuantity.getUnit());
        assertEquals(1500, baseQuantity.getQuantity(), 0);
    }

    @Test
    public void testGetBaseQuantity_Grams() throws Exception {
        Quantity baseQuantity = engine.getBaseQuantity(new Quantity(Unit.G, 500));
        assertEquals(baseQuantity.getQuantity(), 500, 0);
        assertEquals(Unit.G, baseQuantity.getUnit());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUpUnit_UnsuppordedUnit_ThrowsException() throws Exception {
        engine.upQuantity(new Quantity("holabaloo"));
    }

    @Test
    public void testUpQuantity_Grams() throws Exception {
        Quantity uppedQuantity = engine.upQuantity(new Quantity(Unit.G, 1345));
        assertEquals(1.345, uppedQuantity.getQuantity(), 0);
        assertEquals(Unit.KG, uppedQuantity.getUnit());
    }

    @Test
    public void testUpQuantity_kiloGrams() throws Exception {
        Quantity uppedQuantity = engine.upQuantity(new Quantity(Unit.KG, 1.234));
        assertEquals(1.234, uppedQuantity.getQuantity(), 0);
        assertEquals(Unit.KG, uppedQuantity.getUnit());
    }

    @Test
    public void testUpQuantity_GramsLessThanOneKilo_IsStillGrams() throws Exception {
        Quantity uppedQuantity = engine.upQuantity(new Quantity(Unit.G, 22));
        assertEquals(22, uppedQuantity.getQuantity(), 0);
        assertEquals(Unit.G, uppedQuantity.getUnit());
    }

    @Test
    public void testUpQuantity_MlIsLessThan1Dl_IsStillMl() throws Exception{
        Quantity q = engine.upQuantity(new Quantity(Unit.ML, 50));
        assertEquals(50, q.getQuantity(), 0);
        assertEquals(Unit.ML, q.getUnit());
    }

    @Test
    public void testUpQuantity_MlIsMoreThan1Dl_IsMl() throws Exception{
        Quantity q = engine.upQuantity(new Quantity(Unit.ML, 150));
        assertEquals(1.5, q.getQuantity(), 0);
        assertEquals(Unit.DL, q.getUnit());
    }
}
