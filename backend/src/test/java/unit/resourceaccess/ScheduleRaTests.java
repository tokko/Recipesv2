package unit.resourceaccess;

import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.googlecode.objectify.Key;
import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.entities.RecipeUser;
import com.tokko.recipesv2.backend.entities.ScheduleEntry;
import com.tokko.recipesv2.backend.resourceaccess.ScheduleEntryRa;

import org.junit.Before;
import org.junit.Test;

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
        saved = ofy().load().key(Key.create(Key.create(RecipeUser.class, user.getEmail()), ScheduleEntry.class, date)).now();
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
        saved = ofy().load().key(Key.create(Key.create(RecipeUser.class, user.getEmail()), ScheduleEntry.class, date)).now();
        saved.load();
        assertNotNull(saved);
        assertEquals(1, saved.getRecipes().size());
        assertEquals(r.getId(), saved.getRecipes().get(0).getId());
    }
}
