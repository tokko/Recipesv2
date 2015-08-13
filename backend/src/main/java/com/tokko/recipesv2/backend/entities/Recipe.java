package com.tokko.recipesv2.backend.entities;

import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnore;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Recipe {
    @Id
    public Long id;
    public String title;

    @JsonIgnore
    @Parent
    private Ref<RecipeUser> recipeUser;

    public Recipe() {
    }

    public Recipe(String title, RecipeUser recipeUser) {
        this.title = title;
        setRecipeUser(recipeUser);
    }

    public Recipe(String title) {
        setTitle(title);
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

    public RecipeUser getRecipeUser() {
        return recipeUser.get();
    }

    public void setRecipeUser(RecipeUser recipeUser) {
        this.recipeUser = Ref.create(recipeUser);
    }
}
