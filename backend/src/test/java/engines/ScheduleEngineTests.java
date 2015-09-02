package engines;

import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.appengine.repackaged.org.joda.time.DateTimeConstants;
import com.google.appengine.repackaged.org.joda.time.DurationFieldType;
import com.tokko.recipesv2.backend.engines.ScheduleCalculatorEngine;
import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.entities.ScheduleEntry;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
        List<ScheduleEntry> entries = Arrays.asList(new ScheduleEntry(date + DateTimeConstants.MILLIS_PER_DAY, Collections.singletonList(recipe)), new ScheduleEntry(date + DateTimeConstants.MILLIS_PER_DAY * 3, Collections.singletonList(recipe)));

        List<ScheduleEntry> expanded = scheduleCalculatorEngine.expandSchedule(dt, entries);

        assertEquals(ScheduleCalculatorEngine.DAYS_AHEAD, expanded.size());
        for (int i = 0; i<expanded.size(); i++, dt = dt.withFieldAdded(DurationFieldType.days(), 1)) {
            assertEquals("Diff for index: " + i + " is: " + (dt.getMillis()-expanded.get(i).getDate())/DateTimeConstants.MILLIS_PER_DAY, dt.getMillis(), expanded.get(i).getDate());
        }
    }

}
