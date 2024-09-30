package milktea.milktea.BUS;

import milktea.milktea.DAO.Provider_DAO;
import milktea.milktea.DTO.Provider;

import java.util.ArrayList;
public class Provider_BUS {
    public static ArrayList<Provider> getAllProvider(){
        return Provider_DAO.getAllProvider();
    }

    public static boolean addProvider(Provider provider){
        return Provider_DAO.addProvider(provider);
    }

    public static boolean editProvider(Provider provider){
        return Provider_DAO.editProvider(provider);
    }

}
