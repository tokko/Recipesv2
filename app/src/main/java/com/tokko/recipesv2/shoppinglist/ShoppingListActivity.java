package com.tokko.recipesv2.shoppinglist;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tokko.recipesv2.R;

import roboguice.activity.RoboActivity;

public class ShoppingListActivity extends RoboActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShoppingListFragment fragment;
        if (getIntent().getExtras() != null)
            fragment = ShoppingListFragment.newInstance(true);
        else
            fragment = ShoppingListFragment.newInstance(false);
        getFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shopping_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}