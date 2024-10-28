package milktea.milktea.BUS;

import milktea.milktea.DAO.Customer_DAO;
import milktea.milktea.DTO.Customer;

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

    public static boolean checkAvailablePhone(String phone, String id) {
        return Customer_DAO.checkAvailablePhone(phone,id);
    }
    public static String autoId(){
        return Customer_DAO.autoId();
    }

}
