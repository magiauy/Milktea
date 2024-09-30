package milktea.milktea.DAO;

import milktea.milktea.DTO.WasteReport;

import java.sql.*;
import java.util.ArrayList;

public class WasteReport_DAO extends Connect{
    public static ArrayList<WasteReport> getAllWasteReport(){
        ArrayList<WasteReport> arrWasteReport = new ArrayList<>();
        if(openConnection("WasteReport")){
            try{
                String sql = "Select * " +
                        "from WasteReport";

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    WasteReport wasteReport = WasteReport.builder()
                            .id(rs.getString("id"))
                            .employeeId(rs.getString("employeeId"))
                            .date(rs.getDate("date").toLocalDate())
                            .total(rs.getBigDecimal("total"))
                            .build();
                    arrWasteReport.add(wasteReport);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                closeConnection();
            }
        }
        return arrWasteReport;
    }

    public static boolean addWasteReport(WasteReport wasteReport){
        boolean result = false;
        if(openConnection("WasteReport")){
            try{
                String sql = "Insert into WasteReport values(?,?,?,?)";

                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,wasteReport.getId());
                stmt.setString(2,wasteReport.getEmployeeId());
                stmt.setDate(3, Date.valueOf(wasteReport.getDate()));
                stmt.setBigDecimal(4,wasteReport.getTotal());

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
