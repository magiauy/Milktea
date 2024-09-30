package milktea.milktea.DAO;

import milktea.milktea.DTO.Invoice;

import java.sql.*;
import java.util.ArrayList;

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
                e.printStackTrace();
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
                e.printStackTrace();
            }finally{
                closeConnection();
            }
        }
        return result;
    }
}
