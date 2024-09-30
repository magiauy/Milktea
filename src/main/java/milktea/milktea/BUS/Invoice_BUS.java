package milktea.milktea.BUS;

import milktea.milktea.DAO.Invoice_DAO;
import milktea.milktea.DTO.Invoice;

import java.util.ArrayList;
public class Invoice_BUS {
    public static ArrayList<Invoice> getAllInvoice(){
        return Invoice_DAO.getAllInvoice();
    }

    public static boolean addInvoice(Invoice invoice){
        return Invoice_DAO.addInvoice(invoice);
    }




}
