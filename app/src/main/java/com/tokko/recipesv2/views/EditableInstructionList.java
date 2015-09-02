package com.tokko.recipesv2.views;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.recipes.InstructionsDetailFragment;

import java.io.IOException;

public class EditableInstructionList extends EditableListView<String> implements InstructionsDetailFragment.Callbacks {
    public EditableInstructionList(Context context, AttributeSet attrs) {
        super(context, attrs);
        Bundle b = new Bundle();
        b.putSerializable(ItemDetailFragment.EXTRA_CLASS, String.class);
        try {
            b.putString(ItemDetailFragment.EXTRA_ENTITY, new AndroidJsonFactory().toPrettyString(""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        detailFragment.setArguments(b);
    }

    @Override
    protected void prepare(ItemDetailFragment<String> detailFragment) {
        ((InstructionsDetailFragment) detailFragment).setCallbacks(this);
    }

    @Override
    public void instructionAdded(String instruction) {
        adapter.addItem(instruction);
    }

    @Override
    public void instructionDeleted() {

    }
}
