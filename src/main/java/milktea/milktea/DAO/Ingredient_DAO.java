package milktea.milktea.DAO;

import milktea.milktea.DTO.Ingredient;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.SQLException;

public class Ingredient_DAO extends Connect{
    public static ArrayList<Ingredient> getAllIngredient() {
        ArrayList<Ingredient> list = new ArrayList<Ingredient>();
        try {
            if (openConnection("Ingredient")) {
                String sql = "Select * from ingredient";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Ingredient ingredient = Ingredient.builder()
                            .id(rs.getString("ingredientId"))
                            .name(rs.getString("ingredientName"))
                            .quantity(rs.getFloat("quantity"))
                            .price(rs.getBigDecimal("price"))
                            .unit(getUnit(rs.getString("unit")))
                            .status(getStatus(rs.getString("status")))
                            .build();
                    list.add(ingredient);
                }
            }
            }catch(SQLException e){
                System.out.println(e);
            }finally{
                closeConnection();
            }

            return list;
        }
    public static boolean addIngredient(Ingredient ingredient){
        boolean result = false;
        try{
            if(openConnection("Ingredient")){
                String sql = "Insert into ingredient values(?,?,?,?,?,?)";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,ingredient.getId());
                stmt.setString(2,ingredient.getName());
                stmt.setBigDecimal(3,ingredient.getPrice());
                stmt.setFloat(4,ingredient.getQuantity());
                stmt.setString(5,ingredient.getUnit().toString());
                stmt.setString(6,ingredient.getStatus().toString());
                if(stmt.executeUpdate()>=1){
                    result = true;
                }
            }
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            closeConnection();
        }
        return result;
    }
    public static boolean editIngredient(Ingredient ingredient){
        boolean result = false;
        try{
            if(openConnection("Ingredient")){
                String sql = "Update ingredient set name = ?, unit = ?, status = ?, price = ?, quantity = ? where id = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,ingredient.getName());
                stmt.setString(2,ingredient.getUnit().toString());
                stmt.setString(3,ingredient.getStatus().toString());
                stmt.setBigDecimal(4,ingredient.getPrice());
                stmt.setFloat(5,ingredient.getQuantity());
                stmt.setString(6,ingredient.getId());
                if(stmt.executeUpdate()>=1){
                    result = true;
                }
            }
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            closeConnection();
        }
        return result;
    }
}

