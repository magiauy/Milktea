package milktea.milktea.BUS;

import milktea.milktea.DAO.Provider_DAO;
import milktea.milktea.DTO.Provider;

import java.util.ArrayList;
public class Provider_BUS {

    private static ArrayList<Provider> arrProvider = new ArrayList<>();

    public static void getLocalData(){
        arrProvider = Provider_DAO.getAllProvider();
    }
    public static ArrayList<Provider> getAllProvider(){
        return arrProvider;
    }

    public static boolean addProvider(Provider provider){
        return Provider_DAO.addProvider(provider);
    }

    public static boolean editProvider(Provider provider){
        return Provider_DAO.editProvider(provider);
    }

    public static ArrayList<Provider> searchProvider(String search){
        ArrayList<Provider> result = new ArrayList<>();
        for (Provider provider : arrProvider){
            if (provider.getName().toLowerCase().contains(search.toLowerCase())
                    ||provider.getPhone().toLowerCase().contains(search.toLowerCase())
                    ||provider.getEmail().toLowerCase().contains(search.toLowerCase())
                    ||provider.getId().toLowerCase().contains(search.toLowerCase())){
                result.add(provider);
            }
        }
        return result;
    }



}
