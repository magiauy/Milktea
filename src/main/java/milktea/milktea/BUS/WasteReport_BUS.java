package milktea.milktea.BUS;

import milktea.milktea.DAO.WasteReport_DAO;
import milktea.milktea.DTO.WasteReport;

import java.util.ArrayList;
public class WasteReport_BUS {
    public static ArrayList<WasteReport> getAllWasteReport(){
        return WasteReport_DAO.getAllWasteReport();
    }

    public static boolean addWasteReport(WasteReport wasteReport){
        return WasteReport_DAO.addWasteReport(wasteReport);
    }

}
