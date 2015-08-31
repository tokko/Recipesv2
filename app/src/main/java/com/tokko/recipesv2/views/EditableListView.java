package com.tokko.recipesv2.views;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.inject.Inject;
import com.tokko.recipesv2.R;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;
import com.tokko.recipesv2.recipes.IngredientDetailFragment;

import java.io.IOException;
import java.util.List;

import roboguice.RoboGuice;

public class EditableListView<T> extends LinearLayout implements Editable<List<T>> {
    protected final Button addButton;
    @Inject
    protected StringifyableAdapter<T> adapter;
    @Inject
    protected ItemDetailFragment<T> detailFragment;
    private List<T> original;
    @Inject
    private LayoutInflater inflater;
    @Inject
    private FragmentManager fragmentManager;

    private ListView lv;
    private Class<? extends List> clz;

    public EditableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        RoboGuice.getInjector(context).injectMembers(this);

        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.editablelistview, this, true);
        lv = (ListView) v.findViewById(R.id.editable_list);
        lv.setAdapter(adapter);
        addButton = (Button) v.findViewById(R.id.editableList_addButton);

        addButton.setOnClickListener((view) -> {
            detailFragment.getArguments().remove(ItemDetailFragment.EXTRA_ENTITY);
            detailFragment.clear();
            detailFragment.show(fragmentManager, "tag");
        });
        lv.setOnItemClickListener((parent, view, position, id) -> {
            if (addButton.getVisibility() != View.VISIBLE) return;
            T entity = adapter.getItem(position);
            Bundle b = detailFragment.getArguments();
            try {
                b.putSerializable(ItemDetailFragment.EXTRA_ENTITY, new AndroidJsonFactory().toPrettyString(entity));
            } catch (IOException e) {
                e.printStackTrace();
            }
            detailFragment.clear();
            detailFragment.show(fragmentManager, "tag");
        });
    }

    @Override
    public void edit() {
        addButton.setVisibility(View.VISIBLE);
        adapter.setDeleteEnabled(true);
    }

    @Override
    public void discard() {
        addButton.setVisibility(View.GONE);
        adapter.setDeleteEnabled(false);
        if (original != null) {
            setData(original);
        }
    }

    @Override
    public void accept() {
        addButton.setVisibility(View.GONE);
        adapter.setDeleteEnabled(false);
    }

    @Override
    public List<T> getData() {
        return adapter.getItems();
    }

    @Override
    public void setData(List<T> data) {
        if (data == null) return;
        original = data;
        clz = data.getClass();
        detailFragment.getArguments().putSerializable(IngredientDetailFragment.EXTRA_CLASS, clz);
        adapter.replaceData(data);
    }

}
