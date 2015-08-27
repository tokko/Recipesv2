package com.tokko.recipesv2.masterdetail;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.inject.Inject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class StringifyableAdapter<T> implements ListAdapter, Iterable<T>, Filterable {

    private final Context context;
    protected ArrayList<T> data = new ArrayList<>();
    protected ArrayList<T> original = new ArrayList<>();
    private ArrayList<DataSetObserver> observers = new ArrayList<>();
    private int resource = android.R.layout.simple_list_item_1;
    private int textViewResourceId = android.R.id.text1;
    @Inject
    private LayoutInflater inflater;

    @Inject
    public StringifyableAdapter(Context context) {
        this.context = context;
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

    protected abstract String getItemString(int position);

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(resource, null);
        TextView tv = (TextView) convertView.findViewById(textViewResourceId);
        tv.setText(getItemString(position));
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    public void clear() {
        data.clear();
        original.clear();
    }

    public void replaceData(List<T> data) {
        clear();
        if (data != null) {
            this.data.addAll(data);
            original.addAll(data);
        }
        for (DataSetObserver obs : observers) {
            obs.onChanged();
        }
    }

    @Override
    public Iterator<T> iterator() {
        return data.iterator();
    }

    public List<T> getItems() {
        return data;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults res = new FilterResults();
                List<T> filtered = new ArrayList<>();
                if (constraint.length() == 0) {
                    res.count = original.size();
                    res.values = original;
                    return res;
                }
                for (int i = 0; i < getCount(); i++) {
                    if (getItemString(i).startsWith(constraint.toString()))
                        filtered.add(getItem(i));
                }
                res.count = filtered.size();
                res.values = filtered;
                return res;
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                try {
                    return (CharSequence) resultValue.getClass().getMethod("getTitle").invoke(resultValue);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return super.convertResultToString(resultValue);
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                data.clear();
                final List<T> values = (List<T>) results.values;
                data.addAll(values);
                for (DataSetObserver obs : observers) {
                    obs.onChanged();
                }
            }
        };
    }

    public void addItem(T t) {
        data.add(t);
        for (DataSetObserver obs : observers) {
            obs.onChanged();
        }
    }

    public void removeItem(int i) {
        data.remove(i);
        for (DataSetObserver obs : observers) {
            obs.onChanged();
        }
    }
}
