package milktea.milktea.BUS;

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
        for (Recipe recipe : arrRecipes) {
            mapRecipes.computeIfAbsent(recipe.getProductId(), k -> new ArrayList<>()).add(recipe);
        }
    }

    public static ArrayList<Recipe> getRecipes(){
        return arrRecipes;
    }

    public static boolean addRecipe(Recipe recipe){
        return milktea.milktea.DAO.Recipe_DAO.addRecipe(recipe);
    }

    public static boolean editRecipe(Recipe recipe){
        return milktea.milktea.DAO.Recipe_DAO.editRecipe(recipe);
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

}
