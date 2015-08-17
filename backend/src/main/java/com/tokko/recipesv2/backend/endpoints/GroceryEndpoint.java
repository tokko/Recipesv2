package com.tokko.recipesv2.backend.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.google.inject.Inject;
import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.managers.GroceryManager;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.tokko.recipesv2.backend.util.GuiceModule.inject;

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
}