package milktea.milktea.DAO;

import milktea.milktea.DTO.RecipeDetail;
import milktea.milktea.DTO.Unit;

import java.util.ArrayList;

public class RecipeDetail_DAO {
    public static ArrayList<RecipeDetail> getRecipeDetail(){
        ArrayList<RecipeDetail> result = new ArrayList<>();
        if (Connect.openConnection("RecipeDetail")) {
            try {
                String sql = "Select * " +
                        "from recipedetail";

                var stmt = Connect.connection.createStatement();
                var rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    var recipeDetail = RecipeDetail.builder()
                            .recipeId(rs.getString("recipe_id"))
                            .ingredientId(rs.getString("ingredient_id"))
                            .quantity(rs.getFloat("quantity"))
                            .unit(Unit.valueOf(rs.getString("unit")))
                            .build();
                    result.add(recipeDetail);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Connect.closeConnection();
            }
        }
        return result;
    }
    public static boolean addRecipeDetail(RecipeDetail recipeDetail) {
        boolean result = false;
        if (Connect.openConnection("RecipeDetail")) {
            try {
                String sql = "Insert into recipedetail values(?,?,?,?)";

                var stmt = Connect.connection.prepareStatement(sql);

                stmt.setString(1, recipeDetail.getRecipeId());
                stmt.setString(2, recipeDetail.getIngredientId());
                stmt.setFloat(3, recipeDetail.getQuantity());
                stmt.setString(4, recipeDetail.getUnit().name());

                if (stmt.executeUpdate() >= 1) {
                    result = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Connect.closeConnection();
            }
        }
        return result;
    }

    public static boolean editRecipeDetail(RecipeDetail recipeDetail) {
        boolean result = false;
        if (Connect.openConnection("RecipeDetail")) {
            try {
                String sql = "Update recipedetail set quantity = ?, unit = ? where recipeid = ? and ingredientid = ?";

                var stmt = Connect.connection.prepareStatement(sql);

                stmt.setFloat(1, recipeDetail.getQuantity());
                stmt.setString(2, recipeDetail.getUnit().name());
                stmt.setString(3, recipeDetail.getRecipeId());
                stmt.setString(4, recipeDetail.getIngredientId());

                if (stmt.executeUpdate() >= 1) {
                    result = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Connect.closeConnection();
            }
        }
        return result;
    }

    public static boolean deleteRecipeDetail(String recipeId, String ingredientId) {
        boolean result = false;
        if (Connect.openConnection("RecipeDetail")) {
            try {
                String sql = "Delete from recipedetail where recipeid = ? and ingredientid = ?";

                var stmt = Connect.connection.prepareStatement(sql);

                stmt.setString(1, recipeId);
                stmt.setString(2, ingredientId);

                if (stmt.executeUpdate() >= 1) {
                    result = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Connect.closeConnection();
            }
        }
        return result;
    }

}
