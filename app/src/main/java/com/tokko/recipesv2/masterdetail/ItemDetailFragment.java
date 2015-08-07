package com.tokko.recipesv2.masterdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.tokko.recipesv2.R;
import com.tokko.recipesv2.views.Editable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import roboguice.fragment.provided.RoboFragment;
import roboguice.inject.InjectView;

public abstract class ItemDetailFragment<T> extends RoboFragment {
    public static final String EXTRA_CLASS = "class";
    public static final String EXTRA_ENTITY = "entity";
    protected T entity;
    private Class<T> clz;
    @InjectView(R.id.buttonbar)
    private ViewGroup buttonBar;

    @InjectView(R.id.buttonbar_delete)
    private Button deleteButton;

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
        setDeleteButtonEnabledState();
        bindView(entity);
        enterEditMode();
    }

    private void setDeleteButtonEnabledState() {
        try {
            Object getId = entity.getClass().getMethod("getId").invoke(entity);
            deleteButton.setEnabled(getId != null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
        }
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
        traverseView(Editable::edit);
        showButtonBar();
    }

    private void leaveEditMode(Action action) {
        traverseView(action);
        hideButtonBar();
    }

    protected abstract void onOk();

    protected abstract void onDelete();

    @OnClick(R.id.buttonbar_cancel)
    public void onCancelButtonClick(View v) {
        leaveEditMode(Editable::discard);
    }

    @OnClick(R.id.buttonbar_ok)
    public void onOkButtonClick(View v) {
        leaveEditMode(Editable::accept);
        entity = getEntity();
        onOk();
    }

    @OnClick(R.id.buttonbar_delete)
    public void onDeleteButtonClick(View v) {
        leaveEditMode(Editable::discard);
        onDelete();
    }

    private void showButtonBar() {
        buttonBar.setVisibility(View.VISIBLE);
    }

    private void hideButtonBar() {
        buttonBar.setVisibility(View.GONE);
    }
    private void traverseView(Action action) {
        traverseView((ViewGroup) getView(), action);
    }

    private void traverseView(ViewGroup root, Action action) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof Editable)
                action.action((Editable) v);
            else if (v instanceof ViewGroup)
                traverseView((ViewGroup) v, action);

        }
    }

    private interface Action {
        void action(Editable editable);
    }
}
