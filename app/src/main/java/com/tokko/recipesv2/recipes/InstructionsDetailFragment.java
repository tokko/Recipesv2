package com.tokko.recipesv2.recipes;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tokko.recipesv2.R;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;

import roboguice.inject.InjectView;

public class InstructionsDetailFragment extends ItemDetailFragment<String> {

    @InjectView(R.id.editableStringListInput)
    private EditText input;
    private Callbacks callbacks;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getDialog() != null)
            getDialog().setTitle("Instructions");
    }

    @Override
    public ItemDetailFragment<String> newInstance(Bundle args) {
        InstructionsDetailFragment f = new InstructionsDetailFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.instructionsdetailfragment;
    }

    @Override
    protected void bindView(String entity) {
        input.setText(entity);
    }

    @Override
    protected String getEntity() {
        return input.getText().toString();
    }

    @Override
    protected void onOk() {
        callbacks.instructionAdded(input.getText().toString());
        dismiss();
    }

    @Override
    protected void onDelete() {
        callbacks.instructionDeleted();
        dismiss();
    }

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public interface Callbacks {
        void instructionAdded(String instruction);

        void instructionDeleted();
    }
}
