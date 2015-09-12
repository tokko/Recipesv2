package com.tokko.recipesv2.backend.entities;

import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnore;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Grocery extends BaseEntity<Long> {

    @Id
    private Long id;
    private String title;

    @Parent
    @JsonIgnore
    private Ref<RecipeUser> user;

    public Grocery() {
    }

    public Grocery(Long id, String title, RecipeUser user) {
        this.id = id;
        this.title = title;
        setUser(user);
    }

    public Grocery(String title, RecipeUser user) {
        this(null, title, user);
    }

    public Grocery(String title) {
        this(null, title, null);
    }

    public Grocery(long id, String title) {
        this.id = id;
        this.title = title;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public RecipeUser getUser() {
        return user.get();
    }

    public void setUser(RecipeUser user) {
        if (user != null)
        this.user = Ref.create(user);
    }
}
