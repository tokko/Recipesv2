package com.tokko.recipesv2.backend.resourceaccess;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.LoadResult;
import com.tokko.recipesv2.backend.endpoints.Constants;
import com.tokko.recipesv2.backend.entities.RecipeUser;
import com.tokko.recipesv2.backend.entities.ShoppingList;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;

public class ShoppingListRa {

    public ShoppingList commitShoppingList(ShoppingList sl) {
        sl.prepare();
        Key<ShoppingList> now = ofy().save().entity(sl).now();
        LoadResult<ShoppingList> key = ofy().load().key(now);
        ShoppingList safe = key.safe();
        return safe;
    }

    public void deleteShoppingList(ShoppingList list) {
        ofy().delete().entity(list).now();
    }


    public ShoppingList getLatestShoppingList(RecipeUser user) {
        List<ShoppingList> list = ofy().load().type(ShoppingList.class).ancestor(user).order("-expirationDate").list();
        if (list.size() > 0) {
            Iterator<ShoppingList> it = list.iterator();
            while (it.hasNext()) {
                ShoppingList current = it.next();
                if (current.getId().equals(Constants.GENERAL_LIST_ID)) {
                    it.remove();
                    break;
                }
            }
        }
        if (!list.isEmpty()) {
            ShoppingList shoppingList = list.get(0);
            shoppingList.load();
            return shoppingList;
        }
        return null;
    }

    public ShoppingList getShoppingList(RecipeUser user, Long id) {
        ShoppingList now = ofy().load().type(ShoppingList.class).parent(user).id(id).now();
        now.load();
        return now;
    }
}
