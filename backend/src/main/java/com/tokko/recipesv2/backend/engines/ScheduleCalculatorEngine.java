package com.tokko.recipesv2.backend.engines;

import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.appengine.repackaged.org.joda.time.DurationFieldType;
import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.entities.ScheduleEntry;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class ScheduleCalculatorEngine {
    public static final int DAYS_AHEAD = 20;

    public List<ScheduleEntry> expandSchedule(DateTime dateTime, List<ScheduleEntry> entries) {
        Collections.sort(entries);
        Queue<ScheduleEntry> q = new ArrayDeque<>();
        q.addAll(entries);
        List<ScheduleEntry> expandedSchedule = new ArrayList<>();
        for (int i = 0; i < DAYS_AHEAD; i++, dateTime = dateTime.withFieldAdded(DurationFieldType.days(), 1)){
            if(!q.isEmpty() && dateTime.isEqual(q.peek().getDate())){
                expandedSchedule.add(q.poll());
            }
            else{
                expandedSchedule.add(new ScheduleEntry(dateTime.getMillis(), new ArrayList<Recipe>()));
            }
        }
        return expandedSchedule;
    }
}
