package milktea.milktea.BUS;

import milktea.milktea.DAO.Recipe_DAO;
import milktea.milktea.DTO.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static milktea.milktea.Util.CalUnitUtil.*;

public class Recipe_BUS {

    private static ArrayList<Recipe> arrRecipes = new ArrayList<>();
    private static final HashMap<String, List<Recipe>> mapRecipes = new HashMap<>();

    public static void getLocalData(){
        arrRecipes = milktea.milktea.DAO.Recipe_DAO.getRecipes();
        getMapData();
    }
    public static void getMapData(){
        mapRecipes.clear();
        for (Recipe recipe : arrRecipes) {
            mapRecipes.computeIfAbsent(recipe.getProductId(), k -> new ArrayList<>()).add(recipe);
        }
    }

    public static ArrayList<Recipe> getRecipes(){
        return arrRecipes;
    }

    public static boolean addRecipe(ArrayList<Recipe> recipe){
        for (Recipe r : recipe) {
            if (!Recipe_DAO.addRecipe(r)) {
                return false;
            }
        }
        return true;
    }


    public static boolean editRecipe(ArrayList<Recipe> recipes) {
        for (Recipe recipe : recipes) {
            if (findRecipe(recipe.getProductId(), recipe.getIngredientId())) {
                // Nếu `editRecipe` thất bại, trả về `false` ngay lập tức
                if (!Recipe_DAO.editRecipe(recipe)) {
                    return false;
                }
            } else {
                // Nếu `addRecipe` thất bại, trả về `false` ngay lập tức
                if (!Recipe_DAO.addRecipe(recipe)) {
                    return false;
                }
            }
        }
        // Trả về `true` nếu tất cả các thao tác thành công
        return true;
    }


    public static boolean findRecipe(String productId, String ingredientId){
        for (Recipe recipe : arrRecipes) {
            if (recipe.getProductId().equals(productId) && recipe.getIngredientId().equals(ingredientId)) {
                return true;
            }
        }
        return false;
    }

    public static boolean deleteRecipe(ArrayList<Recipe> recipes){
        if (recipes != null) {
            for (Recipe recipe : recipes) {
                if (!Recipe_DAO.deleteRecipe(recipe.getProductId(), recipe.getIngredientId())) {
                    return false;
                }
            }
        }
        return true;
    }

public static boolean deleteLocalRecipe(ArrayList<Recipe> recipes) {
    if (recipes != null) {
        arrRecipes.removeIf(recipe -> recipes.stream()
            .anyMatch(r -> r.getProductId().equals(recipe.getProductId()) && r.getIngredientId().equals(recipe.getIngredientId())));
    }
    getMapData();
    return true;
}
    public static ArrayList<Product> forecastProductQuantity(ArrayList<Product> arrProducts, HashMap<String,Ingredient> arrIngredient) {

        // Update the quantities of the products based on the available ingredients
        for (Product product : arrProducts) {
            float minQuantity = Float.MAX_VALUE;
            List<Recipe> arrRecipes = mapRecipes.get(product.getProductId());
            // Iterate through the ingredients of the product
            for (Recipe recipe : arrRecipes) {
                    String ingredientId = recipe.getIngredientId();
                    float requiredQuantity = (float) recipe.getQuantity();

                    // Find the available quantity of the ingredient
                    Ingredient ingredient = Ingredient_BUS.getIngredientById(arrIngredient, ingredientId);
                    if (ingredient != null) {
                        float possibleQuantity = calUnitConverter(recipe.getUnit(),ingredient.getUnit(),requiredQuantity,ingredient.getQuantity());

                        // Update the minimum quantity
                        if (possibleQuantity < minQuantity) {
                            minQuantity = possibleQuantity;
                        }
                    }
            }
            // Set the quantity of the product
            product.setQuantity((int) Math.floor(minQuantity));
        }

        return arrProducts;
    }


    public static ArrayList<Product> updateProductQuantity(ArrayList<Product> arrProducts, ArrayList<TempInvoiceDetail> arrTempInvoiceDetail, boolean isAdd) {
        // Deep clone the ingredients
        ArrayList<Ingredient> arrIngredient = Ingredient_BUS.deepCloneIngredient(Ingredient_BUS.getAllIngredient());
        //Crete a map for quick lookup of ingredients by their IDs
        HashMap<String, Ingredient> ingredientMap = new HashMap<>();
        for (Ingredient ingredient : arrIngredient) {
            ingredientMap.put(ingredient.getId(), ingredient);
        }

        if (arrTempInvoiceDetail != null) {
            for (TempInvoiceDetail tempInvoiceDetail : arrTempInvoiceDetail) {
                // Get the product and its quantity
                Product product = Product_BUS.quickGetProductById(tempInvoiceDetail.getInvoiceDetail().getProductId());
                int productQuantity = tempInvoiceDetail.getInvoiceDetail().getQuantity();

                // Update the required ingredients for the product
                assert product != null;
                updateIngredients(arrIngredient, product, productQuantity, isAdd);

                // Iterate through the toppings
                if (tempInvoiceDetail.getTopping() != null) {
                    for (Map.Entry<String, Integer> toppingEntry : tempInvoiceDetail.getTopping().entrySet()) {
                        Product topping = Product_BUS.quickGetProductById(toppingEntry.getKey());
                        int toppingQuantity = toppingEntry.getValue();

                        // Update the required ingredients for the topping
                        assert topping != null;
                        updateIngredients(arrIngredient, topping, toppingQuantity, isAdd);
                    }
                }
            }
        }


    return forecastProductQuantity(arrProducts, ingredientMap);
}

private static void updateIngredients(ArrayList<Ingredient> arrIngredient, Product product, int quantity, boolean isAdd) {
    HashMap<String, Ingredient> ingredientMap = new HashMap<>();
    for (Ingredient ingredient : arrIngredient) {
        ingredientMap.put(ingredient.getId(), ingredient);
    }
    // Iterate through the ingredients of the product
    for (Recipe recipe : arrRecipes) {
        if (recipe.getProductId().equals(product.getProductId())) {
            String ingredientId = recipe.getIngredientId();
            float requiredQuantity = (float) recipe.getQuantity() * quantity;

            // Find the ingredient and update its quantity
            Ingredient ingredient = Ingredient_BUS.getIngredientById(ingredientMap, ingredientId);
            if (ingredient != null) {
                float newQuantity = isAdd ? addUnitConverter(recipe.getUnit(), ingredient.getUnit(), requiredQuantity, ingredient.getQuantity()) : subtractUnitConverter(recipe.getUnit(), ingredient.getUnit(), requiredQuantity, ingredient.getQuantity());
                ingredient.setQuantity(newQuantity);
            }
        }
    }
}

    public static List<Recipe> getRecipeByProductID(String productId) {
        return mapRecipes.get(productId);
    }

    public static boolean editRecipeLocal(Recipe recipe) {
        for (Recipe r : arrRecipes) {
            if (r.getProductId().equals(recipe.getProductId()) && r.getIngredientId().equals(recipe.getIngredientId())) {
                r.setQuantity(recipe.getQuantity());
                r.setUnit(recipe.getUnit());
                return true;
            }
        }
        return false;
    }

    public static boolean addRecipeLocal(ArrayList<Recipe> recipes) {
        for (Recipe recipe : recipes) {
            if (!findRecipe(recipe.getProductId(), recipe.getIngredientId())) {
                arrRecipes.add(recipe);
                mapRecipes.computeIfAbsent(recipe.getProductId(), k -> new ArrayList<>()).add(recipe);
            }
        }
        return true;
    }
    public static boolean addRecipeLocal(Recipe recipe) {
        if (!findRecipe(recipe.getProductId(), recipe.getIngredientId())) {
            arrRecipes.add(recipe);
            return true;
        }
        return false;
    }

    public static boolean editRecipesLocal(ArrayList<Recipe> tempArrRecipe) {
        for (Recipe recipe : tempArrRecipe) {
            if (findRecipe(recipe.getProductId(), recipe.getIngredientId())) {
                if(!editRecipeLocal(recipe)){
                    return false;
                }
            } else {
                if(!addRecipeLocal(recipe)){
                    return false;
                }
            }
        }
        getMapData();
        return true;
    }

    public static boolean deleteRecipeByProductID(String productId) {
        return Recipe_DAO.deleteRecipeByProductID(productId);
    }

    public static boolean deleteLocalRecipeByProductID(String productId) {
        arrRecipes.removeIf(recipe -> recipe.getProductId().equals(productId));
        getMapData();
        return true;
    }
}
