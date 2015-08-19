package com.tokko.recipesv2.masterdetail;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.inject.Key;
import com.google.inject.util.Types;
import com.tokko.recipesv2.R;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Grocery;

import java.util.List;

import roboguice.RoboGuice;
import roboguice.fragment.provided.RoboListFragment;

public class ItemListFragment<T> extends RoboListFragment implements LoaderManager.LoaderCallbacks<List<T>> {
    public static final String EXTRA_CLASS = "class";
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    Class<T> clz;
    private Callbacks mCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private StringifyableAdapter<T> adapter;

    public ItemListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clz = (Class<T>) getArguments().getSerializable(EXTRA_CLASS);
        adapter = (StringifyableAdapter<T>) RoboGuice.getInjector(getActivity()).getInstance(Key.<StringifyableAdapter<T>>get(Types.newParameterizedType(StringifyableAdapter.class, clz)));
        setListAdapter(adapter);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.itemfragmentlist, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.listad_add:
                try {
                    T t = clz.newInstance();
                    mCallbacks.onItemSelected(t);
                } catch (java.lang.InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.manage_groceries:
                Intent i = new Intent(getActivity(), ItemListActivity.class).addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra(ItemListActivity.EXTRA_TITLE, "Groceries");
                i.putExtra(ItemListActivity.EXTRA_ENTITY_CLASS, Grocery.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
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
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = null;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        T item = adapter.getItem(position);
        mCallbacks.onItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    @Override
    public Loader<List<T>> onCreateLoader(int id, Bundle args) {
        Loader<List<T>> instance = (Loader<List<T>>) RoboGuice.getInjector(getActivity()).getInstance(Key.<AbstractLoader<T>>get(Types.newParameterizedType(AbstractLoader.class, clz)));
        return instance;
    }

    @Override
    public void onLoadFinished(Loader<List<T>> loader, List<T> data) {
        adapter.replaceData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<T>> loader) {
        adapter.clear();
    }

    public interface Callbacks {

        void onItemSelected(Object entity);
    }
}
