package com.tokko.recipesv2.groceries;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokko.recipesv2.R;
import com.tokko.recipesv2.backend.entities.groceryApi.model.Grocery;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;

import roboguice.inject.InjectView;

public class GroceryDetailFragment extends ItemDetailFragment<Grocery> {

    @InjectView(R.id.grocery_title)
    private TextView titleTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup parent = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        if (parent == null)
            throw new IllegalStateException("Parent must have a view");
        View v = inflater.inflate(R.layout.grocerydetailfragment, null);
        ViewGroup C = (ViewGroup) parent.findViewById(R.id.content);
        //int index = parent.indexOfChild(C);
        //parent.removeView(C);
        C.addView(v);
        titleTextView = (TextView) v.findViewById(R.id.grocery_title);
        return parent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void bindView(Grocery entity) {
        titleTextView.setText(entity.getTitle());
    }

    @Override
    protected Grocery getEntity() {
        return null;
    }
}
