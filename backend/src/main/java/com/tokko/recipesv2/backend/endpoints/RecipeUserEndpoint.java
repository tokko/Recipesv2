package com.tokko.recipesv2.backend.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;
import com.tokko.recipesv2.backend.enteties.RecipeUser;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
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

    private static final Logger logger = Logger.getLogger(RecipeUserEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(RecipeUser.class);
    }

    /**
     * Returns the {@link RecipeUser} with the corresponding ID.
     *
     * @param email the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code RecipeUser} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "recipeUser/{email}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public RecipeUser get(@Named("email") String email) throws NotFoundException {
        logger.info("Getting RecipeUser with ID: " + email);
        RecipeUser recipeUser = ofy().load().type(RecipeUser.class).id(email).now();
        if (recipeUser == null) {
            throw new NotFoundException("Could not find RecipeUser with ID: " + email);
        }
        return recipeUser;
    }

    /**
     * Inserts a new {@code RecipeUser}.
     */
    @ApiMethod(
            name = "insert",
            path = "recipeUser",
            httpMethod = ApiMethod.HttpMethod.POST)
    public RecipeUser insert(RecipeUser recipeUser) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that recipeUser.email has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(recipeUser).now();
        logger.info("Created RecipeUser with ID: " + recipeUser.getEmail());

        return ofy().load().entity(recipeUser).now();
    }

    /**
     * Updates an existing {@code RecipeUser}.
     *
     * @param email      the ID of the entity to be updated
     * @param recipeUser the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code email} does not correspond to an existing
     *                           {@code RecipeUser}
     */
    @ApiMethod(
            name = "update",
            path = "recipeUser/{email}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public RecipeUser update(@Named("email") String email, RecipeUser recipeUser) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(email);
        ofy().save().entity(recipeUser).now();
        logger.info("Updated RecipeUser: " + recipeUser);
        return ofy().load().entity(recipeUser).now();
    }

    /**
     * Deletes the specified {@code RecipeUser}.
     *
     * @param email the ID of the entity to delete
     * @throws NotFoundException if the {@code email} does not correspond to an existing
     *                           {@code RecipeUser}
     */
    @ApiMethod(
            name = "remove",
            path = "recipeUser/{email}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("email") String email) throws NotFoundException {
        checkExists(email);
        ofy().delete().type(RecipeUser.class).id(email).now();
        logger.info("Deleted RecipeUser with ID: " + email);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "recipeUser",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<RecipeUser> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<RecipeUser> query = ofy().load().type(RecipeUser.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<RecipeUser> queryIterator = query.iterator();
        List<RecipeUser> recipeUserList = new ArrayList<RecipeUser>(limit);
        while (queryIterator.hasNext()) {
            recipeUserList.add(queryIterator.next());
        }
        return CollectionResponse.<RecipeUser>builder().setItems(recipeUserList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(String email) throws NotFoundException {
        try {
            ofy().load().type(RecipeUser.class).id(email).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find RecipeUser with ID: " + email);
        }
    }
}