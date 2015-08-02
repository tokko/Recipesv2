package com.tokko.recipesv2.masterdetail;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.inject.Key;
import com.google.inject.util.Types;
import com.tokko.recipesv2.R;

import java.lang.reflect.ParameterizedType;

import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;

public class ItemListActivity extends RoboActivity
        implements ItemListFragment.Callbacks {

    public static final String EXTRA_ENTITY_CLASS = "class";
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
        arguments.putString(ItemDetailFragment.EXTRA_ENTITY, new Gson().toJson(entity));
        /*
        Class<?> groceryClass =  entity.getClass();
        ParameterizedType type = Types.newParameterizedType(ItemDetailFragment.class, groceryClass);
        Key<?> key = Key.get(type);
        ItemDetailFragment fragment = RoboGuice.getInjector(this).getInstance(key);
        */
        ItemDetailFragment fragment = null;
        if (mTwoPane) {
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, ItemDetailActivity.class);
            detailIntent.putExtras(arguments);
            startActivity(detailIntent);
        }
    }
}