package milktea.milktea.BUS;

import milktea.milktea.DAO.WasteReportDetail_DAO;
import milktea.milktea.DTO.WasteReportDetail;

import java.util.ArrayList;
public class WasteReportDetail_BUS {
    public static ArrayList<WasteReportDetail> getAllWasteReportDetail(){
        return WasteReportDetail_DAO.getAllWasteReportDetail();
    }

    public static boolean addWasteReportDetail(ArrayList<WasteReportDetail> wasteReportDetails){
        return WasteReportDetail_DAO.addWasteReportDetail(wasteReportDetails);
    }
}
