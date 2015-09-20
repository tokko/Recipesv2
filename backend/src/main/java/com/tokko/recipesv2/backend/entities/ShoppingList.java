package com.tokko.recipesv2.backend.entities;

import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnore;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import java.util.ArrayList;
import java.util.List;


@Entity
public class ShoppingList extends BaseEntity<Long> {
    @Id
    public Long id;

    @Index
    public long date;

    @JsonIgnore
    @Parent
    private Ref<RecipeUser> parent;

    private List<ShoppingListItem> items = new ArrayList<>();

    public void setUser(RecipeUser user) {
        parent = Ref.create(user);
    }

    public void setDate(long date) {
        this.date = date;
    }

    /*
        public void setDate(DateTime date) {
            this.date = date.withTime(0, 0, 0, 0).getMillis();
        }
    */
    public void addItem(ShoppingListItem item) {
        items.add(item);
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void load() {
        for (ShoppingListItem item : items) {
            item.load();
        }
    }

    @Override
    public void prepare() {
        for (ShoppingListItem item : items) {
            item.prepare();
        }
    }

    public List<ShoppingListItem> getItems() {
        return items;
    }

    public void setItems(List<ShoppingListItem> items) {
        this.items = items;
    }
}
