package milktea.milktea.BUS;

import milktea.milktea.DAO.InvoiceDetail_DAO;
import milktea.milktea.DTO.InvoiceDetail;

import java.util.ArrayList;
import java.util.Objects;

public class InvoiceDetail_BUS {
    public ArrayList<InvoiceDetail> listInvoiceDetail = new ArrayList<>();
    public static ArrayList<InvoiceDetail> getAllInvoiceDetail(){
        return InvoiceDetail_DAO.getAllInvoiceDetail();
    }

    public static boolean addInvoiceDetail(ArrayList<InvoiceDetail> invoiceDetails){
        return InvoiceDetail_DAO.addInvoiceDetail(invoiceDetails);
    }

    public void addTempInvoiceDetail(InvoiceDetail invoiceDetail){
        listInvoiceDetail.add(invoiceDetail);
    }
    public void removeTempInvoiceDetail(InvoiceDetail invoiceDetail){
        listInvoiceDetail.remove(invoiceDetail);
    }
    public void editTempInvoiceDetail(InvoiceDetail invoiceDetail){
        for (InvoiceDetail i : listInvoiceDetail) {
            if (Objects.equals(i.getInvoiceId(), invoiceDetail.getInvoiceId())) {
                i.setProductId(invoiceDetail.getProductId());
                i.setQuantity(invoiceDetail.getQuantity());
                i.setUnitPrice(invoiceDetail.getUnitPrice());
                i.setTotalPrice(invoiceDetail.getTotalPrice());
                return;
            }
        }
    }

}
