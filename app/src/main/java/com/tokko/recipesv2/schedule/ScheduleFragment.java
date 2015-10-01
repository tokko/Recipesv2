package com.tokko.recipesv2.schedule;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.inject.Inject;
import com.tokko.recipesv2.R;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.CommitScheduleContainer;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Recipe;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ScheduleEntry;
import com.tokko.recipesv2.shoppinglist.ShoppingListActivity;

import org.joda.time.DateTime;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import roboguice.RoboGuice;
import roboguice.fragment.provided.RoboListFragment;
import roboguice.inject.InjectView;


public class ScheduleFragment extends RoboListFragment implements LoaderManager.LoaderCallbacks<List<ScheduleEntry>>, View.OnClickListener {

    @Inject
    private ScheduleLoader scheduleLoader;

    private ExpandableAdapter adapter;
    @InjectView(android.R.id.list)
    private ExpandableListView elv;
    @Inject
    private RecipeApi api;

    @InjectView(R.id.scheduleFragmentOk)
    private Button okButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ExpandableAdapter();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.schedulefragment, null);
    }

    @Override
    public void onClick(View v) {
    }

    @OnClick(R.id.scheduleFragmentOk)
    public void onOk(){
        AsyncTask.execute(() -> {
            try {
                api.generateShoppingList(new DateTime().withTime(0, 0, 0, 0).getMillis());
                getActivity().runOnUiThread(() -> startActivity(new Intent(getActivity(), ShoppingListActivity.class).putExtra("generated", true)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @OnClick(R.id.scheduleFragmentShop)
    public void OnShopClick(){
        startActivity(new Intent(getActivity(), ShoppingListActivity.class));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ExpandableListView) getListView()).setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        ButterKnife.inject(this, getActivity());
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getLoaderManager().destroyLoader(0);
    }

    @Override
    public Loader<List<ScheduleEntry>> onCreateLoader(int id, Bundle args) {
            return scheduleLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<ScheduleEntry>> loader, List<ScheduleEntry> data) {
        adapter.clear();
        adapter.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<ScheduleEntry>> loader) {
        adapter.clear();
        adapter.notifyDataSetInvalidated();
    }

    public class ExpandableAdapter extends BaseExpandableListAdapter{
        private ArrayList<ScheduleEntry> data = new ArrayList<>();
        @Inject
        private LayoutInflater inflater;

        public ExpandableAdapter() {
            RoboGuice.getInjector(getActivity()).injectMembers(this);
        }

        public void clear(){
            data.clear();
        }

        public void addAll(Collection<ScheduleEntry> data){
            if (data != null)
                this.data.addAll(data);
        }
        @Override
        public int getGroupCount() {
            return data.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return data.get(groupPosition).getRecipes() != null ? data.get(groupPosition).getRecipes().size() : 0;
        }

        @Override
        public ScheduleEntry getGroup(int groupPosition) {
            return data.get(groupPosition);
        }

        @Override
        public Recipe getChild(int groupPosition, int childPosition) {
            return getGroup(groupPosition).getRecipes().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            ScheduleEntry group = getGroup(groupPosition);
            return group != null ? group.getId() : -1;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return getChild(groupPosition, childPosition).getId();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(android.R.layout.simple_expandable_list_item_1, null);
            }
            ((TextView)convertView.findViewById(android.R.id.text1)).setText(new SimpleDateFormat("yyyy-MM-dd").format(getGroup(groupPosition).getDate()));
            convertView.setTag(getGroup(groupPosition));
            convertView.setOnDragListener(new MyDragListener());
            elv.expandGroup(groupPosition);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = inflater.inflate(R.layout.adapterentry, null);
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(getChild(groupPosition, childPosition).getTitle());
            View deleteButton = convertView.findViewById(R.id.deleteImageButton);
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener((v) -> {
                adapter.getGroup(groupPosition).getRecipes().remove(childPosition);
                adapter.notifyDataSetChanged();
            });

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        class MyDragListener implements View.OnDragListener {

            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DROP:
                        try {
                            Recipe r = new AndroidJsonFactory().fromString(event.getClipData().getDescription().getLabel().toString(), Recipe.class);
                            ScheduleEntry tag = (ScheduleEntry) v.getTag();
                            if (tag.getRecipes() == null) tag.setRecipes(new ArrayList<>());
                            tag.getRecipes().add(r);
                            adapter.notifyDataSetChanged();
                            AsyncTask.execute(() -> {
                                try {
                                    CommitScheduleContainer csc = new CommitScheduleContainer();
                                    csc.setEntries(adapter.data);
                                    api.commitSchedule(csc).execute();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                    default:
                        break;
                }
                return true;
            }
        }

    }
}
