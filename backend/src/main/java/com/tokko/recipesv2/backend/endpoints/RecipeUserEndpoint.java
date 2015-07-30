package com.tokko.recipesv2.backend.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.tokko.recipesv2.backend.entities.RecipeUser;
import com.tokko.recipesv2.backend.entities.RegistrationRecord;
import com.tokko.recipesv2.backend.managers.RecipeUserManager;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import static com.tokko.recipesv2.backend.util.GuiceModule.inject;

@Api(
        name = "recipeUserApi",
        version = "v1",
        // resource = "recipeUser",
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

    @ApiMethod(name = "foo")
    public RecipeUser foo() {
        Logger.getLogger(RecipeUserEndpoint.class.getName()).info("Foo called.");
        RecipeUser b = new RecipeUser();
        b.setEmail("email");
        return b;
    }

    @ApiMethod(name = "insert")
    public void registerDevice(@Named("regid") String regid, User user) throws UnauthorizedException {
        if (user == null) throw new UnauthorizedException("You shall not pass!");
        recipeUserManager.addRegistrationIdToRecipeUser(user.getEmail(), regid);
    }
    @ApiMethod(name = "getMockList")
    public CollectionResponse<RegistrationRecord> getMockList(User user) {
        List<RegistrationRecord> records = Arrays.asList(new RegistrationRecord("first"), new RegistrationRecord("Second"));//ofy().load().type(RegistrationRecord.class).limit(count).list();
        return CollectionResponse.<RegistrationRecord>builder().setItems(records).build();
    }

    public class Bean {
        public String string;
    }
}