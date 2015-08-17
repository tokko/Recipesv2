package com.tokko.recipesv2.backend.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Ingredient extends BaseEntity<Long> {

    @Id
    public Long id;
    @Load
    public Ref<Grocery> grocery;

    @Parent
    private Ref<RecipeUser> user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Grocery getGrocery() {
        return grocery.get();
    }

    public void setGrocery(Grocery grocery) {
        this.grocery = Ref.create(grocery);
    }

    public void setUser(RecipeUser user) {
        this.user = Ref.create(user);
    }
}
