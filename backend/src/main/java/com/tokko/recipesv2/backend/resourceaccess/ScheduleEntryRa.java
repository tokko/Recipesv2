package com.tokko.recipesv2.backend.resourceaccess;

import com.tokko.recipesv2.backend.entities.RecipeUser;
import com.tokko.recipesv2.backend.entities.ScheduleEntry;

import java.util.List;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;

public class ScheduleEntryRa {

    public List<ScheduleEntry> getScheduleEntries(long date, RecipeUser user){
        return getScheduleEntries(date, Long.MAX_VALUE, user);
    }

    public List<ScheduleEntry> getScheduleEntries(long startDate, long endDate, RecipeUser user) {
        List<ScheduleEntry> list = ofy().load().type(ScheduleEntry.class).ancestor(user).filter("date >=", startDate).filter("date <=", endDate).list();
        for (ScheduleEntry se : list) {
            se.load();
        }
        return list;
    }
    public ScheduleEntry commitEntry(ScheduleEntry entry, RecipeUser user){
        entry.setUser(user);
        entry.prepare();

        ofy().save().entity(entry).now();
        return entry;
    }

    public void commitEntries(Iterable<ScheduleEntry> entries, RecipeUser user){
        for (ScheduleEntry scheduleEntry : entries) {
            commitEntry(scheduleEntry, user);
        }
    }

    public void deleteEntry(ScheduleEntry entry, RecipeUser user) {
        entry.setUser(user);
        entry.prepare();
        ofy().delete().entity(entry).now();
    }

    public void deleteEntries(Iterable<ScheduleEntry> toDelete, RecipeUser user) {
        for (ScheduleEntry entry : toDelete) {
            deleteEntry(entry, user);
        }
    }
}
