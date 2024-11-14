package milktea.milktea.BUS;

import milktea.milktea.DAO.Provider_DAO;
import milktea.milktea.DTO.Provider;

import java.util.ArrayList;
public class Provider_BUS {

    private static ArrayList<Provider> arrProvider = new ArrayList<>();

    public static void getLocalData(){
        arrProvider.clear();
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


    public static boolean deleteProvider(String id) {
        return Provider_DAO.deleteProvider(id);

    }

    public static void deleteProviderLocal(String id) {
        arrProvider.removeIf(provider -> provider.getId().equals(id));
    }

    public static String autoID() {
        if (arrProvider.isEmpty()) {
            return "PR001";
        }else {
            int max = 0;
            for (Provider provider : arrProvider){
                int id = Integer.parseInt(provider.getId().substring(2));
                if (id > max){
                    max = id;
                }
            }
            return "PR" + String.format("%03d", max + 1);
        }
    }

    public static void addProviderLocal(Provider provider) {
        arrProvider.add(provider);
    }

    public static void editProviderLocal(Provider provider) {
        for (Provider provider1 : arrProvider){
            if (provider1.getId().equals(provider.getId())){
                provider1.setName(provider.getName());
                provider1.setAddress(provider.getAddress());
                provider1.setPhone(provider.getPhone());
                provider1.setEmail(provider.getEmail());
            }
        }
    }
}
