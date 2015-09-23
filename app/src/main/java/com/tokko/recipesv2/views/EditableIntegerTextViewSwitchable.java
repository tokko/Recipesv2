package com.tokko.recipesv2.views;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditableIntegerTextViewSwitchable extends EditableViewSwitchable<TextView, EditText, Integer> {
    public EditableIntegerTextViewSwitchable(Context context, AttributeSet attrs) {
        super(context, attrs, new TextView(context, attrs), new EditText(context, attrs));
        edit.setInputType(InputType.TYPE_CLASS_NUMBER);
        edit.setVisibility(View.GONE);
    }

    public EditableIntegerTextViewSwitchable setHint(String hint) {
        edit.setHint(hint);
        return this;
    }

    @Override
    protected void onDiscard() {
        edit.setText(label.getText());
    }

    @Override
    protected void onAccept() {
        label.setText(edit.getText());
    }

    @Override
    public Integer getData() {
        return Integer.valueOf(edit.getText().toString());
    }

    @Override
    public void setData(Integer data) {
        if (data != null) {
            edit.setText(data.toString());
            label.setText(data.toString());
        }
    }

    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        edit.setOnFocusChangeListener(listener);
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener listener) {
        edit.setOnEditorActionListener(listener);
    }
}
