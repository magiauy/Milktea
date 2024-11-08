package milktea.milktea.BUS;

import milktea.milktea.DAO.Promotion_DAO;
import milktea.milktea.DTO.Promotion;

import java.util.ArrayList;
public class Promotion_BUS {
    private static ArrayList<Promotion> arrPromotion = new ArrayList<>();

    public static void getLocalData(){
        arrPromotion = Promotion_DAO.getAllPromotion();
    }
    public static ArrayList<Promotion> getAllPromotion(){
        return arrPromotion;
    }

    public static boolean addPromotion(Promotion promotion){
        return Promotion_DAO.addPromotion(promotion);
    }

    public static boolean editPromotion(Promotion promotion){
        return Promotion_DAO.editPromotion(promotion);
    }


}
