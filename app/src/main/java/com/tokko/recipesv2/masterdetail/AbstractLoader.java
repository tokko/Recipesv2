package com.tokko.recipesv2.masterdetail;

import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.google.inject.Inject;
import com.tokko.recipesv2.ApiFactory;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;

import java.util.List;

import roboguice.RoboGuice;


public abstract class AbstractLoader<T> extends AsyncTaskLoader<List<T>> {

    private final IntentFilter intentFilter;
    protected List<T> data;
    private BroadcastReceiver onChangeReceiver;
    private Context context;
    private Class<T> clz;
    @Inject
    protected RecipeApi api;

    public AbstractLoader(Context context, Class<T> clz) {
        super(context);
        this.context = context;
        this.clz = clz;
        onContentChanged();
        intentFilter = new IntentFilter("com.google.android.c2dm.intent.RECEIVE");
        onChangeReceiver = new OnChangeReceiver();
        RoboGuice.getInjector(context).injectMembers(this);
    }

    @Override
    public void deliverResult(List<T> result) {
        if (isReset()) {
            releaseResources(result);
            return;
        }

        List<T> oldResult = data;
        data = result;

        if (isStarted()) {
            super.deliverResult(result);
        }

        if (oldResult != result && oldResult != null) {
            releaseResources(oldResult);
        }
    }

    @Override
    public void onCanceled(List<T> result) {
        super.onCanceled(result);
        releaseResources(result);
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();
        if (onChangeReceiver != null)
            context.unregisterReceiver(onChangeReceiver);

        releaseResources(data);
        data = null;
    }

    @Override
    protected void onStartLoading() {
        if (data != null) {
            deliverResult(data);
        }
        if (takeContentChanged() || data == null) {
            forceLoad();
        }
        if (onChangeReceiver != null)
            context.registerReceiver(onChangeReceiver, intentFilter);

    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    protected void releaseResources(List<T> result) {
    }

    protected void onNewData(T t) {
        forceLoad();
    }

    private class OnChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            onNewData(null);
        }
    }
}
