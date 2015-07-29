package com.tokko.recipesv2.backend.engines;

import com.tokko.recipesv2.backend.entities.RecipeUser;

public class RecipeUserCrudEngine {
    public void startTransaction() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public RecipeUser getUserByEmail(String email) {
        throw new UnsupportedOperationException("Not implemented");
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

    public void commitTransaction() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
