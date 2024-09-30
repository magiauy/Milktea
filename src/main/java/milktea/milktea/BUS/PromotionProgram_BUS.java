package milktea.milktea.BUS;

import milktea.milktea.DAO.PromotionProgram_DAO;
import milktea.milktea.DTO.PromotionProgram;

import java.util.ArrayList;
public class PromotionProgram_BUS {
    public static ArrayList<PromotionProgram> getAllPromotionProgram(){
        return PromotionProgram_DAO.getAllPromotionProgram();
    }

    public static boolean addPromotionProgram(PromotionProgram promotionProgram){
        return PromotionProgram_DAO.addPromotionProgram(promotionProgram);
    }

    public static boolean editPromotionProgram(PromotionProgram promotionProgram){
        return PromotionProgram_DAO.editPromotionProgram(promotionProgram);
    }
}
