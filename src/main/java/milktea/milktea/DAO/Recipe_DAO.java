package milktea.milktea.DAO;

import milktea.milktea.DTO.Recipe;

import java.util.ArrayList;

public class Recipe_DAO {

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
                            .id(rs.getString("id"))
                            .name(rs.getString("name"))
                            .build();
                    result.add(recipe);
                }
            } catch (Exception e) {
                e.printStackTrace();
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
                String sql = "Insert into recipe values(?,?)";

                var stmt = Connect.connection.prepareStatement(sql);

                stmt.setString(1, recipe.getId());
                stmt.setString(2, recipe.getName());

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

    public static boolean editRecipe(Recipe recipe) {
        boolean result = false;
        if (Connect.openConnection("Recipe")) {
            try {
                String sql = "Update recipe set name = ? where id = ?";

                var stmt = Connect.connection.prepareStatement(sql);

                stmt.setString(1, recipe.getName());
                stmt.setString(2, recipe.getId());

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
