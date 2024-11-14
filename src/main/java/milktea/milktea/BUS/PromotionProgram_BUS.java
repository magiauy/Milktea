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

    public static void editPromotionProgramLocal(PromotionProgram promotionProgram) {
        for (PromotionProgram program : arrPromotionProgram) {
            if (program.getPromotionProgramId().equals(promotionProgram.getPromotionProgramId())) {
                program.setName(promotionProgram.getName());
                program.setStartDate(promotionProgram.getStartDate());
                program.setEndDate(promotionProgram.getEndDate());
                break;
            }
        }
    }

    public static void addPromotionProgramLocal(PromotionProgram promotionProgram) {
        arrPromotionProgram.add(promotionProgram);
    }

    public static String autoID(){
        if (arrPromotionProgram.isEmpty()) return "CTKM001";
        String lastID = arrPromotionProgram.getLast().getPromotionProgramId();
        int number = Integer.parseInt(lastID.substring(4));
        return "CTKM" + String.format("%04d", number + 1);

    }

    public static boolean deletePromotionProgram(String promotionProgramId) {
        return PromotionProgram_DAO.deletePromotionProgram(promotionProgramId);
    }

    public static void removePromotionProgramLocal(String promotionProgramId) {
        arrPromotionProgram.removeIf(promotionProgram -> promotionProgram.getPromotionProgramId().equals(promotionProgramId));
    }
}
