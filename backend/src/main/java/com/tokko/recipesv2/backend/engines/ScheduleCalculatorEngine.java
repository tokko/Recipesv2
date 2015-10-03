package com.tokko.recipesv2.backend.engines;

import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.appengine.repackaged.org.joda.time.DateTimeConstants;
import com.tokko.recipesv2.backend.entities.ScheduleEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class ScheduleCalculatorEngine {
    public static final int DAYS_AHEAD = 20;

    public List<ScheduleEntry> expandSchedule(DateTime dateTime, List<ScheduleEntry> entries) {
        removeFutureEntries(dateTime, entries);
        TreeSet<ScheduleEntry> set = new TreeSet<>(entries);
        for (int i = 0; i < DAYS_AHEAD; i++, dateTime = dateTime.plusDays(1)) {
            set.add(new ScheduleEntry(dateTime.getMillis()));
        }
        return new ArrayList<>(set);
    }

    private void removeFutureEntries(DateTime dateTime, List<ScheduleEntry> entries) {
        for (Iterator<ScheduleEntry> it = entries.iterator(); it.hasNext(); ) {
            ScheduleEntry current = it.next();
            if (current.getDate() - dateTime.getMillis() >= DateTimeConstants.MILLIS_PER_DAY * 20)
                it.remove();
        }
    }

    public List<ScheduleEntry> getScheduleEntriesToCommit(List<ScheduleEntry> entries) {
        return getEmptyEntries(entries, false);
    }

    private List<ScheduleEntry> getEmptyEntries(List<ScheduleEntry> entries, boolean b) {
        List<ScheduleEntry> prunedList = new ArrayList<>();
        for (ScheduleEntry entry : entries) {
            if (b == entry.getRecipes().isEmpty())
                prunedList.add(entry);
        }
        return prunedList;
    }

    public List<ScheduleEntry> getScheduleEntriesToDelete(List<ScheduleEntry> entries) {
        return getEmptyEntries(entries, true);
    }

    public long getExpirationDate(List<ScheduleEntry> entries) {
        long date = 0;
        for (ScheduleEntry entry : entries) {
            if (entry.date > date) date = entry.date;
        }
        return date;
    }
}
