package com.tokko.recipesv2.backend.entities;

import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnore;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

import java.util.List;

import static com.tokko.recipesv2.backend.util.IterableUtil.deRef;
import static com.tokko.recipesv2.backend.util.IterableUtil.ref;

@Entity
public class ScheduleEntry extends BaseEntity<Long> implements Comparable<ScheduleEntry>{
    @Id
    private long date;
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
        setRecipes(recipes);
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public List<Ref<Recipe>> getStoredRecipes() {
        return storedRecipes;
    }

    public void setStoredRecipes(List<Ref<Recipe>> storedRecipes) {
        this.storedRecipes = storedRecipes;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public Ref<RecipeUser> getUser() {
        return user;
    }

    private void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void setUser(RecipeUser user){
        this.user = Ref.create(user);
    }

    @Override
    public Long getId() {
        return date;
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
