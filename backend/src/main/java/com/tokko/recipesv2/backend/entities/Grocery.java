package com.tokko.recipesv2.backend.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Grocery {

    @Id
    private Long id;
    private String title;
    //   @Parent @Load
    //  private Ref<RecipeUser> user;

    public Grocery() {
    }

    public Grocery(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
/*
    public RecipeUser getUser() {
        return user.get();
    }

    public void setUser(RecipeUser user) {
        this.user = Ref.create(user);
    }
    */
}
