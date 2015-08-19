package com.tokko.recipesv2.backend.units;

import java.util.Arrays;
import java.util.List;

public abstract class Unit {

    public static List<Unit> getUnits() {
        return Arrays.asList(new Grams(), new HectoGram());
    }

    public abstract Number convertToBase(Number quantity);

    public abstract Number convertToThis(Number quantity);

    public abstract Unit upscale();

    public abstract String getSuffix();

    @Override
    public String toString() {
        return getSuffix();
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return obj != null && getSuffix().equals(((Unit) obj).getSuffix());
        } catch (ClassCastException ignored) {
            return false;
        }
    }
}