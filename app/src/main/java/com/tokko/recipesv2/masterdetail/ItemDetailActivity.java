package com.tokko.recipesv2.masterdetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.inject.Key;
import com.google.inject.util.Types;
import com.tokko.recipesv2.R;

import roboguice.RoboGuice;
import roboguice.activity.RoboFragmentActivity;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link ItemDetailFragment}.
 */
public class ItemDetailActivity extends RoboFragmentActivity implements ItemDetailFragment.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);
        Class<?> entityClass = (Class<?>) getIntent().getSerializableExtra(ItemDetailFragment.EXTRA_CLASS);
        ItemDetailFragment fragment = (ItemDetailFragment) RoboGuice.getInjector(this).getInstance(Key.<ItemDetailFragment>get(Types.newParameterizedType(ItemDetailFragment.class, entityClass)));
        fragment.setArguments(getIntent().getExtras());
        getFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void detailFinished() {
        finish();
    }
}
