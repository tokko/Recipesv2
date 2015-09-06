package com.tokko.recipesv2;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.CollectionResponseScheduleEntry;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ScheduleEntry;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;
import com.tokko.recipesv2.schedule.ScheduleFragment;
import com.tokko.recipesv2.util.TimeUtils;

import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import roboguice.RoboGuice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.robolectric.util.FragmentTestUtil.startVisibleFragment;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/app/src/main/AndroidManifest.xml")
public class ScheduleFragmentTests {

    private ScheduleFragment fragment;

    @Before
    public void setUp() throws Exception {
        DateTime date = new DateTime().withDate(2015, 8, 5);
        ScheduleEntry se = new ScheduleEntry();
        se.setDate(date.getMillis());
        se.setId(1L);
        ScheduleEntry se1 = new ScheduleEntry();
        se1.setDate(date.withFieldAdded(DurationFieldType.days(), 1).getMillis());
        se1.setId(2L);

        List<ScheduleEntry> entries = Arrays.asList(se, se1);

        RecipeApi api = mock(RecipeApi.class);
        RecipeApi.GetSchedule getSchedule = mock(RecipeApi.GetSchedule.class);
        CollectionResponseScheduleEntry resp = new CollectionResponseScheduleEntry();
        resp.setItems(entries);
        doReturn(getSchedule).when(api).getSchedule(anyLong());
        doReturn(resp).when(getSchedule).execute();
        //when(api.getSchedule(anyLong())).thenReturn(getSchedule);
        //when(getSchedule.execute()).thenReturn(resp);

        RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application, new AbstractModule() {
            @Override
            protected void configure() {
                bind(RecipeApi.class).toInstance(api);
                bind(ScheduleFragment.class);
                bind(TimeUtils.class).toInstance(new TimeUtils() {
                    @Override
                    public DateTime getCurrentTime() {
                        return date;
                    }
                });
            }
        });

        fragment = RoboGuice.getInjector(RuntimeEnvironment.application).getInstance(ScheduleFragment.class);
        startVisibleFragment(fragment);
    }

    @Test
    public void testSchduleEntryListDisplayed() throws Exception {
        ScheduleFragment fragment = this.fragment;
        View view = fragment.getView();
        ExpandableListView viewById = (ExpandableListView) view.findViewById(android.R.id.list);
        ScheduleFragment.ExpandableAdapter adapter = (ScheduleFragment.ExpandableAdapter) viewById.getExpandableListAdapter();
        assertNotNull(adapter);
        assertEquals(2, adapter.getGroupCount());
        assertTrue(adapter.getGroup(0).getDate() < adapter.getGroup(1).getDate());
    }
}
