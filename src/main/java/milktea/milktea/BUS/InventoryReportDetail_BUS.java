package milktea.milktea.BUS;

import milktea.milktea.DAO.InventoryReportDetail_DAO;
import milktea.milktea.DTO.InventoryReportDetail;

import java.util.ArrayList;
public class InventoryReportDetail_BUS {
    public static ArrayList<InventoryReportDetail> getAllInventoryReportDetail(){
        return InventoryReportDetail_DAO.getAllInventoryReportDetail();
    }

    public static boolean addInventoryReportDetail(ArrayList<InventoryReportDetail> inventoryReportDetails){
        return InventoryReportDetail_DAO.addInventoryReportDetail(inventoryReportDetails);
    }


}
