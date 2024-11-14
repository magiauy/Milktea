package milktea.milktea.DAO;

import lombok.extern.slf4j.Slf4j;
import milktea.milktea.DTO.Role;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class Role_DAO extends Connect {
    public static ArrayList<Role> getAllRole() {
        ArrayList<Role> arrRole = new ArrayList<>();
        if (openConnection("Role")) {
            try {
                String sql = "Select * from role";

                Statement stmt = connection.createStatement();

                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    Role role = new Role();

                    role.setRoleId(rs.getString("roleId"));
                    role.setRoleName(rs.getString("roleName"));
                    role.setAccess(rs.getInt("access"));

                    arrRole.add(role);
                }

            } catch (SQLException e) {
                log.error("Error: ", e);
            } finally {
                closeConnection();
            }
        }
        return arrRole;
    }
    public static int getAccessById(String id) {
        int access = 0;
        if (openConnection("Role")) {
            try {
                String sql = "Select access from role where roleId = ?";

                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, id);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    access = rs.getInt("access");
                }

            } catch (SQLException e) {
                log.error("Error: ", e);
            } finally {
                closeConnection();
            }
        }
        return access;
    }

    public static boolean updateRole(Role role) {
        if (openConnection("Role")) {
            try {
                String sql = "Update role set roleName = ?, access = ? where roleId = ?";

                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, role.getRoleName());
                stmt.setInt(2, role.getAccess());
                stmt.setString(3, role.getRoleId());

                int rs = stmt.executeUpdate();

                if (rs > 0) {
                    return true;
                }

            } catch (SQLException e) {
                log.error("Error: ", e);
            } finally {
                closeConnection();
            }
        }
        return false;
    }
}