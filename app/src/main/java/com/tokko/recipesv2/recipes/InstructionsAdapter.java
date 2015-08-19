package com.tokko.recipesv2.recipes;

import android.content.Context;

import com.google.inject.Inject;
import com.tokko.recipesv2.masterdetail.StringifyableAdapter;

public class InstructionsAdapter extends StringifyableAdapter<String> {
    @Inject
    public InstructionsAdapter(Context context) {
        super(context);
    }

    @Override
    protected String getItemString(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
