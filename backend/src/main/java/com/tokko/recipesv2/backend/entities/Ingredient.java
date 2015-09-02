package com.tokko.recipesv2.backend.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;
import com.tokko.recipesv2.backend.units.Quantity;

@Entity
public class Ingredient extends BaseEntity<Long> {

    @Id
    public Long id;
    @Ignore
    public Grocery grocery;
    public Quantity quantity;
    @Load
    private Ref<Grocery> storedGrocery;
    @Parent
    private Ref<RecipeUser> user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    @Override
    public void load() {
        grocery = storedGrocery.get();
    }

    @Override
    public void prepare() {
        storedGrocery = Ref.create(grocery);
    }

    public Grocery getGrocery() {
        return grocery;
    }

    public void setGrocery(Grocery grocery) {
        this.grocery = grocery;
    }

    public void setUser(RecipeUser user) {
        this.user = Ref.create(user);
    }
}
