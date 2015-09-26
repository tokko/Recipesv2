package com.tokko.recipesv2.recipes;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tokko.recipesv2.R;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;

import butterknife.OnClick;
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
        deleteButton.setVisibility(View.GONE);
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
    protected boolean onOk() {
        callbacks.instructionAdded(input.getText().toString());
        dismiss();
        return true;
    }

    @Override
    protected boolean onDelete() {
        callbacks.instructionDeleted();
        dismiss();
        return true;
    }

    @Override
    @OnClick(R.id.buttonbar_cancel)
    public void onCancelButtonClick(View v) {
        super.onCancelButtonClick(v);
        dismiss();
    }

    @Override
    protected EntityGetter<String> getEntityGetter() {
        return null;
    }

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public interface Callbacks {
        void instructionAdded(String instruction);

        void instructionDeleted();
    }
}
