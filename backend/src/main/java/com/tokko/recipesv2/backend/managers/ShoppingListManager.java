package com.tokko.recipesv2.backend.managers;

import com.google.inject.Inject;
import com.tokko.recipesv2.backend.endpoints.Constants;
import com.tokko.recipesv2.backend.engines.QuantityCalculatorEngine;
import com.tokko.recipesv2.backend.engines.ScheduleCalculatorEngine;
import com.tokko.recipesv2.backend.engines.ShoppingListAggregatorEngine;
import com.tokko.recipesv2.backend.engines.ShoppingListGeneralListPreparerEngine;
import com.tokko.recipesv2.backend.engines.ShoppingListItemConverterEngine;
import com.tokko.recipesv2.backend.entities.Grocery;
import com.tokko.recipesv2.backend.entities.Ingredient;
import com.tokko.recipesv2.backend.entities.Recipe;
import com.tokko.recipesv2.backend.entities.RecipeUser;
import com.tokko.recipesv2.backend.entities.ScheduleEntry;
import com.tokko.recipesv2.backend.entities.ShoppingList;
import com.tokko.recipesv2.backend.entities.ShoppingListItem;
import com.tokko.recipesv2.backend.resourceaccess.RecipeUserRa;
import com.tokko.recipesv2.backend.resourceaccess.ScheduleEntryRa;
import com.tokko.recipesv2.backend.resourceaccess.ShoppingListRa;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListManager {

    private ShoppingListRa shoppingListRa;
    private RecipeUserRa recipeUserRa;
    private ShoppingListGeneralListPreparerEngine shoppingListGeneralListPreparerEngine;
    private GroceryManager groceryManager;
    private ScheduleEntryRa scheduleEntryRa;
    private ShoppingListAggregatorEngine shoppingListAggregatorEngine;
    private ShoppingListItemConverterEngine shoppingListItemConverterEngine;
    private QuantityCalculatorEngine quantityCalculatorEngine;
    private ScheduleCalculatorEngine scheduleCalculatorEngine;

    @Inject
    public ShoppingListManager(ShoppingListRa shoppingListRa,
                               RecipeUserRa recipeUserRa,
                               ShoppingListGeneralListPreparerEngine shoppingListGeneralListPreparerEngine,
                               GroceryManager groceryManager,
                               ScheduleEntryRa scheduleEntryRa,
                               ShoppingListAggregatorEngine shoppingListAggregatorEngine,
                               ShoppingListItemConverterEngine shoppingListItemConverterEngine,
                               QuantityCalculatorEngine quantityCalculatorEngine,
                               ScheduleCalculatorEngine scheduleCalculatorEngine) {
        this.shoppingListRa = shoppingListRa;
        this.recipeUserRa = recipeUserRa;
        this.shoppingListGeneralListPreparerEngine = shoppingListGeneralListPreparerEngine;
        this.groceryManager = groceryManager;
        this.scheduleEntryRa = scheduleEntryRa;
        this.shoppingListAggregatorEngine = shoppingListAggregatorEngine;
        this.shoppingListItemConverterEngine = shoppingListItemConverterEngine;
        this.quantityCalculatorEngine = quantityCalculatorEngine;
        this.scheduleCalculatorEngine = scheduleCalculatorEngine;
    }

    public ShoppingList getLatestList(String email) {
        RecipeUser user = recipeUserRa.getUserByEmail(email);
        ShoppingList general = shoppingListRa.getShoppingList(user, Constants.GENERAL_LIST_ID);
        ShoppingList shoppingList = shoppingListRa.getLatestShoppingList(user);
        if(shoppingList != null){
            shoppingList.getItems().addAll(general.getItems());
            return shoppingList;
        }
        return general;
    }

    public void commitShoppingList(final ShoppingList shoppingList, String email) {
        RecipeUser user = recipeUserRa.getUserByEmail(email);
        shoppingList.setUser(user);
        List<Grocery> groceries = new ArrayList<>();
        for (ShoppingListItem item : shoppingList.getItems()) {
            groceries.add(item.ingredient.getGrocery());
        }
        groceryManager.commitGroceries(groceries, email);

        ShoppingList generalList = shoppingListRa.getShoppingList(user, 1L);
        generalList.getItems().addAll(shoppingListGeneralListPreparerEngine.getGeneralItems(shoppingList.getItems()));
        shoppingList.setItems(shoppingListGeneralListPreparerEngine.getGeneratedItems(shoppingList.getItems()));
        shoppingListRa.commitShoppingList(shoppingList);
        shoppingListRa.commitShoppingList(generalList);
    }

    public ShoppingList generateShoppingList(long date, String email) {
        RecipeUser user = recipeUserRa.getUserByEmail(email);

        ShoppingList generalList = getLatestList(email);

        List<ScheduleEntry> scheduleEntries = scheduleEntryRa.getScheduleEntries(date, user);

        List<Recipe> recipes = shoppingListAggregatorEngine.getRecipesFromScheduleEntries(scheduleEntries);

        List<Ingredient> ingredients = shoppingListAggregatorEngine.getIngredientsFromRecipes(recipes);

        List<ShoppingListItem> shoppingListItems = shoppingListItemConverterEngine.convertIngredientToShoppingListItems(ingredients);
        shoppingListItems.addAll(generalList.getItems());

        List<ShoppingListItem> baseLinedShoppingListItems = quantityCalculatorEngine.toBaseQuantitiesShoppingList(shoppingListItems);
        List<ShoppingListItem> aggregatedShoppingListItems = shoppingListAggregatorEngine.aggregateIngredients(baseLinedShoppingListItems);
        List<ShoppingListItem> upScaledShoppingListItems = quantityCalculatorEngine.upQuantitiesShoppingList(aggregatedShoppingListItems);

        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setStartDate(date);
        shoppingList.setItems(upScaledShoppingListItems);
        shoppingList.setExpirationDate(scheduleCalculatorEngine.getExpirationDate(scheduleEntries));
        commitShoppingList(shoppingList, email);
        return shoppingList;
    }

    public ShoppingList getGeneralList(String email) {
        RecipeUser user = recipeUserRa.getUserByEmail(email);
        ShoppingList generalList = shoppingListRa.getShoppingList(user, 1L);
        return generalList;
    }
}
