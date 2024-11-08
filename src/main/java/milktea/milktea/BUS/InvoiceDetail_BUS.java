package milktea.milktea.BUS;

import milktea.milktea.DAO.InvoiceDetail_DAO;
import milktea.milktea.DTO.InvoiceDetail;

import java.util.ArrayList;
import java.util.Objects;

public class InvoiceDetail_BUS {
    private static ArrayList<InvoiceDetail> arrInvoiceDetail = new ArrayList<>();

    public static void getLocalData(){
        arrInvoiceDetail = InvoiceDetail_DAO.getAllInvoiceDetail();
    }
    public static boolean addInvoiceDetail(ArrayList<InvoiceDetail> invoiceDetails){
        return InvoiceDetail_DAO.addInvoiceDetail(invoiceDetails);
    }

    public static ArrayList<InvoiceDetail> getInvoiceDetailByInvoiceId(String invoiceId){
        ArrayList<InvoiceDetail> arr = new ArrayList<>();
        for (InvoiceDetail invoiceDetail : arrInvoiceDetail){
            if (Objects.equals(invoiceDetail.getInvoiceId(), invoiceId)){
                arr.add(invoiceDetail);
            }
        }
        return arr;
    }

}
