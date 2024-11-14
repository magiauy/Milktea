package milktea.milktea.BUS;

import milktea.milktea.DAO.GoodsReceipt_DAO;
import milktea.milktea.DTO.GoodsReceipt;
import milktea.milktea.DTO.Invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class GoodsReceipt_BUS {

    private static ArrayList<GoodsReceipt> arrGoodsReceipt = new ArrayList<>();

    public static void getLocalData() {
        arrGoodsReceipt.clear();
        arrGoodsReceipt = GoodsReceipt_DAO.getAllGoodsReceipt();
    }

        public static ArrayList<GoodsReceipt> getAllGoodsReceipt() {
        return arrGoodsReceipt;
    }

    public static boolean addGoodsReceipt(GoodsReceipt goodsReceipt) {
        return GoodsReceipt_DAO.addGoodsReceipt(goodsReceipt);
    }

    public static void addGoodsReceiptLocal(GoodsReceipt goodsReceipt) {
        arrGoodsReceipt.add(goodsReceipt);
    }

    public static String autoId(){
        if (arrGoodsReceipt.isEmpty()) {
            return "GRN0001";
        }
        String lastId = arrGoodsReceipt.getLast().getId();
        int id = Integer.parseInt(lastId.substring(3)) + 1;
        return "GRN" + String.format("%03d", id);
    }

    public static void deleteGoodsReceipt(String id) {
        GoodsReceipt_DAO.deleteGoodsReceipt(id);
    }

    public static ArrayList<GoodsReceipt> searchGoodsReceipt(String key) {
        ArrayList<GoodsReceipt> result = new ArrayList<>();
        for (GoodsReceipt goodsReceipt : arrGoodsReceipt) {
            if (goodsReceipt.getId().contains(key) || goodsReceipt.getProviderId().contains(key)|| goodsReceipt.getEmployeeId().contains(key)) {
                result.add(goodsReceipt);
            }
        }
        return result;
    }

    public static ArrayList<GoodsReceipt> advancedSearchGoodsReceipt(HashMap<String, String> search) {
        ArrayList<GoodsReceipt> result = new ArrayList<>();
        for (GoodsReceipt goodsReceipt : arrGoodsReceipt) {
            boolean matches = true;
            if (search.containsKey("goodsReceiptID")) {
                String id = search.get("goodsReceiptID");
                if (!goodsReceipt.getId().equals(id)) {
                    matches = false;
                }
            }
            // Date range filter
            if (search.containsKey("startDate") && search.containsKey("endDate")) {
                LocalDate startDate = LocalDate.parse(search.get("startDate"));
                LocalDate endDate = LocalDate.parse(search.get("endDate"));
                LocalDate issueDate = goodsReceipt.getDate();
                if (issueDate.isBefore(startDate) || issueDate.isAfter(endDate)) {
                    matches = false;
                }
            }

            // Total amount range filter
            if (search.containsKey("minTotal") && search.containsKey("maxTotal")) {
                BigDecimal minTotal = new BigDecimal(search.get("minTotal"));
                BigDecimal maxTotal = new BigDecimal(search.get("maxTotal"));
                BigDecimal total = goodsReceipt.getTotal();
                if (total.compareTo(minTotal) < 0 || total.compareTo(maxTotal) > 0) {
                    matches = false;
                }
            }

            // Customer ID filter
            if (search.containsKey("providerId")) {
                String providerId = search.get("providerId");
                if (!goodsReceipt.getProviderId().equals(providerId)) {
                    matches = false;
                }
            }

            // Employee ID filter
            if (search.containsKey("employeeId")) {
                String employeeId = search.get("employeeId");
                if (!goodsReceipt.getEmployeeId().equals(employeeId)) {
                    matches = false;
                }
            }
            if (matches) {
                result.add(goodsReceipt);
            }
        }
        return result;
    }

    public static boolean isProviderExist(String id) {
        for (GoodsReceipt goodsReceipt : arrGoodsReceipt) {
            if (goodsReceipt.getProviderId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmployeeExist(String id) {
        for (GoodsReceipt goodsReceipt : arrGoodsReceipt) {
            if (goodsReceipt.getEmployeeId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
