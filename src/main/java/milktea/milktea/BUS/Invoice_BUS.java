package milktea.milktea.BUS;

import lombok.NonNull;
import milktea.milktea.DAO.InvoiceDetail_DAO;
import milktea.milktea.DAO.Invoice_DAO;
import milktea.milktea.DTO.Invoice;
import milktea.milktea.DTO.Promotion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Invoice_BUS {
    private static ArrayList<Invoice> arrInvoice = new ArrayList<>();
    public static void getLocalData(){
        arrInvoice.clear();
        arrInvoice = Invoice_DAO.getAllInvoice();
    }
    public static ArrayList<Invoice> getAllInvoice(){
        return arrInvoice;
    }

    public static boolean addInvoice(Invoice invoice){
        return Invoice_DAO.addInvoice(invoice);
    }
    public static void addInvoiceLocal(Invoice invoice){
        arrInvoice.add(invoice);
    }
    public static void removeInvoice(String id){
        Invoice_DAO.removeInvoice(id);
    }

    @NonNull
    public static String autoId(){
        if (arrInvoice.isEmpty()){
            return "HD0001";
        }
        String lastId = arrInvoice.getLast().getInvoiceId();
        int id = Integer.parseInt(lastId.substring(2)) + 1;
        return "HD" + String.format("%04d", id);
    }


    public static void removeInvoiceDetail(String invoiceId) {
        InvoiceDetail_DAO.removeInvoiceDetail(invoiceId);
    }

    public static ArrayList<Invoice> searchInvoice(String search){
        ArrayList<Invoice> arr = new ArrayList<>();
        for (Invoice invoice : arrInvoice){
            if (invoice.getInvoiceId().contains(search) || invoice.getCustomerId().contains(search) || invoice.getEmployeeId().contains(search)){
                arr.add(invoice);
            }
        }
        return arr;
    }

public static ArrayList<Invoice> advancedSearchInvoice(HashMap<String, String> search) {
    ArrayList<Invoice> result = new ArrayList<>();
    for (Invoice invoice : arrInvoice) {
        boolean matches = true;

        // Date range filter
        if (search.containsKey("startDate") && search.containsKey("endDate")) {
            System.out.println(search.get("startDate"));
            LocalDate startDate = LocalDate.parse(search.get("startDate"));
            LocalDate endDate = LocalDate.parse(search.get("endDate"));
            LocalDate issueDate = invoice.getIssueDate();
            if (issueDate.isBefore(startDate) || issueDate.isAfter(endDate)) {
                matches = false;
            }
        }

        // Total amount range filter
        if (search.containsKey("minTotal") && search.containsKey("maxTotal")) {
            BigDecimal minTotal = new BigDecimal(search.get("minTotal"));
            BigDecimal maxTotal = new BigDecimal(search.get("maxTotal"));
            BigDecimal total = invoice.getTotal();
            if (total.compareTo(minTotal) < 0 || total.compareTo(maxTotal) > 0) {
                matches = false;
            }
        }

        // Customer ID filter
        if (search.containsKey("customerId")) {
            String customerId = search.get("customerId");
            if (!invoice.getCustomerId().equals(customerId)) {
                matches = false;
            }
        }

        // Employee ID filter
        if (search.containsKey("employeeId")) {
            String employeeId = search.get("employeeId");
            if (!invoice.getEmployeeId().equals(employeeId)) {
                matches = false;
            }
        }

        //Promotion ID filter
        if (search.containsKey("promotionId")) {
            String promotionId = search.get("promotionId");
            if (!invoice.getPromotionId().equals(promotionId)) {
                matches = false;
            }
        }


        if (matches) {
            result.add(invoice);
        }
    }
    return result;
}
        public static boolean isCustomerExist(String customerId) {
            for (Invoice invoice : arrInvoice) {
                if (invoice.getCustomerId().equals(customerId)) {
                    return true;
                }
            }
            return false;
        }

    public static boolean isEmployeeExist(String id) {
        for (Invoice invoice : arrInvoice) {
            if (invoice.getEmployeeId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPromotionExist(ArrayList<Promotion> promotions) {
        for (Promotion promotion : promotions) {
            for (Invoice invoice : arrInvoice) {
                if (invoice.getPromotionId().equals(promotion.getPromotionId())) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean isPromotionExist(String promotionId) {
        for (Invoice invoice : arrInvoice) {
            if (invoice.getPromotionId().equals(promotionId)) {
                return true;
            }
        }
        return false;
    }
}
