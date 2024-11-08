package milktea.milktea.BUS;

import milktea.milktea.DAO.Employee_DAO;
import milktea.milktea.DTO.Employee;
import milktea.milktea.DTO.Status;

import java.util.ArrayList;
public class Employee_BUS {

    private static ArrayList<Employee> arrEmployee = new ArrayList<>();

    public static void getLocalData() {
        arrEmployee = Employee_DAO.getAllEmployee();
    }
    public static ArrayList<Employee> getAllEmployee() {
        return arrEmployee;
    }

    public static boolean addEmployee(Employee employee) {
        return Employee_DAO.addEmployee(employee);
    }

    public static boolean editEmployee(Employee employee) {
        return Employee_DAO.editEmployee(employee);
    }

    public static boolean updateStatus(String id , Status status) {
        return Employee_DAO.updateStatus(id,status);
    }

    public static boolean checkInvalidUsername(String username) {
        for (Employee employee : arrEmployee) {
            if (employee.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkLogin(String username,String password) {
        for (Employee employee : arrEmployee) {
            if (employee.getUsername().equals(username) && employee.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public static Employee getEmployeeByUsername(String username) {
        for (Employee employee : arrEmployee) {
            if (employee.getUsername().equals(username)) {
                return employee;
            }
        }
        return null;
    }
}
