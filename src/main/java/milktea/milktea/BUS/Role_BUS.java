package milktea.milktea.BUS;
import milktea.milktea.DAO.Role_DAO;
import milktea.milktea.DTO.Role;

import java.util.ArrayList;

public class Role_BUS {

    private static ArrayList<Role> arrRole = new ArrayList<>();

    public static void getLocalData(){
        arrRole = Role_DAO.getAllRole();
    }

    public static ArrayList<Role> getAllRole(){
        return arrRole;
    }

    public static int getAccessById(String id){
        return Role_DAO.getAccessById(id);
    }

    public static Role getRoleByID(String role) {
        for (Role r : arrRole) {
            if (r.getRoleId().equals(role)) {
                return r;
            }
        }
        return null;
    }

    public static boolean updateRole(Role role) {
        return Role_DAO.updateRole(role);
    }

    public static void updateRoleLocal(Role role) {
        for (Role r : arrRole) {
            if (r.getRoleId().equals(role.getRoleId())) {
                r.setRoleName(role.getRoleName());
                r.setAccess(role.getAccess());
            }
        }
    }
}
