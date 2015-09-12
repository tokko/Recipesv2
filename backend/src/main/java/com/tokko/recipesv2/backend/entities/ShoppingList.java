package com.tokko.recipesv2.backend.entities;

import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnore;
import com.google.appengine.repackaged.org.joda.time.DateTime;
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

    public DateTime getDate() {
        return new DateTime(date);
    }

    public void setDate(DateTime date) {
        this.date = date.withTime(0, 0, 0, 0).getMillis();
    }

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
    }

    @Override
    public void prepare() {
    }

    public List<ShoppingListItem> getItems() {
        return items;
    }
}
