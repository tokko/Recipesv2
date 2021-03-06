package com.tokko.recipesv2.masterdetail;

import android.content.ClipData;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.inject.Inject;
import com.tokko.recipesv2.R;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class StringifyableAdapter<T> implements ListAdapter, Iterable<T>, Filterable {

    private final Context context;
    protected ArrayList<T> data = new ArrayList<>();
    protected ArrayList<T> original = new ArrayList<>();
    private ArrayList<DataSetObserver> observers = new ArrayList<>();
    private int textViewResourceId = android.R.id.text1;
    @Inject
    private LayoutInflater inflater;
    private boolean delete;


    private boolean isDraggable;

    @Inject
    public StringifyableAdapter(Context context) {
        this.context = context;
    }

    public boolean isDraggable() {
        return isDraggable;
    }

    public void setIsDraggable(boolean isDraggable) {
        this.isDraggable = isDraggable;
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

    public void setDeleteEnabled(boolean delete) {
        this.delete = delete;
        notifyChange();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    protected int getResource(){
        return  R.layout.adapterentry;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(getResource(), null);
        TextView tv = (TextView) convertView.findViewById(textViewResourceId);
        tv.setText(getItemString(position));
        View deleteButton = convertView.findViewById(R.id.deleteImageButton);
        convertView.setTag(getItem(position));
        convertView.setOnTouchListener(new MyThouchListener());
        if( deleteButton != null) {
            if (delete) {
                deleteButton.setTag(position);
                deleteButton.setOnClickListener(v -> removeItem((Integer) v.getTag()));
                deleteButton.setVisibility(View.VISIBLE);
            } else
                deleteButton.setVisibility(View.GONE);
        }
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
        notifyChange();
    }

    public void notifyChange() {
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
                if (data != null) {
                    data.clear();
                    final List<T> values = (List<T>) results.values;
                    if(values != null)
                        data.addAll(values);
                    notifyChange();
                }
            }
        };
    }

    public void addItem(T t) {
        data.add(t);
        notifyChange();
    }

    public void removeItem(int i) {
        data.remove(i);
        notifyChange();
    }

    public void addItem(Integer isUpdatingPosition, T entity) {
        data.add(isUpdatingPosition, entity);
    }

    private final class MyThouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (!isDraggable) return false;
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = null;
                try {
                    data = ClipData.newPlainText(new AndroidJsonFactory().toPrettyString(view.getTag()), "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            } else {
                return false;
            }
        }
    }
}
