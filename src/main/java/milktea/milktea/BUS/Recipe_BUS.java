package milktea.milktea.BUS;

import milktea.milktea.DTO.Recipe;

import java.util.ArrayList;

public class Recipe_BUS {
    public static ArrayList<Recipe> getRecipes(){
        return milktea.milktea.DAO.Recipe_DAO.getRecipes();
    }

    public static boolean addRecipe(Recipe recipe){
        return milktea.milktea.DAO.Recipe_DAO.addRecipe(recipe);
    }

    public static boolean editRecipe(Recipe recipe){
        return milktea.milktea.DAO.Recipe_DAO.editRecipe(recipe);
    }


}
