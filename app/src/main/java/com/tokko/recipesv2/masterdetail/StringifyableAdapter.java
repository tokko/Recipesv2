package com.tokko.recipesv2.masterdetail;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.inject.Inject;

import java.util.ArrayList;

import roboguice.RoboGuice;

public class StringifyableAdapter<T> implements ListAdapter {

    private final Context context;
    private final int resource;
    private final int textViewResourceId;
    private ArrayList<T> data = new ArrayList<>();
    private ArrayList<DataSetObserver> observers = new ArrayList<>();

    @Inject
    private LayoutInflater inflater;
    @Inject
    private Stringifier<T> stringifier;
    @Inject
    private IdGetter<T> idGetter;

    public StringifyableAdapter(Context context, int resource, int textViewResourceId) {
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        RoboGuice.getInjector(context).injectMembers(this);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        observers.remove(observer);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return idGetter.getId(getItem(position));
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(resource, null);
        TextView tv = (TextView) v.findViewById(textViewResourceId);
        tv.setText(stringifier.stringify(getItem(position)));
        return v;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    public interface IdGetter<T> {
        long getId(T t);
    }

    public interface Stringifier<T> {
        String stringify(T t);
    }
}
