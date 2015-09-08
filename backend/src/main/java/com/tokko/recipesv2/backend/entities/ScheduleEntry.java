package com.tokko.recipesv2.backend.entities;

import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnore;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

import java.util.ArrayList;
import java.util.List;

import static com.tokko.recipesv2.backend.util.IterableUtil.deRef;
import static com.tokko.recipesv2.backend.util.IterableUtil.ref;

@Entity
public class ScheduleEntry extends BaseEntity<Long> implements Comparable<ScheduleEntry>{
    @Id
    public Long id;
    @Index
    public long date;
    @Load
    @JsonIgnore
    private List<Ref<Recipe>> storedRecipes;
    @Ignore
    private List<Recipe> recipes;
    @Parent
    @JsonIgnore
    private Ref<RecipeUser> user;

    public ScheduleEntry(long date, List<Recipe> recipes) {
        this.date = date;
        id = date;
        setRecipes(recipes);
    }

    public ScheduleEntry() {
        recipes = new ArrayList<>();
    }

    public long getDate() {
        return date;
    }

    public ScheduleEntry setDate(long date) {
        this.date = date;
        id = date;
        return this;
    }

    public void addRecipe(Recipe r){
        recipes.add(r);
    }
    public List<Recipe> getRecipes() {
        return recipes;
    }

    private void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public Ref<RecipeUser> getUser() {
        return user;
    }

    public ScheduleEntry setUser(RecipeUser user){
        this.user = Ref.create(user);
        return this;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void load() {
        recipes = deRef(storedRecipes);
        storedRecipes = null;
    }

    @Override
    public void prepare() {
        storedRecipes = ref(recipes);
        recipes = null;
    }

    @Override
    public int compareTo(ScheduleEntry o) {
        return (int) (date - o.getDate());
    }
}
