package com.tokko.recipesv2.backend.entities;

public class ShoppingListItem extends BaseEntity<Void> {
    public Ingredient ingredient;
    public boolean purchased;
    public boolean generated = true;

    @Override
    public Void getId() {
        return null;
    }

    @Override
    public void load() {
        ingredient.load();
    }

    @Override
    public void prepare() {
        ingredient.prepare();
    }
}
