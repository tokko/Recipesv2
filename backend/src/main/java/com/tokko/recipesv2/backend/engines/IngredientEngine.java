package com.tokko.recipesv2.backend.engines;

import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientEngine {

    public List<Grocery> getGroceries(List<Ingredient> ingredients) {
        List<Grocery> ret = new ArrayList<>();
        for (Ingredient i : ingredients) {
            ret.add(i.getGrocery());
        }
        return ret;
    }
}
