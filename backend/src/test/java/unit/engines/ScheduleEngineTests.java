package unit.engines;

import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.appengine.repackaged.org.joda.time.DateTimeConstants;
import com.google.appengine.repackaged.org.joda.time.DurationFieldType;
import com.tokko.recipesv2.backend.engines.ScheduleCalculatorEngine;
import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.entities.ScheduleEntry;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class ScheduleEngineTests {
    ScheduleCalculatorEngine scheduleCalculatorEngine;

    @Before
    public void setUp() throws Exception {
        scheduleCalculatorEngine = new ScheduleCalculatorEngine();
    }

    @Test
    public void testExpandSchedule() throws Exception {
        DateTime dt = new DateTime().withDate(2015, 3, 4).withTime(0, 0, 0, 0);
        long date = dt.getMillis();
        Recipe recipe = new Recipe();
        recipe.setTitle("Recipe");
        recipe.setId(1234L);
        List<ScheduleEntry> entries = Arrays.asList(new ScheduleEntry(date, Collections.singletonList(recipe)), new ScheduleEntry(date + DateTimeConstants.MILLIS_PER_DAY * 3, Collections.singletonList(recipe)));

        List<ScheduleEntry> expanded = scheduleCalculatorEngine.expandSchedule(dt, entries);

        assertEquals(ScheduleCalculatorEngine.DAYS_AHEAD, expanded.size());
        assertFalse(expanded.get(0).getRecipes().isEmpty());
        for (int i = 0; i<expanded.size(); i++, dt = dt.withFieldAdded(DurationFieldType.days(), 1)) {
            assertEquals("Diff for index: " + i + " is: " + (dt.getMillis() - expanded.get(i).getDate()) / DateTimeConstants.MILLIS_PER_DAY, dt.getMillis(), expanded.get(i).getDate());
        }
    }

    @Test
    public void testExpandSchedule_EntriesTooFarAheadExcluded(){
        DateTime dt = new DateTime().withDate(2015, 3, 4).withTime(0, 0, 0, 0);
        long date = dt.getMillis();
        Recipe recipe = new Recipe();
        recipe.setTitle("Recipe");
        recipe.setId(1234L);
        List<ScheduleEntry> entries = new ArrayList<>(Arrays.asList(new ScheduleEntry(date + DateTimeConstants.MILLIS_PER_DAY, Collections.singletonList(recipe)), new ScheduleEntry(date + DateTimeConstants.MILLIS_PER_DAY * (ScheduleCalculatorEngine.DAYS_AHEAD + 1), Collections.singletonList(recipe))));

        List<ScheduleEntry> expanded = scheduleCalculatorEngine.expandSchedule(dt, entries);

        int c = 0;
        for (ScheduleEntry se : expanded) {
            if(!se.getRecipes().isEmpty()) c++;
        }
        assertEquals(1, c);
    }

    @Test
    public void testGetScheduleEntriesToCommit_OnlyNonEmptyRecipesReturned() throws Exception {
        ScheduleEntry se = new ScheduleEntry(new DateTime().getMillis(), Collections.singletonList(new Recipe()));
        ScheduleEntry se1 = new ScheduleEntry(new DateTime().getMillis(), Collections.singletonList(new Recipe()));
        ScheduleEntry se2 = new ScheduleEntry(new DateTime().getMillis(), Collections.<Recipe>emptyList());
        List<ScheduleEntry> entries = Arrays.asList(se, se1, se2);

        List<ScheduleEntry> toCommit = scheduleCalculatorEngine.getScheduleEntriesToCommit(entries);
        assertNotNull(toCommit);
        assertEquals(2, toCommit.size());
        assertEquals(se.getId(), toCommit.get(0).getId());
        assertEquals(se1.getId(), toCommit.get(1).getId());
    }

    @Test
    public void testGetScheduleEntriesToDelete_OnlyEmptyRecipesReturned() throws Exception {
        ScheduleEntry se = new ScheduleEntry(new DateTime().getMillis(), Collections.singletonList(new Recipe()));
        ScheduleEntry se1 = new ScheduleEntry(new DateTime().getMillis(), Collections.singletonList(new Recipe()));
        ScheduleEntry se2 = new ScheduleEntry(new DateTime().getMillis(), Collections.<Recipe>emptyList());
        List<ScheduleEntry> entries = Arrays.asList(se, se1, se2);

        List<ScheduleEntry> toDelete = scheduleCalculatorEngine.getScheduleEntriesToDelete(entries);
        assertNotNull(toDelete);
        assertEquals(1, toDelete.size());
        assertEquals(se2.getId(), toDelete.get(0).getId());
    }
}
