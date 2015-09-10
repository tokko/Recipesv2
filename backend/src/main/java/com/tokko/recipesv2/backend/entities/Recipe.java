package com.tokko.recipesv2.backend.entities;

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
public class Recipe {
    @Id
    public Long id;
    public String title;
    @Ignore
    public List<Ingredient> ingredients;
    public List<String> instructions;
    public int helpings = 0;
    @Parent
    private Ref<RecipeUser> recipeUser;
    @Load
    private List<Ref<Ingredient>> storedIngredients;

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
        ingredients = deRef(storedIngredients);
        for (Ingredient i : ingredients) {
            i.load();
        }
        storedIngredients = null;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public int getHelpings() {
        return helpings;
    }

    public void setHelpings(int helpings) {
        this.helpings = helpings;
    }

    public void prepare() {
        storedIngredients = ref(ingredients);
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

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
