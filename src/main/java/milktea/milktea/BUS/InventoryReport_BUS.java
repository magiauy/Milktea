package milktea.milktea.BUS;

import milktea.milktea.DAO.InventoryReport_DAO;
import milktea.milktea.DTO.InventoryReport;

import java.util.ArrayList;
public class InventoryReport_BUS {
    public static ArrayList<InventoryReport> getAllInventoryReport(){
        return InventoryReport_DAO.getAllInventoryReport();
    }

    public static boolean addInventoryReport(InventoryReport inventoryReport){
        return InventoryReport_DAO.addInventoryReport(inventoryReport);
    }

}
