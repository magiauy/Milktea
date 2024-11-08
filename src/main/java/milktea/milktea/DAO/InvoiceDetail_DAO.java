package milktea.milktea.DAO;

import lombok.extern.slf4j.Slf4j;
import milktea.milktea.DTO.InvoiceDetail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@Slf4j
public class InvoiceDetail_DAO extends Connect{
    public static ArrayList<InvoiceDetail> getAllInvoiceDetail() {
        ArrayList<InvoiceDetail> list = new ArrayList<>();
        if (openConnection("InvoiceDetail")) {
            try {
                String sql = "Select * from InvoiceDetail";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    InvoiceDetail invoiceDetail = InvoiceDetail.builder()
                            .invoiceId(rs.getString("invoiceId"))
                            .productId(rs.getString("productId"))
                            .quantity(rs.getInt("quantity"))
                            .unitPrice(rs.getBigDecimal("unitPrice"))
                            .totalPrice(rs.getBigDecimal("totalPrice"))
                            .build();
                    list.add(invoiceDetail);
                }
            } catch (SQLException e) {
                log.error("e: ", e);
            } finally {
                closeConnection();
            }
        }
        return list;
    }
    public static boolean addInvoiceDetail(ArrayList<InvoiceDetail> invoiceDetails) {
        boolean result = false;
        if (openConnection("InvoiceDetail")) {
            try {
                for (InvoiceDetail invoiceDetail : invoiceDetails) {
                    String sql = "Insert into InvoiceDetail values(?,?,?,?,?)";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, invoiceDetail.getInvoiceId());
                    stmt.setString(2, invoiceDetail.getProductId());
                    stmt.setInt(3, invoiceDetail.getQuantity());
                    stmt.setBigDecimal(4, invoiceDetail.getUnitPrice());
                    stmt.setBigDecimal(5, invoiceDetail.getTotalPrice());
                    if (stmt.executeUpdate() >= 1) {
                        result = true;
                    }
                }
            } catch (SQLException e) {
                log.error("e: ", e);
            } finally {
                closeConnection();
            }
        }
        return result;
    }

    public static void removeInvoiceDetail(String invoiceId) {
        try {
            String checkSql = "SELECT COUNT(*) FROM invoicedetail WHERE invoiceId = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setString(1, invoiceId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                String sql = "DELETE FROM invoicedetail WHERE invoiceId = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, invoiceId);
            }
        } catch (SQLException e) {
            log.error("Error: ", e);
        } finally {
            closeConnection();
        }
    }
}
