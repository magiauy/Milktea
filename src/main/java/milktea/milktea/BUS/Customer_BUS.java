package milktea.milktea.BUS;

import milktea.milktea.DAO.Customer_DAO;
import milktea.milktea.DTO.Customer;

import java.math.RoundingMode;
import java.util.ArrayList;

public class Customer_BUS {

    private static ArrayList<Customer> arrCustomer = new ArrayList<>();

    public static void getLocalData() {
        arrCustomer.clear();
        arrCustomer = Customer_DAO.getAllCustomer();
    }
    public static ArrayList<Customer> getAllCustomer() {
        return arrCustomer;
    }

    public static boolean addCustomer(Customer customer) {
        return Customer_DAO.addCustomer(customer);
    }
    public static void addCustomerLocal(Customer customer){
        arrCustomer.add(customer);
    }
    public static boolean editCustomer(Customer customer) {
        return Customer_DAO.editCustomer(customer);
    }

    public static boolean checkAvailablePhone(String phone, String id) {
        for (Customer customer : arrCustomer) {
            if (customer.getPhoneNumber().equals(phone) && !customer.getId().equals(id)) {
                return true;
            }
        }return false;
    }
    public static String getPointByID(String id){
        for(Customer c: arrCustomer){
            if (c.getId().equals(id)){
                return c.getPoint().setScale(0, RoundingMode.HALF_UP).toString();
            }
        }
        return null;
    }
    public static String autoId(){
        if (arrCustomer.isEmpty()) {
            return "KH0001";
        }
        String lastId = arrCustomer.getLast().getId();
        int id = Integer.parseInt(lastId.substring(2)) + 1;
        return "KH" + String.format("%03d", id);
    }

    public static void editCustomerLocal(Customer customer) {
        for (Customer c : arrCustomer) {
            if (c.getId().equals(customer.getId())) {
                c.setFirstName(customer.getFirstName());
                c.setLastName(customer.getLastName());
                c.setGender(customer.getGender());
                c.setPhoneNumber(customer.getPhoneNumber());
            }
        }
    }

    public static Customer getCustomerById(String id) {
        for (Customer c : arrCustomer) {
            if (c.getId().equals(id)) {
                return c;
            }
        }return null;
    }

    public static boolean deleteCustomer(String id) {
        return Customer_DAO.deleteCustomer(id);
    }

    public static void deleteCustomerLocal(String id) {
        arrCustomer.removeIf(c -> c.getId().equals(id));
    }

    public static ArrayList<Customer> searchCustomer(String search) {
        ArrayList<Customer> arr = new ArrayList<>();
        for (Customer customer : arrCustomer) {
            if (customer.getId().contains(search) || customer.getFirstName().contains(search) || customer.getLastName().contains(search) || customer.getPhoneNumber().contains(search)) {
                arr.add(customer);
            }
        }
        return arr;
    }
}
