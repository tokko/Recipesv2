package com.tokko.recipesv2.views;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

public class EditableIntegerTextViewSwitchable extends EditTextViewSwitchable {
    public EditableIntegerTextViewSwitchable(Context context, AttributeSet attrs) {
        super(context, attrs);
        edit.setInputType(InputType.TYPE_CLASS_NUMBER);
    }
}
