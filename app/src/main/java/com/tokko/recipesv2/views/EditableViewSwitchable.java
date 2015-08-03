package com.tokko.recipesv2.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokko.recipesv2.R;

import java.util.UUID;

public abstract class EditableViewSwitchable<W1 extends View, W2 extends View, D> extends LinearLayout implements Editable<D> {
    protected W1 label;
    protected W2 edit;

    public EditableViewSwitchable(Context context, AttributeSet attrs, W1 w1, W2 w2) {
        super(context, attrs);
        label = w1;
        edit = w2;
        w1.setId(UUID.randomUUID().hashCode());
        w2.setId(UUID.randomUUID().hashCode());
        setOrientation(VERTICAL);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.editable, this, true);
        replaceView(R.id.editable_label, w1);
        replaceView(R.id.editable_edit, w2);
    }
    private void replaceView(int id, View n) {
        View v = findViewById(id);
        ViewGroup parent = (ViewGroup) v.getParent();
        int index = parent.indexOfChild(v);
        parent.removeViewAt(index);
        parent.addView(n, index);
    }

    @Override
    public void edit() {
        label.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);
    }

    protected abstract void onDiscard();

    @Override
    public final void discard() {
        label.setVisibility(View.VISIBLE);
        edit.setVisibility(View.GONE);
        onDiscard();
    }

    @Override
    public final void accept() {
        label.setVisibility(View.VISIBLE);
        edit.setVisibility(View.GONE);
        onAccept();
    }

    protected abstract void onAccept();
}
