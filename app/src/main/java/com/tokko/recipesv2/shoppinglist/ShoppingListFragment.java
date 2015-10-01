package com.tokko.recipesv2.shoppinglist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.inject.Inject;
import com.tokko.recipesv2.R;
import com.tokko.recipesv2.backend.entities.recipeApi.RecipeApi;
import com.tokko.recipesv2.backend.entities.recipeApi.model.Ingredient;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ShoppingList;
import com.tokko.recipesv2.backend.entities.recipeApi.model.ShoppingListItem;
import com.tokko.recipesv2.masterdetail.ItemDetailFragment;
import com.tokko.recipesv2.recipes.IngredientDetailFragment;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import roboguice.RoboGuice;
import roboguice.fragment.provided.RoboListFragment;
import roboguice.inject.InjectView;

public class ShoppingListFragment extends RoboListFragment implements ShoppingListDownloader.ShoppingListDownloadedCallbacks, IngredientDetailFragment.IngredientDetailFragmentCallbacks {
    @Inject
    private RecipeApi api;

    @Inject
    private ShoppingListAdapter adapter;
    private ShoppingList list;
    private Integer editing;
    @InjectView(R.id.buttonbar)
    private LinearLayout buttonBar;
    @InjectView(R.id.shoppingListAddbutton)
    private Button addButton;
    private boolean generated;
    private boolean checklist;

    public static ShoppingListFragment newInstance(boolean generated) {
        ShoppingListFragment f = new ShoppingListFragment();
        Bundle b = new Bundle();
        b.putBoolean("generated", generated);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            generated = getArguments().getBoolean("generated");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shoppinglistfragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, getActivity());
        adapter.setDeleteEnabled(true);
        setListAdapter(adapter);
        buttonBar.setVisibility(View.VISIBLE);
        view.findViewById(R.id.buttonbar_delete).setVisibility(View.GONE);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if(checklist){
            list.getItems().get(position).setPurchased(l.getCheckedItemPositions().get(position));
        }
        else {
            IngredientDetailFragment ingredientDetailFragment = RoboGuice.getInjector(getActivity()).getInstance(IngredientDetailFragment.class);
            try {
                ShoppingListItem item = adapter.getItem(position);
                Bundle b = new Bundle();
                b.putSerializable(ItemDetailFragment.EXTRA_CLASS, item.getIngredient().getClass());
                editing = position;
                b.putString(ItemDetailFragment.EXTRA_ENTITY, new AndroidJsonFactory().toPrettyString(item.getIngredient()));
                ingredientDetailFragment.setIngredientDetailFragmentCallbacks(this);
                ingredientDetailFragment.setArguments(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ingredientDetailFragment.show(getFragmentManager(), "tag");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ShoppingListDownloader listDownloader = RoboGuice.getInjector(getActivity()).getInstance(ShoppingListDownloader.class);
        listDownloader.setCallbacks(this);
        listDownloader.execute(generated);
    }

    @OnClick(R.id.shoppingListAddbutton)
    public void onAdd(){
        IngredientDetailFragment ingredientDetailFragment = RoboGuice.getInjector(getActivity()).getInstance(IngredientDetailFragment.class);
        Bundle b = new Bundle();
        b.putSerializable(ItemDetailFragment.EXTRA_CLASS, Ingredient.class);
        ingredientDetailFragment.setArguments(b);
        ingredientDetailFragment.setIngredientDetailFragmentCallbacks(this);
        ingredientDetailFragment.setDeletable(false);
        ingredientDetailFragment.show(getFragmentManager(), "tag");
    }

    @OnClick(R.id.buttonbar_cancel)
    public void onCancel() {
        getActivity().finish();
    }

    @OnClick(R.id.buttonbar_ok)
    public void onOk() {
        List<ShoppingListItem> items = adapter.getItems();
        list.setItems(items);
        AsyncTask.execute(() -> {
            try {
                api.commitShoppingList(list).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        checklist = true;
        adapter.setChecklist(checklist);
        setListAdapter(adapter);
        ListView listView = getListView();
        for (int i = 0; i < adapter.getCount(); i++) {
            ShoppingListItem item = adapter.getItem(i);
            listView.setItemChecked(i, item.getPurchased());
        }
        buttonBar.setVisibility(View.GONE);
        addButton.setVisibility(View.GONE);
    }

    @Override
    public void onShoppingListDownloaded(ShoppingList list) {
        this.list = list;
        adapter.replaceData(list.getItems());
    }

    @Override
    public void ingredientAdded(Ingredient ingredient) {
        if (editing != null) {
            adapter.getItem(editing).setIngredient(ingredient);
            adapter.notifyChange();
            editing = null;
            return;
        }
        ShoppingListItem sli = new ShoppingListItem();
        sli.setIngredient(ingredient);
        sli.setGenerated(false);
        adapter.addItem(sli);
    }

    @Override
    public void ingredientDeleted(Ingredient entity) {

    }
}
