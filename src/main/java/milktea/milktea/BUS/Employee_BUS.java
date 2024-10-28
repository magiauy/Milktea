package milktea.milktea.BUS;

import milktea.milktea.DAO.Employee_DAO;
import milktea.milktea.DTO.Employee;
import milktea.milktea.DTO.Status;

import java.util.ArrayList;

public class Employee_BUS {
    public static ArrayList<Employee> getAllEmployee() {
        return Employee_DAO.getAllEmployee();
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
        return Employee_DAO.checkInvalidUsername(username);
    }

    public static boolean checkLogin(String username,String password) {
        return Employee_DAO.checkLogin(username,password);
    }

    public static Employee getEmployeeByUsername(String username) {
        return Employee_DAO.getEmployee(username);
    }
}
