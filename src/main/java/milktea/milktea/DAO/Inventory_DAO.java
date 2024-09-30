package milktea.milktea.DAO;

import milktea.milktea.DTO.Inventory;
import milktea.milktea.DTO.Status;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Inventory_DAO extends Connect{
    public static ArrayList<Inventory> getAllInventory(){
        ArrayList<Inventory> arrInventory = new ArrayList<>();
        if(openConnection("Inventory")){
            try{
                String sql = "Select * " +
                        "from inventory";

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    Inventory inventory = Inventory.builder()
                            .inventoryId(rs.getString("inventoryId"))
                            .ingredientId(rs.getString("ingredientId"))
                            .quantityInStock(rs.getDouble("quantity"))
                            .lastUpdatedDate(rs.getDate("lastUpdatedDate").toLocalDate())
                            .expirationDate(rs.getDate("expirationDate").toLocalDate())
                            .price(rs.getBigDecimal("price"))
                            .status(getStatus(rs.getString("status")))
                            .build();
                    arrInventory.add(inventory);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                closeConnection();
            }
        }
        return arrInventory;
    }
    public static boolean addInventory(Inventory inventory){
        boolean result = false;
        if(openConnection("Inventory")){
            try{
                String sql = "Insert into inventory values(?,?,?,?,?,?,?)";

                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setString(1,inventory.getInventoryId());
                stmt.setString(2,inventory.getIngredientId());
                stmt.setDouble(3,inventory.getQuantityInStock());
                stmt.setDate(4,java.sql.Date.valueOf(inventory.getLastUpdatedDate()));
                stmt.setDate(5,java.sql.Date.valueOf(inventory.getExpirationDate()));
                stmt.setBigDecimal(6,inventory.getPrice());
                stmt.setString(7,inventory.getStatus().toString());


                if(stmt.executeUpdate()>=1){
                    result = true;
                }

            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                closeConnection();
            }
        }
        return result;
    }
    public static boolean editInventory(Inventory inventory){
        boolean result = false;
        if(openConnection("Inventory")){
            try{
                String sql = "Update inventory set ingredientId = ?, quantityInStock = ?, lastUpdatedDate = ?, expirationDate = ?, price = ?, status = ? where inventoryId = ?";

                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setString(1,inventory.getIngredientId());
                stmt.setDouble(2,inventory.getQuantityInStock());
                stmt.setDate(3,java.sql.Date.valueOf(inventory.getLastUpdatedDate()));
                stmt.setDate(4,java.sql.Date.valueOf(inventory.getExpirationDate()));
                stmt.setBigDecimal(5,inventory.getPrice());
                stmt.setString(6,inventory.getStatus().toString());
                stmt.setString(7,inventory.getInventoryId());

                if(stmt.executeUpdate()>=1){
                    result = true;
                }

            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                closeConnection();
            }
        }
        return result;
    }

    public static boolean clearInventory(Inventory inventory){
        boolean result = false;
        if(openConnection("Inventory")){
            try{
        String sql = "UPDATE inventory SET status = ? , quantityInStock = 0 WHERE inventoryId = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setString(1, Status.DISPOSED.toString());
                stmt.setString(2,inventory.getInventoryId());

                if(stmt.executeUpdate()>=1){
                    result = true;
                }

            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                closeConnection();
            }
        }
        return result;
    }
}
