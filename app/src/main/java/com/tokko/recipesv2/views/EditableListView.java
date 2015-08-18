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
import com.tokko.recipesv2.backend.entities.recipeApi.model.Ingredient;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;

import java.io.IOException;
import java.util.List;

import roboguice.RoboGuice;

public class EditableListView<T> extends LinearLayout implements Editable<List<T>> {
    private final Button addButton;
    private String original;

    @Inject
    private StringifyableAdapter<T> adapter;

    @Inject
    private LayoutInflater inflater;

    @Inject
    private ItemDetailFragment<T> detailFragment;

    @Inject
    private FragmentManager fragmentManager;

    public EditableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        RoboGuice.getInjector(context).injectMembers(this);

        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.editablelistview, this, true);
        ListView lv = (ListView) v.findViewById(R.id.editable_list);
        lv.setAdapter(adapter);
        addButton = (Button) v.findViewById(R.id.editableList_addButton);
        Bundle b = new Bundle();
        b.putSerializable(ItemDetailFragment.EXTRA_CLASS, Ingredient.class);
        try {
            b.putString(ItemDetailFragment.EXTRA_ENTITY, new AndroidJsonFactory().toPrettyString(new Ingredient()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        detailFragment.setArguments(b);
        addButton.setOnClickListener((view) -> detailFragment.show(fragmentManager, "tag"));
    }

    @Override
    public void edit() {
        addButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void discard() {
        addButton.setVisibility(View.GONE);
    }

    @Override
    public void accept() {
        addButton.setVisibility(View.GONE);
    }

    @Override
    public List<T> getData() {
        return adapter.getItems();
    }

    @Override
    public void setData(List<T> data) {
        adapter.replaceData(data);
    }
}
