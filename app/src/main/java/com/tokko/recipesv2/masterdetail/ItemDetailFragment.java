package com.tokko.recipesv2.masterdetail;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.tokko.recipesv2.R;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class ItemDetailFragment<T> extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    public static final String EXTRA_CLASS = "class";
    public static final String EXTRA_ENTITY = "entity";
    private Class<T> clz;
    private T entity;

    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        clz = (Class<T>) getArguments().getSerializable(EXTRA_CLASS);
        String json = getArguments().getString(EXTRA_ENTITY);
        try {
            entity = new AndroidJsonFactory().fromString(json, clz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int i = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        bindView(entity);
    }

    protected abstract void bindView(T entity);

    protected abstract T getEntity();

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.itemdetail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                enterEditMode();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void enterEditMode() {

    }

    @OnClick(R.id.buttonbar_cancel)
    public void onCancelButtonClick(View v) {

    }

    @OnClick(R.id.buttonbar_ok)
    public void onOkButtonClick(View v) {

    }

    @OnClick(R.id.buttonbar_cancel)
    public void onDeleteButtonClick(View v) {

    }
}
