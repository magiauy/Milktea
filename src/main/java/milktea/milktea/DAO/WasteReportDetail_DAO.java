package milktea.milktea.DAO;

import milktea.milktea.DTO.WasteReportDetail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class WasteReportDetail_DAO extends Connect{
    public static ArrayList<WasteReportDetail> getAllWasteReportDetail() {
        ArrayList<WasteReportDetail> list = new ArrayList<WasteReportDetail>();
        if (openConnection("WasteReportDetail")) {
            try {
                String sql = "Select * from WasteReportDetail";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    WasteReportDetail wasteReportDetail = WasteReportDetail.builder()
                            .id(rs.getString("id"))
                            .inventoryId(rs.getString("inventoryId"))
                            .quantity(rs.getInt("quantity"))
                            .price(rs.getBigDecimal("price"))
                            .total(rs.getBigDecimal("total"))
                            .build();
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                closeConnection();
            }
        }
        return list;
    }
    public static boolean addWasteReportDetail(ArrayList<WasteReportDetail> wasteReportDetails) {
        boolean result = false;
        if (openConnection("WasteReportDetail")) {
            try {
                for (WasteReportDetail wasteReportDetail : wasteReportDetails) {
                    String sql = "Insert into WasteReportDetail values(?,?,?,?,?)";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, wasteReportDetail.getId());
                    stmt.setString(2, wasteReportDetail.getInventoryId());
                    stmt.setDouble(3, wasteReportDetail.getQuantity());
                    stmt.setBigDecimal(4, wasteReportDetail.getPrice());
                    stmt.setBigDecimal(5, wasteReportDetail.getTotal());
                    if (stmt.executeUpdate() >= 1) {
                        result = true;
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                closeConnection();
            }
        }
        return result;
    }
}
