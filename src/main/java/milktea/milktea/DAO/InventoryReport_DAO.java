package milktea.milktea.DAO;

import milktea.milktea.DTO.InventoryReport;

import java.sql.*;
import java.util.ArrayList;

public class InventoryReport_DAO extends Connect{
    public static ArrayList<InventoryReport> getAllInventoryReport(){
        ArrayList<InventoryReport> arrInventoryReport = new ArrayList<>();
        if(openConnection("InventoryReport")){
            try{
                String sql = "Select * " +
                        "from inventoryreport";

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    InventoryReport inventoryReport = InventoryReport.builder()
                            .id(rs.getString("id"))
                            .employeeId(rs.getString("employeeId"))
                            .date(rs.getDate("date").toLocalDate())
                            .build();
                    arrInventoryReport.add(inventoryReport);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                closeConnection();
            }
        }
        return arrInventoryReport;
    }

    public static boolean addInventoryReport(InventoryReport inventoryReport){
        boolean result = false;
        if(openConnection("InventoryReport")){
            try{
                String sql = "Insert into inventoryreport values(?,?,?)";

                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setString(1,inventoryReport.getId());
                stmt.setString(2,inventoryReport.getEmployeeId());
                stmt.setDate(3, Date.valueOf(inventoryReport.getDate()));

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
