package milktea.milktea.DAO;

import milktea.milktea.DTO.InventoryReportDetail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class InventoryReportDetail_DAO extends Connect{
    public static ArrayList<InventoryReportDetail> getAllInventoryReportDetail(){
        ArrayList<InventoryReportDetail> arrInventoryReportDetail = new ArrayList<>();
        if(openConnection("InventoryReportDetail")){
            try{
                String sql = "Select * " +
                        "from inventoryreportdetail ird";

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    InventoryReportDetail inventoryReportDetail = InventoryReportDetail.builder()
                            .id(rs.getString("id"))
                            .inventoryId(rs.getString("inventoryId"))
                            .quantity(rs.getDouble("quantity"))
                            .build();
                    arrInventoryReportDetail.add(inventoryReportDetail);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                closeConnection();
            }
        }
        return arrInventoryReportDetail;
    }
    public static boolean addInventoryReportDetail(ArrayList<InventoryReportDetail> inventoryReportDetails){
        boolean result = false;
        if(openConnection("InventoryReportDetail")){
            try{

                for(InventoryReportDetail inventoryReportDetail:inventoryReportDetails){
                    String sql = "Insert into inventoryreportdetail values(?,?,?)";
                PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1,inventoryReportDetail.getId());
                    stmt.setString(2,inventoryReportDetail.getInventoryId());
                    stmt.setDouble(3,inventoryReportDetail.getQuantity());

                    if(stmt.executeUpdate()>=1){
                        result = true;
                    }
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
