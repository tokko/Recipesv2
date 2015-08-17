package com.tokko.recipesv2.backend.entities;

public abstract class BaseEntity<T> {

    public abstract T getId();

    @Override
    public boolean equals(Object obj) {
        try {
            return ((Ingredient) obj).getId().equals(getId());
        } catch (ClassCastException ignored) {
            return false;
        }
    }
}
