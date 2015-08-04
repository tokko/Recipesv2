package com.tokko.recipesv2.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditTextViewSwitchable extends EditableViewSwitchable<TextView, EditText, String> {

    public EditTextViewSwitchable(Context context, AttributeSet attrs) {
        super(context, attrs, new TextView(context, attrs), new EditText(context, attrs));
        edit.setVisibility(View.GONE);
    }

    public EditTextViewSwitchable setHint(String hint) {
        edit.setHint(hint);
        return this;
    }

    public String getText() {
        return edit.getText().toString();
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
    public String getData() {
        return label.getText().toString();
    }

    @Override
    public void setData(String data) {
        edit.setText(data);
        label.setText(data);
    }
}
