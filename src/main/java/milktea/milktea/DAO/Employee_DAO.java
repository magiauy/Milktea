package milktea.milktea.DAO;

import lombok.extern.slf4j.Slf4j;
import milktea.milktea.DTO.Employee;
import milktea.milktea.DTO.Status;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
@Slf4j
public class Employee_DAO extends Connect {
    public static ArrayList<Employee> getAllEmployee() {
        ArrayList<Employee> arrEmployee = new ArrayList<>();
        if (openConnection("Employee")) {
            try {
                String sql = "Select emp.id , per.firstName, per.lastName, per.gender, per.phoneNumber , emp.username, emp.password, emp.roleId , emp.status " +
                        "from employee emp " +
                        "join person per on emp.id = per.id";

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Employee employee = Employee.builder()
                            .id(rs.getString("id"))
                            .firstName(rs.getString("firstName"))
                            .lastName(rs.getString("lastName"))
                            .gender(getGender(rs.getString("gender")))
                            .phoneNumber(rs.getString("phoneNumber"))
                            .username(rs.getString("username"))
                            .password(rs.getString("password"))
                            .role(rs.getString("roleId"))
                            .status(getStatus(rs.getString("status")))
                            .build();
                    arrEmployee.add(employee);
                }
            } catch (SQLException e) {
                log.error("Error: ", e);
            } finally {
                closeConnection();
            }

        }
        return arrEmployee;
    }

    public static boolean addEmployee(Employee employee) {
        boolean result = false;
        if (openConnection("Employee")) {
            try {
                String sql = "Insert into person values(?,?,?,?,?)";
                String sql2 = "Insert into employee values(?,?,?,?,?)";

                PreparedStatement stmt = connection.prepareStatement(sql);
                PreparedStatement stmt2 = connection.prepareStatement(sql2);

                stmt.setString(1, employee.getId());
                stmt.setString(2, employee.getFirstName());
                stmt.setString(3, employee.getLastName());
                stmt.setString(4, employee.getGender().toString());
                stmt.setString(5, employee.getPhoneNumber());

                stmt2.setString(1, employee.getId());
                stmt2.setString(2, employee.getUsername());
                stmt2.setString(3, employee.getPassword());
                stmt2.setString(4, employee.getRole());
                stmt2.setString(5, employee.getStatus().toString());

                if (stmt.executeUpdate() >= 1 && stmt2.executeUpdate() >= 1) {
                    result = true;
                }
            } catch (SQLException e) {
                log.error("Error: ", e);
            } finally {
                closeConnection();
            }
        }
        return result;
    }

public static boolean editEmployee(Employee employee) {
    boolean result = false;
    if (openConnection("Employee")) {
        try {
            String sql = "Update person set firstName = ?, lastName = ?, gender = ?, phoneNumber = ? where id = ?";
            String sql2 = "Update employee set username = ?, password = ?, roleId = ?, status = ? where id = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            PreparedStatement stmt2 = connection.prepareStatement(sql2);

            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getGender().toString());
            stmt.setString(4, employee.getPhoneNumber());
            stmt.setString(5, employee.getId());

            stmt2.setString(1, employee.getUsername());
            stmt2.setString(2, employee.getPassword());
            stmt2.setString(3, employee.getRole());
            stmt2.setString(4, employee.getStatus().toString());
            stmt2.setString(5, employee.getId());

            if (stmt.executeUpdate() >= 1 && stmt2.executeUpdate() >= 1) {
                result = true;
            }
        } catch (SQLException e) {
            log.error("Error: ", e);
        } finally {
            closeConnection();
        }
    }
    return result;
}

public static boolean updateStatus(String id, Status status) {
    boolean result = false;
    if (openConnection("Employee")) {
        try {
            String sql = "Update employee set status = ? where id = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, status.toString());
            stmt.setString(2, id);

            if (stmt.executeUpdate() >= 1) {
                result = true;
            }

        } catch (SQLException e) {
            log.error("Error: ", e);
        } finally {
            closeConnection();

        }
    }
    return result;
    }

}