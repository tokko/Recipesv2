package com.tokko.recipesv2.masterdetail;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
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
import roboguice.fragment.provided.RoboDialogFragment;
import roboguice.inject.InjectView;

public abstract class ItemDetailFragment<T> extends RoboDialogFragment {
    public static final String EXTRA_CLASS = "class";
    public static final String EXTRA_ENTITY = "entity";
    protected T entity;
    protected Callbacks callbacks;
    @InjectView(R.id.buttonbar_delete)
    protected Button deleteButton;
    private Class<T> clz;
    @InjectView(R.id.buttonbar)
    private ViewGroup buttonBar;
    private boolean deletable = true;
    private IntentFilter intentFilter;
    private OnChangeReceiver onChangeReceiver;

    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            clz = (Class<T>) getArguments().getSerializable(EXTRA_CLASS);
            if (getArguments().containsKey(EXTRA_ENTITY)) {
                String json = getArguments().getString(EXTRA_ENTITY);
                try {
                    entity = new AndroidJsonFactory().fromString(json, clz);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            } else
                try {
                    entity = clz.newInstance();
                } catch (java.lang.InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
        }

        intentFilter = new IntentFilter("com.google.android.c2dm.intent.RECEIVE");
        onChangeReceiver = new OnChangeReceiver();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (onChangeReceiver == null)
            onChangeReceiver = new OnChangeReceiver();
        getActivity().registerReceiver(onChangeReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (onChangeReceiver != null)
            getActivity().unregisterReceiver(onChangeReceiver);

    }

    public void clear() {
        if (clz == null) return;
        try {
            entity = clz.newInstance();
        } catch (java.lang.InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        bindView(entity);
    }

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup parent = (ViewGroup) inflater.inflate(R.layout.fragment_item_detail, container, false);
        if (parent == null)
            throw new IllegalStateException("Parent must have a view");
        View v = inflater.inflate(getLayoutResource(), null);
        ViewGroup C = (ViewGroup) parent.findViewById(R.id.content);
        C.addView(v);
        return parent;
    }

    protected abstract int getLayoutResource();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        setDeleteButtonEnabledState();
        bindView(entity);
        try {
            if (entity != null && entity.getClass().getMethod("getId").invoke(entity) == null) {
                enterEditMode();
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            enterEditMode();
        }
        deleteButton.setVisibility(deletable ? View.VISIBLE : View.GONE);
    }

    public abstract ItemDetailFragment<T> newInstance(Bundle args);

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            setCallbacks((Callbacks) activity);
        } catch (ClassCastException ignored) {
        }
    }

    private void setDeleteButtonEnabledState() {
        if (entity == null) return;
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

    public void enterEditMode() {
        traverseView(Editable::edit);
        showButtonBar();
    }

    private void leaveEditMode(Action action) {

        traverseView(action);
        hideButtonBar();
    }

    protected abstract boolean onOk();

    protected abstract boolean onDelete();

    @OnClick(R.id.buttonbar_cancel)
    public void onCancelButtonClick(View v) {
        if (!onCancel()) return;
        leaveEditMode(Editable::discard);
    }

    public boolean onCancel() {
        //do nothing, let children override
        return true;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    @OnClick(R.id.buttonbar_ok)
    public void onOkButtonClick(View v) {
        if (!onOk()) return;
        leaveEditMode(Editable::accept);
        entity = getEntity();
    }

    @OnClick(R.id.buttonbar_delete)
    public void onDeleteButtonClick(View v) {
        if (!onDelete()) return;
        leaveEditMode(Editable::discard);
        if (callbacks != null)
            callbacks.detailFinished();
    }

    protected void showButtonBar() {
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

    protected abstract EntityGetter<T> getEntityGetter();

    private interface Action {
        void action(Editable editable);
    }

    public interface Callbacks {
        void detailFinished();

        void hideFragment();
    }

    public interface EntityGetter<T> {
        T getEntity(Long id) throws IOException;
    }

    private class OnChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String data = intent.getStringExtra("id");
                Long id = Long.valueOf(data);
                if (id != null) {
                    EntityGetter<T> entityGetter = getEntityGetter();
                    if (entityGetter != null) {
                        AsyncTask.execute(() -> {
                            try {
                                entity = entityGetter.getEntity(id);
                                getActivity().runOnUiThread(() -> bindView(entity));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            } catch (ClassCastException ignore) {
            }
        }
    }
}
