package com.tokko.recipesv2.masterdetail;

import android.content.Intent;
import android.os.Bundle;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.inject.Key;
import com.google.inject.util.Types;
import com.tokko.recipesv2.R;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;

public class ItemListActivity extends RoboActivity
        implements ItemListFragment.Callbacks, ItemDetailFragment.Callbacks {

    public static final String EXTRA_ENTITY_CLASS = "class";
    public static final String EXTRA_TITLE = "title";
    ItemListFragment<?> listFragment;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        Intent i = getIntent();
        Class<?> clz;
        if (i.getExtras().containsKey(EXTRA_ENTITY_CLASS))
            clz = (Class<?>) i.getSerializableExtra(EXTRA_ENTITY_CLASS);
        else
            throw new IllegalArgumentException("Activity extras must contain Class<Entity> with key 'class'");
        if (i.getExtras().containsKey(EXTRA_TITLE))
            setTitle(i.getStringExtra(EXTRA_TITLE));
        else setTitle("Groceries");
        ParameterizedType parameterizedType = Types.newParameterizedType(ItemListFragment.class, clz);
        Key<?> key = Key.get(parameterizedType);
        listFragment = (ItemListFragment<?>) RoboGuice.getInjector(getApplicationContext()).getInstance(key);
        Bundle b = new Bundle();
        b.putSerializable(ItemListFragment.EXTRA_CLASS, clz);
        listFragment.setArguments(b);
//        listFragment.setActivateOnItemClick(true);
        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }
        getFragmentManager().beginTransaction().replace(R.id.item_list, listFragment).commit();
    }

    @Override
    public void onItemSelected(Object entity) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(ItemDetailFragment.EXTRA_CLASS, entity.getClass());
        String json = null;
        try {
            json = new AndroidJsonFactory().toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        arguments.putString(ItemDetailFragment.EXTRA_ENTITY, json);
        Class<?> entityClass = entity.getClass();
        ItemDetailFragment fragment = (ItemDetailFragment) RoboGuice.getInjector(this).getInstance(Key.<ItemDetailFragment>get(Types.newParameterizedType(ItemDetailFragment.class, entityClass)));

        if (mTwoPane) {
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .addToBackStack("name")
                    .replace(R.id.item_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, ItemDetailActivity.class);
            detailIntent.putExtras(arguments);
            startActivity(detailIntent);
        }
    }

    @Override
    public void detailFinished() {
        getFragmentManager().popBackStack();
    }
}
