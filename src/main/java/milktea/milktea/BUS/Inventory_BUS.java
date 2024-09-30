package milktea.milktea.BUS;

import milktea.milktea.DAO.Inventory_DAO;
import milktea.milktea.DTO.Inventory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class Inventory_BUS {
    public static ArrayList<Inventory> getAllInventory(){
        return Inventory_DAO.getAllInventory();
    }

    public static boolean addInventory(Inventory inventory){
        return Inventory_DAO.addInventory(inventory);
    }

    public static boolean editInventory(Inventory inventory){
        return Inventory_DAO.editInventory(inventory);
    }

    public static boolean clearInventory(Inventory inventory){
        return Inventory_DAO.clearInventory(inventory);
    }
}
