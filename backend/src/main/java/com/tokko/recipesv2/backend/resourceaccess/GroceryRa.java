package com.tokko.recipesv2.backend.resourceaccess;

import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.RecipeUser;

import java.util.List;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;

public class GroceryRa {
    public List<Grocery> listGroceries(RecipeUser user) {
        return ofy().load().type(Grocery.class).ancestor(user).list();
    }

    public Grocery save(Grocery grocery, RecipeUser user) {
        grocery.setUser(user);
        ofy().save().entity(grocery).now();
        return grocery;
    }

    public Grocery getGrocery(Long id, RecipeUser user) {
        return ofy().load().type(Grocery.class).parent(user).id(id).now();
    }
}
