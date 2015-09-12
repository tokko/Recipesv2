package com.tokko.recipesv2.backend.managers;

import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.inject.Inject;
import com.tokko.recipesv2.backend.engines.ScheduleCalculatorEngine;
import com.tokko.recipesv2.backend.entities.RecipeUser;
import com.tokko.recipesv2.backend.entities.ScheduleEntry;
import com.tokko.recipesv2.backend.resourceaccess.RecipeUserRa;
import com.tokko.recipesv2.backend.resourceaccess.ScheduleEntryRa;

import java.util.List;

public class ScheduleEntryManager {

    private ScheduleCalculatorEngine scheduleCalculatorEngine;
    private ScheduleEntryRa scheduleEntryRa;
    private RecipeUserRa recipeUserRa;

    @Inject
    public ScheduleEntryManager(ScheduleCalculatorEngine scheduleCalculatorEngine, ScheduleEntryRa scheduleEntryRa, RecipeUserRa recipeUserRa) {
        this.scheduleCalculatorEngine = scheduleCalculatorEngine;
        this.scheduleEntryRa = scheduleEntryRa;
        this.recipeUserRa = recipeUserRa;
    }

    public List<ScheduleEntry> getSchedule(long date, String email){
        RecipeUser user = recipeUserRa.getUserByEmail(email);
        List<ScheduleEntry> entries = scheduleEntryRa.getScheduleEntries(date, user);
        List<ScheduleEntry> scheduleEntries = scheduleCalculatorEngine.expandSchedule(new DateTime(date), entries);
        return scheduleEntries;
    }

    public void commitSchedule(List<ScheduleEntry> entries, String email) {
        RecipeUser user = recipeUserRa.getUserByEmail(email);
        Iterable<ScheduleEntry> toCommit = scheduleCalculatorEngine.getScheduleEntriesToCommit(entries);
        Iterable<ScheduleEntry> toDelete = scheduleCalculatorEngine.getScheduleEntriesToDelete(entries);
        scheduleEntryRa.commitEntries(toCommit, user);
        scheduleEntryRa.deleteEntries(toDelete, user);
    }
}
