package com.tokko.recipesv2.schedule;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tokko.recipesv2.R;

import roboguice.activity.RoboActivity;

public class ScheduleActivity extends RoboActivity {

    private RecipeListFragment recipeFragment;
    private ScheduleFragment scheduleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduleactivity);
        recipeFragment = (RecipeListFragment) getFragmentManager().findFragmentById(R.id.recipeListFragment);
        scheduleFragment = (ScheduleFragment) getFragmentManager().findFragmentById(R.id.scheduleFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
