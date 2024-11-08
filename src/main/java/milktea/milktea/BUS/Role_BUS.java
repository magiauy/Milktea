package milktea.milktea.BUS;
import milktea.milktea.DAO.Role_DAO;
public class Role_BUS {
    public static int getAccessById(String id){
        return Role_DAO.getAccessById(id);
    }
}
