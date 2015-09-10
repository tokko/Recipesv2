package com.tokko.recipesv2.backend.engines;

import com.tokko.recipesv2.backend.entities.Ingredient;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ShoppingListAggregatorEngine {

    public List<Ingredient> aggregateIngredients(List<Ingredient> ingredients) {
        HashMap<Long, Ingredient> ings = new HashMap<>();
        for (Ingredient ingredient : ingredients) {
            if (ings.containsKey(ingredient.getGrocery().getId())) {
                ings.get(ingredient.getGrocery().getId()).getQuantity().add(ingredient.getQuantity());
            } else {
                ings.put(ingredient.getGrocery().getId(), ingredient);
            }
        }
        return ings.values().stream().collect(Collectors.<Ingredient>toList());
    }

}
