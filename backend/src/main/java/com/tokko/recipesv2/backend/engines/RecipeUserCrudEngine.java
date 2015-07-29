package com.tokko.recipesv2.backend.engines;

import com.tokko.recipesv2.backend.entities.RecipeUser;

import static com.tokko.recipesv2.backend.resourceaccess.OfyService.ofy;

public class RecipeUserCrudEngine {

    public RecipeUser getUserByEmail(String email) {
        return ofy().load().type(RecipeUser.class).id(email).now();
    }

    public RecipeUser createUser(String email) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void addRegistrationIdToUser(RecipeUser user, String registrationId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void persistUser(RecipeUser user) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
