package com.tokko.recipesv2.backend.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.oauth.OAuthRequestException;
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
        version = Constants.API_VERSION,
        resource = "grocery",
        scopes = {Constants.EMAIL_SCOPE},
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
    @ApiMethod(
            name = "insert",
            path = "grocery",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Grocery insert(Grocery grocery, User user) throws OAuthRequestException {
        if (user == null) throw new OAuthRequestException("User us not allowed");
        try {
            return groceryManager.commitGrocery(grocery, user.getEmail());
        } catch (Exception e) {
            Logger.getLogger(GroceryEndpoint.class.getName()).info(e.getMessage());
            return null;
        }
    }

    @ApiMethod(
            name = "remove",
            path = "grocery/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        throw new UnsupportedOperationException("not implemented");
    }

    @ApiMethod(
            name = "list",
            path = "grocery",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Grocery> list(User user) throws OAuthRequestException {
        if (user == null) throw new OAuthRequestException("User is not allowed");
        try {
            List<Grocery> groceryList = groceryManager.listGroceries(user.getEmail());
            return CollectionResponse.<Grocery>builder().setItems(groceryList).build();
        } catch (Exception e) {
            Logger.getLogger(GroceryEndpoint.class.getName()).info(e.getMessage());
            return null;
        }
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(Grocery.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Grocery with ID: " + id);
        }
    }
}