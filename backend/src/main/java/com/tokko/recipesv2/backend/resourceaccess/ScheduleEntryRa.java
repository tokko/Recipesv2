package com.tokko.recipesv2.backend.resourceaccess;

import com.tokko.recipesv2.backend.entities.RecipeUser;
import com.tokko.recipesv2.backend.entities.ScheduleEntry;

import java.util.List;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;

public class ScheduleEntryRa {

    public List<ScheduleEntry> getScheduleEntries(long date, RecipeUser user){
        List<ScheduleEntry> list = ofy().load().type(ScheduleEntry.class).ancestor(user).filter("date >=", date).list();
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
}
