package milktea.milktea.DAO;

import lombok.extern.slf4j.Slf4j;
import milktea.milktea.DTO.Invoice;

import java.sql.*;
import java.util.ArrayList;
@Slf4j
public class Invoice_DAO extends Connect{
    public static ArrayList<Invoice> getAllInvoice(){
        ArrayList<Invoice> arrInvoice = new ArrayList<>();
        if(openConnection("Invoice")){
            try{
                String sql = "Select * " +
                        "from invoice";

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    Invoice invoice = Invoice.builder()
                            .invoiceId(rs.getString("invoiceId"))
                            .customerId(rs.getString("customerId"))
                            .employeeId(rs.getString("employeeId"))
                            .promotionId(rs.getString("promotionId"))
                            .issueDate(rs.getDate("issueDate").toLocalDate())
                            .discount(rs.getBigDecimal("discount"))
                            .total(rs.getBigDecimal("total"))
                            .build();
                    arrInvoice.add(invoice);
                }
            }catch(SQLException e){
                log.error("Error: ", e);
            }finally{
                closeConnection();
            }
        }
        return arrInvoice;
    }
    public static boolean addInvoice(Invoice invoice){
        boolean result = false;
        if(openConnection("Invoice")){
            try{
                String sql = "Insert into invoice values(?,?,?,?,?,?,?)";

                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setString(1,invoice.getInvoiceId());
                stmt.setString(2,invoice.getCustomerId());
                stmt.setString(3,invoice.getEmployeeId());
                stmt.setString(4,invoice.getPromotionId());
                stmt.setDate(5, Date.valueOf(invoice.getIssueDate()));
                stmt.setBigDecimal(6,invoice.getDiscount());
                stmt.setBigDecimal(7,invoice.getTotal());

                if(stmt.executeUpdate()>=1){
                    result = true;
                }

            }catch(SQLException e){
                log.error("Error: ", e);
            }finally{
                closeConnection();
            }
        }
        return result;
    }

    public static void removeInvoice(String id) {
        if (openConnection()) {
            try {
                String checkSql = "SELECT COUNT(*) FROM invoice WHERE invoiceId = ?";
                PreparedStatement checkStmt = connection.prepareStatement(checkSql);
                checkStmt.setString(1, id);
                ResultSet rs = checkStmt.executeQuery();
                while (rs.next() && rs.getInt(1) > 0) {
                    String sql = "DELETE FROM invoice WHERE invoiceId = ?";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, id);
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                log.error("Error: ", e);
            } finally {
                closeConnection();
            }
        }
    }

}
