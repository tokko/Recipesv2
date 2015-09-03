package unit.resourceaccess;

import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.appengine.repackaged.org.joda.time.DurationFieldType;
import com.googlecode.objectify.Key;
import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.entities.RecipeUser;
import com.tokko.recipesv2.backend.entities.ScheduleEntry;
import com.tokko.recipesv2.backend.resourceaccess.ScheduleEntryRa;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ScheduleRaTests extends TestsWithObjectifyStorage{
    private RecipeUser user;
    private ScheduleEntryRa scheduleEntryRa;

    @Before
    public void setUp() throws Exception {
        super.setup();
        user = new RecipeUser("email");
        ofy().save().entity(user);
        scheduleEntryRa = new ScheduleEntryRa();
    }

    @Test
    public void testCommitScheduleEntry_DoesNotExist_IsAdded() throws Exception {
        ScheduleEntry se = new ScheduleEntry();
        long date = new DateTime().withDate(2015, 5, 8).getMillis();
        se.setDate(date);
        ScheduleEntry saved = scheduleEntryRa.commitEntry(se, user);
        assertNotNull(saved);
        saved = ofy().load().key(Key.create(Key.create(RecipeUser.class, user.getEmail()), ScheduleEntry.class, saved.getId())).now();
        assertNotNull(saved);
    }

    @Test
    public void testCommitScheduleEntry_Exists_IsUpdated() throws Exception {
        Recipe r = new Recipe();
        r.setTitle("testrecipe");
        r.setRecipeUser(user);

        long date = new DateTime().withDate(2015, 5, 8).getMillis();
        ScheduleEntry se = new ScheduleEntry();
        se.setDate(date);

        ofy().save().entities(r, se).now();

        se.addRecipe(r);

        ScheduleEntry saved = scheduleEntryRa.commitEntry(se, user);
        assertNotNull(saved);
        saved = ofy().load().key(Key.create(Key.create(RecipeUser.class, user.getEmail()), ScheduleEntry.class, saved.getId())).now();
        saved.load();
        assertNotNull(saved);
        assertEquals(1, saved.getRecipes().size());
        assertEquals(r.getId(), saved.getRecipes().get(0).getId());
    }

    @Test
    public void testGetScheduleEntryList() throws Exception {
        DateTime dt = new DateTime().withDate(2015, 5, 8);
        ofy().save().entities(new ScheduleEntry().setUser(user).setDate(dt.getMillis()), new ScheduleEntry().setUser(user).setDate(dt.withFieldAdded(DurationFieldType.days(), 3).getMillis())).now();
        List<ScheduleEntry> scheduleEntries = scheduleEntryRa.getScheduleEntries(dt.getMillis(), user);
        assertNotNull(scheduleEntries);
        assertEquals(2, scheduleEntries.size());
    }

    @Test
    public void testGetScheduleEntryList_ExcludesPastEntries() throws Exception {
        DateTime dt = new DateTime().withDate(2015, 5, 8);
        ofy().save().entities(new ScheduleEntry().setUser(user).setDate(dt.getMillis()), new ScheduleEntry().setUser(user).setDate(dt.withFieldAdded(DurationFieldType.days(), -1).getMillis())).now();
        List<ScheduleEntry> scheduleEntries = scheduleEntryRa.getScheduleEntries(dt.getMillis(), user);
        assertNotNull(scheduleEntries);
        assertEquals(1, scheduleEntries.size());
        assertEquals(dt.getMillis(), scheduleEntries.get(0).getDate());
    }


}
