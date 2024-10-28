package milktea.milktea.BUS;

import milktea.milktea.DTO.RecipeDetail;

import java.util.ArrayList;

public class RecipeDetail_DAO {
    public static ArrayList<RecipeDetail> getRecipeDetails(){
        return milktea.milktea.DAO.RecipeDetail_DAO.getRecipeDetail();
    }

    public static boolean addRecipeDetail(RecipeDetail recipeDetail){
        return milktea.milktea.DAO.RecipeDetail_DAO.addRecipeDetail(recipeDetail);
    }

    public static boolean editRecipeDetail(RecipeDetail recipeDetail){
        return milktea.milktea.DAO.RecipeDetail_DAO.editRecipeDetail(recipeDetail);
    }

    public static boolean deleteRecipeDetail(String recipeId, String ingredientId){
        return milktea.milktea.DAO.RecipeDetail_DAO.deleteRecipeDetail(recipeId, ingredientId);
    }
}
