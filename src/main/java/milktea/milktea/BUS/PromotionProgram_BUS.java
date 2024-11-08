package milktea.milktea.BUS;

import milktea.milktea.DAO.PromotionProgram_DAO;
import milktea.milktea.DTO.PromotionProgram;

import java.time.LocalDate;
import java.util.ArrayList;
public class PromotionProgram_BUS {

    private static ArrayList<PromotionProgram> arrPromotionProgram = new ArrayList<>();

    public static void getLocalData(){
        arrPromotionProgram = PromotionProgram_DAO.getAllPromotionProgram();
    }
    public static ArrayList<PromotionProgram> getAllPromotionProgram(){
        return arrPromotionProgram;
    }

    public static boolean addPromotionProgram(PromotionProgram promotionProgram){
        return PromotionProgram_DAO.addPromotionProgram(promotionProgram);
    }

    public static boolean editPromotionProgram(PromotionProgram promotionProgram){
        return PromotionProgram_DAO.editPromotionProgram(promotionProgram);
    }

    public static boolean checkInvalidDate(String id) {
        for (PromotionProgram program : arrPromotionProgram) {
            if (program.getPromotionProgramId().equals(id)) {
                LocalDate currentDate = LocalDate.now();
                LocalDate startDate = program.getStartDate();
                LocalDate endDate = program.getEndDate();

                System.out.println(currentDate.isBefore(startDate));
                System.out.println(currentDate.isAfter(endDate));
                if (currentDate.isBefore(startDate) || currentDate.isAfter(endDate)) {
                    return false;
                }
                break;
            }
        }
        return true;
    }
}
