package com.tokko.recipesv2.backend.entities;

public class ShoppingListItem extends BaseEntity<Void> {
    public Ingredient ingredient;
    public boolean purchased;
    public boolean generated = true;

    public ShoppingListItem() {
    }

    public ShoppingListItem(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

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

    public void addIngredientQuantity(ShoppingListItem other) {
        if (ingredient == null)
            ingredient = other.ingredient;
        else
            ingredient.getQuantity().add(other.ingredient.getQuantity());
        generated = other.generated;
    }
}
