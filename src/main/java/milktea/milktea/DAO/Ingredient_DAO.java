package milktea.milktea.DAO;

import lombok.extern.slf4j.Slf4j;
import milktea.milktea.DTO.Ingredient;
import milktea.milktea.DTO.Status;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.SQLException;

@Slf4j
public class Ingredient_DAO extends Connect{
    public static ArrayList<Ingredient> getAllIngredient() {
        ArrayList<Ingredient> list = new ArrayList<>();
        try {
            if (openConnection("Ingredient")) {
                String sql = "Select * from ingredient";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Ingredient ingredient = Ingredient.builder()
                            .id(rs.getString("id"))
                            .name(rs.getString("name"))
                            .quantity(rs.getFloat("quantity"))
                            .unit(getUnit(rs.getString("unit")))
                            .status(getStatus(rs.getString("status")))
                            .build();
                    list.add(ingredient);
                }
            }
            }catch(SQLException e){
                log.error("e: ", e);
            }finally{
                closeConnection();
            }

            return list;
        }
    public static boolean addIngredient(Ingredient ingredient){
        boolean result = false;
        try{
            if(openConnection("Ingredient")){
                String sql = "Insert into ingredient values(?,?,?,?,?)";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,ingredient.getId());
                stmt.setString(2,ingredient.getName());
                stmt.setFloat(3,ingredient.getQuantity());
                stmt.setString(4,ingredient.getUnit().toString());
                stmt.setString(5,ingredient.getStatus().toString());
                if(stmt.executeUpdate()>=1){
                    result = true;
                }
            }
        }catch(SQLException e){
            log.error("e: ", e);
        }finally{
            closeConnection();
        }
        return result;
    }
    public static boolean editIngredient(Ingredient ingredient){
        boolean result = false;
        try{
            if(openConnection()){
                String sql = "Update ingredient set name = ?, unit = ?, status = ?, quantity = ? where id = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,ingredient.getName());
                stmt.setString(2,ingredient.getUnit().toString());
                stmt.setString(3,ingredient.getStatus().toString());
                stmt.setFloat(4,ingredient.getQuantity());
                stmt.setString(5,ingredient.getId());
                if(stmt.executeUpdate()>=1){
                    result = true;
                }
            }
        }catch(SQLException e){
            log.error("e: ", e);
        }finally{
            closeConnection();
        }
        return result;
    }

    public static boolean deleteIngredient(String id){
        boolean result = false;
        try{
            if(openConnection()){
                String sql = "Delete from ingredient where id = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,id);
                if(stmt.executeUpdate()>=1){
                    result = true;
                }
            }
        }catch(SQLException e){
            log.error("e: ", e);
        }finally{
            closeConnection();
        }
        return result;
    }

    public static boolean changeStatus(String id, Status status) {
        boolean result = false;
        try {
            if (openConnection()) {
                String sql = "Update ingredient set status = ? where id = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, status.toString());
                stmt.setString(2, id);
                if (stmt.executeUpdate() >= 1) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            log.error("e: ", e);
            return false;
        } finally {
            closeConnection();
        }
        return result;
    }
}

