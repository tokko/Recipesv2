package com.tokko.recipesv2.groceries;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.inject.Inject;
import com.tokko.recipesv2.R;
import com.tokko.recipesv2.backend.entities.groceryApi.GroceryApi;
import com.tokko.recipesv2.backend.entities.groceryApi.model.Grocery;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.views.EditTextViewSwitchable;

import java.io.IOException;

import roboguice.inject.InjectView;

public class GroceryDetailFragment extends ItemDetailFragment<Grocery> {

    @InjectView(R.id.grocery_title)
    private EditTextViewSwitchable titleTextView;
    @Inject
    private GroceryApi api;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup parent = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        if (parent == null)
            throw new IllegalStateException("Parent must have a view");
        View v = inflater.inflate(R.layout.grocerydetailfragment, null);
        ViewGroup C = (ViewGroup) parent.findViewById(R.id.content);
        C.addView(v);
        titleTextView = (EditTextViewSwitchable) v.findViewById(R.id.grocery_title);
        return parent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void bindView(Grocery entity) {
        titleTextView.setData(entity.getTitle());
    }

    @Override
    protected Grocery getEntity() {
        entity.setTitle(titleTextView.getText().toString());
        return entity;
    }

    @Override
    protected void onOk() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    api.insert(entity).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void onDelete() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    api.remove(entity.getId()).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
