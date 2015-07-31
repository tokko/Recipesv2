package com.tokko.recipesv2.backend.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;
import com.google.inject.Inject;
import com.googlecode.objectify.ObjectifyService;
import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.managers.GroceryManager;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static com.tokko.recipesv2.backend.util.GuiceModule.inject;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "groceryApi",
        version = "v1",
        resource = "grocery",
        clientIds = {Constants.ANDROID_CLIENT_ID, Constants.WEB_CLIENT_ID},
        audiences = {Constants.ANDROID_AUDIENCE},
        namespace = @ApiNamespace(
                ownerDomain = "entities.backend.recipesv2.tokko.com",
                ownerName = "entities.backend.recipesv2.tokko.com",
                packagePath = ""
        )
)
public class GroceryEndpoint {

    private static final Logger logger = Logger.getLogger(GroceryEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Grocery.class);
    }

    @Inject
    private GroceryManager groceryManager;

    public GroceryEndpoint() {
        inject(this);
    }
    /**
     * Returns the {@link Grocery} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Grocery} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "grocery/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Grocery get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting Grocery with ID: " + id);
        Grocery grocery = ofy().load().type(Grocery.class).id(id).now();
        if (grocery == null) {
            throw new NotFoundException("Could not find Grocery with ID: " + id);
        }
        return grocery;
    }

    /**
     * Inserts a new {@code Grocery}.
     */
    @ApiMethod(
            name = "insert",
            path = "grocery",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Grocery insert(Grocery grocery) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that grocery.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(grocery).now();
        logger.info("Created Grocery with ID: " + grocery.getId());

        return ofy().load().entity(grocery).now();
    }

    /**
     * Updates an existing {@code Grocery}.
     *
     * @param id      the ID of the entity to be updated
     * @param grocery the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Grocery}
     */
    @ApiMethod(
            name = "update",
            path = "grocery/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Grocery update(@Named("id") Long id, Grocery grocery) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(grocery).now();
        logger.info("Updated Grocery: " + grocery);
        return ofy().load().entity(grocery).now();
    }

    /**
     * Deletes the specified {@code Grocery}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Grocery}
     */
    @ApiMethod(
            name = "remove",
            path = "grocery/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(Grocery.class).id(id).now();
        logger.info("Deleted Grocery with ID: " + id);
    }

    @ApiMethod(
            name = "list",
            path = "grocery",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Grocery> list(User user) {
        List<Grocery> groceryList = groceryManager.listGroceries(user.getEmail());
        return CollectionResponse.<Grocery>builder().setItems(groceryList).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(Grocery.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Grocery with ID: " + id);
        }
    }
}