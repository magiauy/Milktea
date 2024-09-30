package milktea.milktea.BUS;

import milktea.milktea.DAO.Ingredient_DAO;
import milktea.milktea.DTO.Ingredient;

import java.util.ArrayList;
import java.util.Objects;

public class Ingredient_BUS {
    public static ArrayList<Ingredient> getAllIngredient() {
        return Ingredient_DAO.getAllIngredient();
    }

    public static boolean addIngredient(Ingredient ingredient) {
        return Ingredient_DAO.addIngredient(ingredient);
    }

    public static boolean editIngredient(Ingredient ingredient) {
        return Ingredient_DAO.editIngredient(ingredient);
    }


}
