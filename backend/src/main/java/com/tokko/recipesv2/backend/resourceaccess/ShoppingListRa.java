package com.tokko.recipesv2.backend.resourceaccess;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.LoadResult;
import com.tokko.recipesv2.backend.entities.RecipeUser;
import com.tokko.recipesv2.backend.entities.ShoppingList;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;

public class ShoppingListRa {

    public ShoppingList commitShoppingList(ShoppingList sl, RecipeUser user) {
        sl.setUser(user);
        Key<ShoppingList> now = ofy().save().entity(sl).now();
        LoadResult<ShoppingList> key = ofy().load().key(now);
        ShoppingList safe = key.safe();
        return safe;
    }

    public void deleteShoppingList(ShoppingList list) {
        ofy().delete().entity(list).now();
    }
}
