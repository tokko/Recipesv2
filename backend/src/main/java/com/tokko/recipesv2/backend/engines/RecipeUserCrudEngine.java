package com.tokko.recipesv2.backend.engines;

import com.tokko.recipesv2.backend.entities.RecipeUser;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;

public class RecipeUserCrudEngine {

    public RecipeUser getUserByEmail(String email) {
        return ofy().load().type(RecipeUser.class).id(email).now();
    }

    public RecipeUser createUser(String email) {
        RecipeUser user = new RecipeUser();
        user.setEmail(email);
        return user;
    }

    public void persistUser(RecipeUser user) {
        ofy().save().entity(user).now();
    }
}