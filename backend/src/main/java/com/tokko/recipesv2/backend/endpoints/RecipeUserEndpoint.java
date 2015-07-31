package com.tokko.recipesv2.backend.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.tokko.recipesv2.backend.managers.RecipeUserManager;

import javax.inject.Inject;

import static com.tokko.recipesv2.backend.util.GuiceModule.inject;

@Api(
        name = "recipeUserApi",
        version = "v1",
        resource = "recipeUser",
        clientIds = {Constants.ANDROID_CLIENT_ID, Constants.WEB_CLIENT_ID},
        audiences = {Constants.ANDROID_AUDIENCE},
        namespace = @ApiNamespace(
                ownerDomain = "enteties.backend.recipesv2.tokko.com",
                ownerName = "enteties.backend.recipesv2.tokko.com",
                packagePath = ""
        )
)
public class RecipeUserEndpoint {
    @Inject
    RecipeUserManager recipeUserManager;

    public RecipeUserEndpoint() {
        inject(this);
    }

    @ApiMethod(
            name = "insert",
            path = "recipeUser",
            httpMethod = ApiMethod.HttpMethod.GET)
    public void registerDevice(@Named("regid") String regid, User user) throws UnauthorizedException {
        if (user == null) throw new UnauthorizedException("You shall not pass!");
        recipeUserManager.addRegistrationIdToRecipeUser(user.getEmail(), regid);
    }
}