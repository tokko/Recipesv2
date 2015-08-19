
import com.tokko.recipesv2.backend.units.HectoGram;
import com.tokko.recipesv2.backend.units.Quantity;

import org.junit.Assert;
import org.junit.Test;

public class UnitsTests {


    @Test
    public void convertToBase_ConvertsProperly() {
        Quantity q = new Quantity(100, new HectoGram());
        Assert.assertEquals(10000.0, q.getQuantity().doubleValue(), 0);
    }

    @Test
    public void quantityToString_ScalesUpProperly() {
        Quantity q = new Quantity(100, new HectoGram());
        Assert.assertEquals("100.0hg", q.toString());
    }

}
