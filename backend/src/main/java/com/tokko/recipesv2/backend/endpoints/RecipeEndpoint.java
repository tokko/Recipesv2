package com.tokko.recipesv2.backend.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;
import com.google.inject.Inject;
import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.managers.RecipeManager;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.tokko.recipesv2.backend.util.GuiceModule.inject;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "recipeApi",
        resource = "recipe",
        version = Constants.API_VERSION,
        scopes = {Constants.EMAIL_SCOPE},
        clientIds = {Constants.ANDROID_CLIENT_ID, Constants.WEB_CLIENT_ID},
        audiences = {Constants.ANDROID_AUDIENCE},
        namespace = @ApiNamespace(
                ownerDomain = "entities.backend.recipesv2.tokko.com",
                ownerName = "entities.backend.recipesv2.tokko.com",
                packagePath = ""
        )
)
public class RecipeEndpoint {

    private static final Logger logger = Logger.getLogger(RecipeEndpoint.class.getName());

    @Inject
    private RecipeManager recipeManager;

    public RecipeEndpoint() {
        inject(this);
    }

    @ApiMethod(
            name = "get",
            path = "recipe/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Recipe get(@Named("id") Long id) throws NotFoundException {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Inserts a new {@code Recipe}.
     */
    @ApiMethod(
            name = "insert",
            path = "recipe",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Recipe insert(Recipe recipe) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @ApiMethod(
            name = "update",
            path = "recipe/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Recipe update(@Named("id") Long id, Recipe recipe) throws NotFoundException {
        throw new UnsupportedOperationException("Not implemented");

    }

    @ApiMethod(
            name = "remove",
            path = "recipe/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @ApiMethod(
            name = "list",
            path = "recipe",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Recipe> list(User user) {
        List<Recipe> recipeList = recipeManager.listRecipesForUser(user.getEmail());
        return CollectionResponse.<Recipe>builder().setItems(recipeList).build();
    }
}