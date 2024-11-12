package milktea.milktea.DAO;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import milktea.milktea.DTO.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


@Slf4j
public class Customer_DAO extends Connect {
    @NonNull
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
                log.error("Error: ", e);
            }finally{
                closeConnection();
            }
        }
        return arrCustomer;
    }

    public static boolean addCustomer(@NonNull Customer customer) {
        String sql1 = "Insert into person values(?,?,?,?,?)";
        String sql2 = "Insert into customer values(?,?)";
        return executeUpdate(sql1, customer.getId(), customer.getFirstName(), customer.getLastName(), customer.getGender().toString(), customer.getPhoneNumber()) &&
               executeUpdate(sql2, customer.getId(), customer.getPoint());
    }

    public static boolean editCustomer(@NonNull Customer customer) {
        String sql1 = "Update person set firstName = ?, lastName = ?, gender = ?, phoneNumber = ? where id = ?";
        String sql2 = "Update customer set point = ? where id = ?";
        return executeUpdate(sql1, customer.getFirstName(), customer.getLastName(), customer.getGender().toString(), customer.getPhoneNumber(), customer.getId()) &&
               executeUpdate(sql2, customer.getPoint(), customer.getId());
    }

    public static boolean deleteCustomer(String id) {
        String sql1 = "Delete from customer where id = ?";
        String sql2 = "Delete from person where id = ?";
        return executeUpdate(sql1, id) && executeUpdate(sql2, id);
    }
}
