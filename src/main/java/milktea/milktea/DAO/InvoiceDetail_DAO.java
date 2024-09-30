package milktea.milktea.DAO;

import milktea.milktea.DTO.InvoiceDetail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class InvoiceDetail_DAO extends Connect{
    public static ArrayList<InvoiceDetail> getAllInvoiceDetail() {
        ArrayList<InvoiceDetail> list = new ArrayList<InvoiceDetail>();
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
                }
            } catch (Exception e) {
                System.out.println(e);
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
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                closeConnection();
            }
        }
        return result;
    }
}
