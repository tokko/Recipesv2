package com.tokko.recipesv2.backend.managers;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.engines.QuantityCalculatorEngine;

import java.util.List;

public class QuantityManager {
    private QuantityCalculatorEngine quantityCalculatorEngine;

    @Inject
    public QuantityManager(QuantityCalculatorEngine quantityCalculatorEngine) {
        this.quantityCalculatorEngine = quantityCalculatorEngine;
    }

    public List<String> listUnits() {
        return quantityCalculatorEngine.listUnits();
    }
}
