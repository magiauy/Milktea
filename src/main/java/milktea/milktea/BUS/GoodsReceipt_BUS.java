package milktea.milktea.BUS;

import milktea.milktea.DAO.GoodsReceipt_DAO;
import milktea.milktea.DTO.GoodsReceipt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.scene.chart.XYChart;
import javafx.collections.ObservableList;

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
    public static HashMap<String,BigDecimal> calCapitalDate(LocalDate startDate, LocalDate endDate) {
        HashMap<String,BigDecimal> result = new HashMap<>();
        if (ChronoUnit.DAYS.between(startDate, endDate) > 31) {
            throw new IllegalArgumentException("Date range should not exceed 31 days.");
        }
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            result.put(date.toString(), BigDecimal.ZERO);
            date = date.plusDays(1);
        }
        for (GoodsReceipt goodsReceipt : arrGoodsReceipt) {
            LocalDate dateGRN = goodsReceipt.getDate();
            if (!dateGRN.isBefore(startDate) && !dateGRN.isAfter(endDate)) {
                String day = dateGRN.toString();
                BigDecimal total = result.get(day).add(goodsReceipt.getTotal());
                result.put(day, total);
            }
        }
        return result;
    }

    public static HashMap<String,BigDecimal> calCapitalDate(int year, int month) {
        HashMap<String,BigDecimal> result = new HashMap<>();
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        // Initialize all days with zero
        for (int day = 1; day <= daysInMonth; day++) {
            result.put(String.valueOf(day), BigDecimal.ZERO);
        }
        for (GoodsReceipt goodsReceipt : arrGoodsReceipt) {
            LocalDate date = goodsReceipt.getDate();
            if (date.getYear() == year && date.getMonthValue() == month) {
                String day = String.valueOf(date.getDayOfMonth());
                BigDecimal total = result.get(day).add(goodsReceipt.getTotal());
                result.put(day, total);
            }
        }
        return result;
    }

    public static HashMap<String,BigDecimal> calCapitalDate(int year) {
        HashMap<String,BigDecimal> result = new HashMap<>();

        for(int i=1;i<=12;i++){
            result.put(String.valueOf(i),BigDecimal.ZERO);
        }


        for (GoodsReceipt goodsReceipt : arrGoodsReceipt) {
            LocalDate date = goodsReceipt.getDate();
            if (date.getYear() == year) {
                String month = String.valueOf(goodsReceipt.getDate().getMonthValue());
                BigDecimal total = result.getOrDefault(month, BigDecimal.ZERO);
                total = total.add(goodsReceipt.getTotal());
                result.put(month, total);
            }
        }
        return result;
    }

    public static ObservableList<XYChart.Series<String,Number>> getCapitalData(HashMap<String, String> search) {
        ObservableList<XYChart.Series<String,Number>> data = FXCollections.observableArrayList();
        XYChart.Series<String,Number> series = new XYChart.Series<>();
        series.setName("Vốn");

        if (search.containsKey("startDate") && search.containsKey("endDate")) {
            LocalDate startDate = LocalDate.parse(search.get("startDate"));
            LocalDate endDate = LocalDate.parse(search.get("endDate"));
            HashMap<String, BigDecimal> capitalData = calCapitalDate(startDate, endDate);
        
            // Create a list of LocalDate keys
            List<LocalDate> sortedDates = new ArrayList<>();
            for (String dateStr : capitalData.keySet()) {
                LocalDate date = LocalDate.parse(dateStr);
                sortedDates.add(date);
            }
            // Sort the dates
            Collections.sort(sortedDates);
            // Add data in sorted order
            for (LocalDate date : sortedDates) {
                String dateStr = date.toString();
                BigDecimal value = capitalData.getOrDefault(dateStr, BigDecimal.ZERO);
                series.getData().add(new XYChart.Data<>(dateStr, value));
            
            }
        } else if (search.containsKey("year") && search.containsKey("month")) {
            int year = Integer.parseInt(search.get("year"));
            int month = Integer.parseInt(search.get("month"));
            HashMap<String, BigDecimal> capitalData = calCapitalDate(year, month);
            // Create a sorted list of keys
            List<String> sortedDates = new ArrayList<>(capitalData.keySet());
            sortedDates.sort(Comparator.comparingInt(Integer::parseInt));
            // Add data in sorted order
            for (String date : sortedDates) {
                BigDecimal value = capitalData.getOrDefault(date, BigDecimal.ZERO);
                series.getData().add(new XYChart.Data<>(date, value));
            }
        } else if (search.containsKey("year")) {
            int year = Integer.parseInt(search.get("year"));
            HashMap<String, BigDecimal> capitalData = calCapitalDate(year);
            // Create a sorted list of keys
            List<String> sortedYears = new ArrayList<>(capitalData.keySet());
            sortedYears.sort(Comparator.comparingInt(Integer::parseInt));
            // Add data in sorted order
                    for (String month : sortedYears) {
                        BigDecimal value = capitalData.getOrDefault(month, BigDecimal.ZERO);
                        series.getData().add(new XYChart.Data<>(month, value));
                    }

        }
        data.add(series);
        return data;
    }

    public static Set<String> getYearAvailable() {
        Set<String> result = new HashSet<>();
        for (GoodsReceipt goodsReceipt : arrGoodsReceipt) {
            result.add(goodsReceipt.getDate().getYear() + "");
        }
        return result;
    }

    public static HashMap<String, BigDecimal> CapitalInfo(HashMap<String, String> search) {
        HashMap<String, BigDecimal> result = new HashMap<>();
        result.put("total", BigDecimal.ZERO);
        if (search.containsKey("startDate") && search.containsKey("endDate")) {
            LocalDate startDate = LocalDate.parse(search.get("startDate"));
            LocalDate endDate = LocalDate.parse(search.get("endDate"));
            HashMap<String, BigDecimal> capitalData = calCapitalDate(startDate, endDate);
            for (BigDecimal value : capitalData.values()) {
                result.put("total", result.get("total").add(value));
            }
        } else if (search.containsKey("year") && search.containsKey("month")) {
            int year = Integer.parseInt(search.get("year"));
            int month = Integer.parseInt(search.get("month"));
            HashMap<String, BigDecimal> capitalData = calCapitalDate(year, month);
            for (BigDecimal value : capitalData.values()) {
                result.put("total", result.get("total").add(value));
            }
        } else if (search.containsKey("year")) {
            int year = Integer.parseInt(search.get("year"));
            HashMap<String, BigDecimal> capitalData = calCapitalDate(year);
            for (BigDecimal value : capitalData.values()) {
                result.put("total", result.get("total").add(value));
            }
        }
        return result;
    }
}
