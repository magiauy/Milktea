package milktea.milktea.DAO;

import lombok.extern.slf4j.Slf4j;
import milktea.milktea.DTO.Recipe;

import java.util.ArrayList;
@Slf4j
public class Recipe_DAO extends Connect {

    public static ArrayList<Recipe> getRecipes() {
        ArrayList<Recipe> result = new ArrayList<>();
        if (Connect.openConnection("Recipe")) {
            try {
                String sql = "Select * " +
                        "from recipe";

                var stmt = Connect.connection.createStatement();
                var rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    var recipe = Recipe.builder()
                            .productId(rs.getString("productId"))
                            .ingredientId(rs.getString("ingredientId"))
                            .quantity(rs.getDouble("quantity"))
                            .unit(getUnit(rs.getString("unit")))
                            .build();
                    result.add(recipe);
                }
            } catch (Exception e) {
                log.error("Error: ", e);
            } finally {
                Connect.closeConnection();
            }
        }
        return result;
    }


    public static boolean addRecipe(Recipe recipe) {
        boolean result = false;
        if (Connect.openConnection("Recipe")) {
            try {
                String sql = "Insert into recipe values(?,?,?,?)";

                var stmt = Connect.connection.prepareStatement(sql);

                stmt.setString(1, recipe.getProductId());
                stmt.setString(2, recipe.getIngredientId());
                stmt.setDouble(3, recipe.getQuantity());
                stmt.setString(4, recipe.getUnit().toString());

                if (stmt.executeUpdate() >= 1) {
                    result = true;
                }

            } catch (Exception e) {
                log.error("Error: ", e);
            } finally {
                Connect.closeConnection();
            }
        }
        return result;
    }

    public static boolean editRecipe(Recipe recipe) {
        boolean result = false;
        if (Connect.openConnection()) {
            try {
                String sql = "Update recipe set quantity = ?, unit = ? where productId = ? and ingredientId = ?";

                var stmt = Connect.connection.prepareStatement(sql);

                stmt.setDouble(1, recipe.getQuantity());
                stmt.setString(2, recipe.getUnit().toString());
                stmt.setString(3, recipe.getProductId());
                stmt.setString(4, recipe.getIngredientId());

                if (stmt.executeUpdate() >= 1) {
                    result = true;
                }

            } catch (Exception e) {
                log.error("Error: ", e);
            } finally {
                Connect.closeConnection();
            }
        }
        return result;
    }

    public static boolean deleteRecipe(String productId, String ingredientId) {
        boolean result = false;
        if (Connect.openConnection()) {
            try {
                String sql = "Delete from recipe where productId = ? and ingredientId = ?";

                var stmt = Connect.connection.prepareStatement(sql);

                stmt.setString(1, productId);
                stmt.setString(2, ingredientId);

                if (stmt.executeUpdate() >= 1) {
                    result = true;
                }

            } catch (Exception e) {
                log.error("Error: ", e);
            } finally {
                Connect.closeConnection();
            }
        }
        return result;
    }

    public static boolean deleteRecipeByProductID(String productId) {
        boolean result = false;
        if (Connect.openConnection()) {
            try {
                String sql = "Delete from recipe where productId = ?";

                var stmt = Connect.connection.prepareStatement(sql);

                stmt.setString(1, productId);

                if (stmt.executeUpdate() >= 1) {
                    result = true;
                }

            } catch (Exception e) {
                log.error("Error: ", e);
            } finally {
                Connect.closeConnection();
            }
        }
        return result;
    }
}
