package milktea.milktea.BUS;

import milktea.milktea.DAO.Promotion_DAO;
import milktea.milktea.DTO.Promotion;

import java.util.ArrayList;
public class Promotion_BUS {
    private static ArrayList<Promotion> arrPromotion = new ArrayList<>();

    public static void getLocalData(){
        arrPromotion.clear();
        arrPromotion = Promotion_DAO.getAllPromotion();
    }
    public static ArrayList<Promotion> getAllPromotion(){
        return arrPromotion;
    }

    public static boolean addPromotion(Promotion promotion){
        return Promotion_DAO.addPromotion(promotion);
    }

    public static boolean addPromotion(ArrayList<Promotion> promotions) {
        if (!promotions.isEmpty()) {
                for (Promotion promotion : promotions) {
                    if (!addPromotion(promotion)) {
                        return false;
                    }
                }
        }
        return true;
    }

    public static boolean editPromotion(Promotion promotion){
        return Promotion_DAO.editPromotion(promotion);
    }

    public static boolean editPromotion(ArrayList<Promotion> promotions) {
        if (!promotions.isEmpty()) {
            {
                for (Promotion promotion : promotions) {
                    if (!editPromotion(promotion)) {
                        return false;
                    }
                }
            }
        }
        return true;

    }

    public static ArrayList<Promotion> getPromotionsByID(String promotionProgramId) {
        ArrayList<Promotion> result = new ArrayList<>();
        for (Promotion promotion : arrPromotion){
            if (promotion.getPromotionProgramId().equals(promotionProgramId)){
                result.add(promotion);
            }
        }
        return result;
    }

    public static boolean removePromotion(ArrayList<Promotion> removedPromotions) {
        if (!removedPromotions.isEmpty()) {
            return Promotion_DAO.removePromotion(removedPromotions);
        }
        return true;
    }

    public static void removePromotionLocal(ArrayList<Promotion> removedPromotions) {
        arrPromotion.removeAll(removedPromotions);
    }

    public static void editPromotionLocal(ArrayList<Promotion> currentPromotions) {
        for (Promotion promotion : currentPromotions){
            for (Promotion promotion1 : arrPromotion){
                if (promotion.getPromotionId().equals(promotion1.getPromotionId())){
                    promotion1.setDiscount(promotion.getDiscount());
                    promotion1.setMinimumPrice(promotion.getMinimumPrice());
                    break;
                }
            }
        }
    }

    public static void addPromotionLocal(ArrayList<Promotion> addedPromotions) {
        arrPromotion.addAll(addedPromotions);
    }
}
