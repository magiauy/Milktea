package milktea.milktea.BUS;

import milktea.milktea.DAO.Employee_DAO;
import milktea.milktea.DTO.Employee;
import milktea.milktea.DTO.Status;

import java.util.ArrayList;
public class Employee_BUS {

    private static ArrayList<Employee> arrEmployee = new ArrayList<>();

    public static void getLocalData() {
        arrEmployee.clear();
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

    public static boolean checkAvailableUsername(String username) {
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

    public static ArrayList<Employee> searchEmployee(String text) {
        ArrayList<Employee> arrSearch = new ArrayList<>();
        for (Employee employee : arrEmployee) {
            String fullName = employee.getFirstName() + " " + employee.getLastName();
            if (employee.getId().equals(text) || employee.getFirstName().equals(text) || employee.getLastName().equals(text) || employee.getPhoneNumber().equals(text)
            ||fullName.equals(text)) {
                arrSearch.add(employee);
            }
        }
        return arrSearch;
    }

    public static boolean deleteEmployee(String id) {
        return Employee_DAO.deleteEmployee(id);
    }

    public static void callBackIfFail(Employee employee) {
        Employee_DAO.callBackIfFail(employee);
    }

    public static String autoID() {
        if (arrEmployee.isEmpty()) {
            return "NV001";
        } else {
            String lastId = arrEmployee.getLast().getId();
            int number = Integer.parseInt(lastId.substring(2));
            return "NV" + String.format("%03d", number + 1);
        }
    }

    public static boolean checkAvailablePhone(String phone, String id) {
        for (Employee employee : arrEmployee) {
            if (employee.getPhoneNumber().equals(phone) && !employee.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public static void editEmployeeLocal(Employee employee) {
        for (Employee employee1 : arrEmployee) {
            if (employee1.getId().equals(employee.getId())) {
                employee1.setFirstName(employee.getFirstName());
                employee1.setLastName(employee.getLastName());
                employee1.setPhoneNumber(employee.getPhoneNumber());
                employee1.setRole(employee.getRole());
                employee1.setGender(employee.getGender());
            }
        }
    }

    public static void addEmployeeLocal(Employee employee) {
        arrEmployee.add(employee);
    }

    public static boolean updateStatusLocal(String id, Status newStatus) {
        for (Employee employee : arrEmployee) {
            if (employee.getId().equals(id)) {
                employee.setStatus(newStatus);
                return true;
            }
        }
        return false;
    }

    public static boolean getStatus(String text) {
        for (Employee employee : arrEmployee) {
            if (employee.getUsername().equals(text)) {
                return employee.getStatus().equals(Status.ACTIVE);
            }
        }
        return false;
    }
}
