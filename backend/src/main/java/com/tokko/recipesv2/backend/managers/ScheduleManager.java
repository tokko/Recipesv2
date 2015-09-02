package com.tokko.recipesv2.backend.managers;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.engines.ScheduleCalculatorEngine;

public class ScheduleManager {

    private ScheduleCalculatorEngine scheduleCalculatorEngine;

    @Inject
    public ScheduleManager(ScheduleCalculatorEngine scheduleCalculatorEngine) {
        this.scheduleCalculatorEngine = scheduleCalculatorEngine;
    }
}
