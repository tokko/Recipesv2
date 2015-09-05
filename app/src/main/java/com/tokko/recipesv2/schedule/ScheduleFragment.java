package com.tokko.recipesv2.schedule;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ScheduleEntry;

import java.util.List;

import roboguice.fragment.provided.RoboListFragment;


public class ScheduleFragment extends RoboListFragment implements LoaderManager.LoaderCallbacks<List<ScheduleEntry>>{

    @Inject
    private ScheduleLoader scheduleLoader;
    private ArrayAdapter<ScheduleEntry> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getLoaderManager().destroyLoader(0);
    }

    @Override
    public Loader<List<ScheduleEntry>> onCreateLoader(int id, Bundle args) {
        return scheduleLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<ScheduleEntry>> loader, List<ScheduleEntry> data) {
        adapter.clear();
        adapter.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<ScheduleEntry>> loader) {
        adapter.clear();
        adapter.notifyDataSetInvalidated();
    }
}
