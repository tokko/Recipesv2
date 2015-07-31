package com.tokko.recipesv2.backend.engines;

import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import java.util.List;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;

public class GroceryCrudEngine {
    public List<Grocery> listGroceries(RecipeUser user) {
        return ofy().load().type(Grocery.class).ancestor(user).list();
    }
}
