package milktea.milktea.BUS;

import milktea.milktea.DAO.Ingredient_DAO;
import milktea.milktea.DTO.Ingredient;
import milktea.milktea.DTO.InvoiceDetail;
import milktea.milktea.DTO.Recipe;
import milktea.milktea.Util.CalUnitUtil;

import java.util.*;
import java.util.stream.Collectors;


public class Ingredient_BUS {

    private static ArrayList<Ingredient> arrIngredients = new ArrayList<>();
    private static final HashMap<String, Ingredient> tempArrIngredients = new HashMap<>();

    public static void getLocalData() {
        arrIngredients = Ingredient_DAO.getAllIngredient();
        for (Ingredient ingredient : arrIngredients) {
            tempArrIngredients.put(ingredient.getId(), ingredient);
        }
    }

    public static ArrayList<Ingredient> getAllIngredient() {
        return arrIngredients;
    }

    public static boolean addIngredient(Ingredient ingredient) {
        return Ingredient_DAO.addIngredient(ingredient);
    }

    public static boolean editIngredient(Ingredient ingredient) {
        return Ingredient_DAO.editIngredient(ingredient);
    }

    public static String getIngredientNameById(String id) {
        return tempArrIngredients.get(id).getName();
    }
    public static Ingredient getIngredientById(HashMap<String,Ingredient> tempArrIngredients,String id) {
        return tempArrIngredients.get(id);
    }
    public static Ingredient getIngredientById(String id) {
        return tempArrIngredients.get(id);
    }

    public static ArrayList<Ingredient> deepCloneIngredient(ArrayList<Ingredient> tempArrIngredients) {
        ArrayList<Ingredient> clonedList = new ArrayList<>();
        for (Ingredient ingredient : tempArrIngredients) {
            // Assuming Ingredient class has a copy constructor
            clonedList.add(new Ingredient(ingredient));
        }
        return clonedList;
    }
    public static ArrayList<Ingredient> deepCloneIngredient() {
        return deepCloneIngredient(arrIngredients);
    }
    public static ArrayList<Ingredient> getIngredientsExcludeIds(ArrayList<Recipe> recipes) {
    ArrayList<Ingredient> ingredients = deepCloneIngredient();
    Set<String> recipeIngredientIds = recipes.stream()
        .map(Recipe::getIngredientId)
        .collect(Collectors.toSet());

    ingredients.removeIf(ingredient -> recipeIngredientIds.contains(ingredient.getId()));
    return ingredients;
}

    public static boolean updateIngredient(ArrayList<InvoiceDetail> arrInvoiceDetail) {
    // Create a map for quick lookup of ingredients by their IDs
    Map<String, Ingredient> ingredientMap = new HashMap<>();
    for (Ingredient ingredient : arrIngredients) {
        ingredientMap.put(ingredient.getId(), ingredient);
    }

    // Create a map for quick lookup of recipes by their product IDs
    Map<String, List<Recipe>> recipeMap = new HashMap<>();
    for (Recipe recipe : Recipe_BUS.getRecipes()) {
        recipeMap.computeIfAbsent(recipe.getProductId(), k -> new ArrayList<>()).add(recipe);
    }

    // Iterate through invoice details and update ingredients
    for (InvoiceDetail invoiceDetail : arrInvoiceDetail) {
        List<Recipe> recipes = recipeMap.get(invoiceDetail.getProductId());
        if (recipes != null) {
            for (Recipe recipe : recipes) {
                Ingredient ingredient = ingredientMap.get(recipe.getIngredientId());
                if (ingredient != null) {
                    ingredient.setQuantity(CalUnitUtil.subtractUnitConverter(
                            recipe.getUnit(), ingredient.getUnit(), (float) recipe.getQuantity(), ingredient.getQuantity()));
                    if (!editIngredient(ingredient)) {
                        return false;
                    }
                }
            }
        }
    }
    return true;
}
}
