package milktea.milktea.BUS;

import milktea.milktea.DAO.Customer_DAO;
import milktea.milktea.DTO.Customer;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Customer_BUS {
    public static ArrayList<Customer> getAllCustomer() {
        return Customer_DAO.getAllCustomer();
    }

    public static boolean addCustomer(Customer customer) {
        return Customer_DAO.addCustomer(customer);
    }

    public static boolean editCustomer(Customer customer) {
        return Customer_DAO.editCustomer(customer);
    }


}
