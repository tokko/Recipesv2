package com.tokko.recipesv2.backend.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.google.inject.Inject;
import com.tokko.recipesv2.backend.engines.QuantityCalculatorEngine;
import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.entities.ScheduleEntry;
import com.tokko.recipesv2.backend.entities.ShoppingList;
import com.tokko.recipesv2.backend.managers.GroceryManager;
import com.tokko.recipesv2.backend.managers.RecipeManager;
import com.tokko.recipesv2.backend.managers.RecipeUserManager;
import com.tokko.recipesv2.backend.managers.ScheduleEntryManager;
import com.tokko.recipesv2.backend.managers.ShoppingListManager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.tokko.recipesv2.backend.util.GuiceModule.inject;

@Api(
        name = "recipeApi",
 //       resource = "recipe",
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
public class RecipesApi {

    @Inject
    private RecipeManager recipeManager;

    @Inject
    private RecipeUserManager recipeUserManager;
    @Inject
    private GroceryManager groceryManager;

    @Inject
    private QuantityCalculatorEngine quantityCalculatorEngine;

    @Inject
    private ScheduleEntryManager scheduleEntryManager;

    @Inject
    private ShoppingListManager shoppingListManager;

    public RecipesApi() {
        inject(this);
    }

    @ApiMethod(
            name = "commitGrocery",
            path = "grocery",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Grocery commitGrocery(Grocery grocery, User user) throws OAuthRequestException {
        if (user == null) throw new OAuthRequestException("User us not allowed");
        try {
            return groceryManager.commitGrocery(grocery, user.getEmail());
        } catch (Exception e) {
            Logger.getLogger(RecipesApi.class.getName()).info(e.getMessage());
            return null;
        }
    }

    @ApiMethod(
            name = "deleteGrocery",
            path = "grocery/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void deleteGrocery(@Named("id") Long id) throws NotFoundException {
        throw new UnsupportedOperationException("not implemented");
    }

    @ApiMethod(
            name = "getSchedule",
            path = "schedule",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<ScheduleEntry> getSchedule(@Named("time") Long time, User user){
        List<ScheduleEntry> schedule = scheduleEntryManager.getSchedule(time, user.getEmail());
        return CollectionResponse.<ScheduleEntry>builder().setItems(schedule).build();
    }

    @ApiMethod(
            name = "commitSchedule",
            path = "schedule",
            httpMethod = ApiMethod.HttpMethod.POST)
    public void commitSchedule(CommitScheduleContainer entries, User user) {
        scheduleEntryManager.commitSchedule(entries.entries, user.getEmail());
    }

    @ApiMethod(
            name = "listGroceries",
            path = "grocery",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Grocery> listGroceries(User user) throws OAuthRequestException {
        if (user == null) throw new OAuthRequestException("User is not allowed");
        try {
            List<Grocery> groceryList = groceryManager.listGroceries(user.getEmail());
            return CollectionResponse.<Grocery>builder().setItems(groceryList).build();
        } catch (Exception e) {
            Logger.getLogger(RecipesApi.class.getName()).info(e.getMessage());
            return null;
        }
    }

    @ApiMethod(
            name = "registerDevice",
            path = "recipeUser",
            httpMethod = ApiMethod.HttpMethod.GET)
    public void registerDevice(@com.google.api.server.spi.config.Named("regid") String regid, User user) throws UnauthorizedException {
        if (user == null) throw new UnauthorizedException("You shall not pass!");
        recipeUserManager.addRegistrationIdToRecipeUser(user.getEmail(), regid);
    }

    @ApiMethod(
            name = "getShoppingList",
            path = "shoppingList",
            httpMethod = ApiMethod.HttpMethod.GET)
    public ShoppingList getShoppingList(User user) {
        return shoppingListManager.getGeneralList(user.getEmail());
    }

    @ApiMethod(
            name = "listUnits",
            path = "quantity",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<String> listUnits() throws UnauthorizedException {
        return CollectionResponse.<String>builder().setItems(quantityCalculatorEngine.listUnits()).build();
    }

    @ApiMethod(
            name = "get",
            path = "recipe/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Recipe getRecipe(@Named("id") Long id) throws NotFoundException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @ApiMethod(
            name = "commitRecipe",
            path = "recipe",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Recipe commitRecipe(Recipe recipe, User user) {
        return recipeManager.commitRecipe(recipe, user.getEmail());
    }

    @ApiMethod(
            name = "deleteRecipe",
            path = "recipe/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void deleteRecipe(@Named("id") Long id) throws NotFoundException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @ApiMethod(
            name = "listRecipes",
            path = "recipe",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Recipe> listRecipes(User user) {
        List<Recipe> recipeList = recipeManager.listRecipesForUser(user.getEmail());
        return CollectionResponse.<Recipe>builder().setItems(recipeList).build();
    }

    public static class CommitScheduleContainer {
        public List<ScheduleEntry> entries;

        public CommitScheduleContainer() {
            this.entries = new ArrayList<>();
        }

        public List<ScheduleEntry> getEntries() {
            return entries;
        }

        public void setEntries(List<ScheduleEntry> entries) {
            this.entries = entries;
        }
    }
}
