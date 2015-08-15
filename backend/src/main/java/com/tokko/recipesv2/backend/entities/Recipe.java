package com.tokko.recipesv2.backend.entities;

import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnore;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Recipe {
    @Id
    public Long id;
    public String title;
    @Ignore
    public List<Ingredient> ingredients = new ArrayList<>();
    @JsonIgnore
    @Parent
    private Ref<RecipeUser> recipeUser;
    @Load
    @JsonIgnore
    private List<Ref<Ingredient>> stiredIngredients;

    public Recipe() {
    }

    public Recipe(String title, RecipeUser recipeUser) {
        this.title = title;
        setRecipeUser(recipeUser);
    }

    public Recipe(String title) {
        setTitle(title);
    }

    public void load() {

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
