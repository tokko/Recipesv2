package com.tokko.recipesv2.backend.managers;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.engines.ScheduleCalculatorEngine;

public class ScheduleEntryManager {

    private ScheduleCalculatorEngine scheduleCalculatorEngine;

    @Inject
    public ScheduleEntryManager(ScheduleCalculatorEngine scheduleCalculatorEngine) {
        this.scheduleCalculatorEngine = scheduleCalculatorEngine;
    }
}
