package com.tokko.recipesv2.backend.resourceaccess;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.LoadResult;
import com.tokko.recipesv2.backend.entities.RecipeUser;
import com.tokko.recipesv2.backend.entities.ShoppingList;

import java.util.List;

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


    public ShoppingList getLatestShoppingList(RecipeUser user) {
        List<ShoppingList> list = ofy().load().type(ShoppingList.class).ancestor(user).order("-date").limit(1).list();
        if (list.size() > 0)
            return list.get(0);
        return null;
    }
}
