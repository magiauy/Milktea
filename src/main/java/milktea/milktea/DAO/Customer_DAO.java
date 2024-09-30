package milktea.milktea.DAO;


import milktea.milktea.DTO.Customer;
import milktea.milktea.DTO.Gender;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;


public class Customer_DAO extends Connect {
    public static ArrayList<Customer> getAllCustomer(){
        ArrayList<Customer> arrCustomer = new ArrayList<>();
        if(openConnection("Customer")){
            try{
                String sql = "Select cus.id , per.firstName ,per.lastName,per.gender,per.phoneNumber, cus.point "+
                        "from customer cus "+
                        "join person per on cus.id = per.id"
                        ;

                Statement stmt = connection.createStatement();

                ResultSet rs = stmt.executeQuery(sql);

                while(rs.next()){
                    Customer customer = Customer.builder()
                            .id(rs.getString("id"))
                            .firstName(rs.getString("firstName"))
                            .lastName(rs.getString("lastName"))
                            .gender(getGender(rs.getString("gender")))
                            .phoneNumber(rs.getString("phoneNumber"))
                            .point(rs.getBigDecimal("point"))
                            .build();

                    arrCustomer.add(customer);
                }

            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                closeConnection();
            }
        }
        return arrCustomer;
    }

    public static boolean addCustomer(Customer customer){
        boolean result = false;
        if(openConnection("Customer")){
            try{
                String sql = "Insert into person values(?,?,?,?,?)";
                String sql2 = "Insert into customer values(?,?)";

                PreparedStatement stmt = connection.prepareStatement(sql);
                PreparedStatement stmt2 = connection.prepareStatement(sql2);

                stmt.setString(1,customer.getId());
                stmt.setString(2,customer.getFirstName());
                stmt.setString(3,customer.getLastName());
                stmt.setString(4,customer.getGender().toString());
                stmt.setString(5,customer.getPhoneNumber());

                stmt2.setString(1,customer.getId());
                stmt2.setBigDecimal(2,customer.getPoint());

                if(stmt.executeUpdate()>=1 && stmt2.executeUpdate()>=1){
                    result = true;
                }

            }catch(SQLException e){

                System.out.println(e);
            }finally{
                closeConnection();
            }
        }
        return result;
    }

    public static boolean updatePoint(String id, BigDecimal point){
        boolean result = false;
        if(openConnection("Customer")){
            try{
                String sql = "Update customer set point = ? where id = ?";

                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setBigDecimal(1,point);
                stmt.setString(2,id);

                if(stmt.executeUpdate()>=1){
                    result = true;
                }

            }catch(SQLException e){
                e.printStackTrace();
                System.out.println(e);
            }finally{
                closeConnection();
            }
        }
        return result;
    }

    public static boolean editCustomer(Customer customer){
        boolean result = false;
            if (openConnection("Customer")) {
                try {
                    String sql = "Update person set firstName = ?, lastName = ?,gender = ?, phoneNumber = ? where id = ?";
                    String sql2 = "Update customer set point = ? where id = ?";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    PreparedStatement stmt2 = connection.prepareStatement(sql2);

                    stmt.setString(1, customer.getFirstName());
                    stmt.setString(2, customer.getLastName());
                    stmt.setString(3, customer.getGender().toString());
                    stmt.setString(4, customer.getPhoneNumber());
                    stmt.setString(5, customer.getId());

                    stmt2.setBigDecimal(1, customer.getPoint());
                    stmt2.setString(2, customer.getId());

                    if (stmt.executeUpdate() >= 1 && stmt2.executeUpdate() >= 1) {
                        result = true;
                    }
                }catch (SQLException e){
                    System.out.println(e);
                }finally {
                    closeConnection();
                }
                }
                return result;
    }

}
