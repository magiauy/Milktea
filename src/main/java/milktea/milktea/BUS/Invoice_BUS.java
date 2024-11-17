package milktea.milktea.BUS;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import lombok.NonNull;
import milktea.milktea.DAO.InvoiceDetail_DAO;
import milktea.milktea.DAO.Invoice_DAO;
import milktea.milktea.DTO.Invoice;
import milktea.milktea.DTO.InvoiceDetail;
import milktea.milktea.DTO.Product;
import milktea.milktea.DTO.Promotion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

    public static HashMap<String,BigDecimal> calRevenueData(int year){
        HashMap<String,BigDecimal> data = new HashMap<>();

        for(int i=1;i<=12;i++){
            data.put(String.valueOf(i),BigDecimal.ZERO);
        }

        for (Invoice invoice : arrInvoice){
            if (invoice.getIssueDate().getYear() == year){
                String month = String.valueOf(invoice.getIssueDate().getMonthValue());
                BigDecimal total = data.getOrDefault(month, BigDecimal.ZERO);
                total = total.add(invoice.getTotal());
                data.put(month, total);
            }
        }
        return data;
    }
        public static HashMap<String, BigDecimal> calRevenueData(int year, int month) {
            HashMap<String, BigDecimal> data = new HashMap<>();
            // Get number of days in the month
            YearMonth yearMonth = YearMonth.of(year, month);
            int daysInMonth = yearMonth.lengthOfMonth();
            // Initialize all days with zero
            for (int day = 1; day <= daysInMonth; day++) {
                data.put(String.valueOf(day), BigDecimal.ZERO);
            }
            // Accumulate totals per day
            for (Invoice invoice : arrInvoice) {
                LocalDate issueDate = invoice.getIssueDate();
                if (issueDate.getYear() == year && issueDate.getMonthValue() == month) {
                    String day = String.valueOf(issueDate.getDayOfMonth());
                    BigDecimal total = data.get(day).add(invoice.getTotal());
                    data.put(day, total);
                }
            }
            return data;
        }
        public static HashMap<String, BigDecimal> calRevenueData(LocalDate startDate, LocalDate endDate) {
        HashMap<String, BigDecimal> data = new HashMap<>();
        // Check if date range exceeds 31 days
        if (ChronoUnit.DAYS.between(startDate, endDate) > 31) {
            throw new IllegalArgumentException("Date range should not exceed 31 days.");
        }
        // Initialize data map with dates from startDate to endDate
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            data.put(date.toString(), BigDecimal.ZERO);
            date = date.plusDays(1);
        }
        // Accumulate totals per day
        for (Invoice invoice : arrInvoice) {
            LocalDate issueDate = invoice.getIssueDate();
            if (!issueDate.isBefore(startDate) && !issueDate.isAfter(endDate)) {
                String day = issueDate.toString();
                BigDecimal total = data.get(day).add(invoice.getTotal());
                data.put(day, total);
            }
        }
        return data;
    }

    public static ObservableList<XYChart.Series<String,Number>> getRevenueData(HashMap<String, String> search) {
        ObservableList<XYChart.Series<String, Number>> data = FXCollections.observableArrayList();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu");
                if (search.containsKey("startDate") && search.containsKey("endDate")) {
            LocalDate startDate = LocalDate.parse(search.get("startDate"));
            LocalDate endDate = LocalDate.parse(search.get("endDate"));
            HashMap<String, BigDecimal> revenueData = calRevenueData(startDate, endDate);
        
            // Create a list of LocalDate keys
            List<LocalDate> sortedDates = new ArrayList<>();
            for (String dateStr : revenueData.keySet()) {
                LocalDate date = LocalDate.parse(dateStr);
                sortedDates.add(date);
            }
            // Sort the dates
            Collections.sort(sortedDates);
            // Add data in sorted order
            for (LocalDate date : sortedDates) {
                String dateStr = date.toString();
                BigDecimal value = revenueData.getOrDefault(dateStr, BigDecimal.ZERO);
                series.getData().add(new XYChart.Data<>(dateStr, value));
            
            }
        } else if (search.containsKey("year") && search.containsKey("month")) {
            int year = Integer.parseInt(search.get("year"));
            int month = Integer.parseInt(search.get("month"));
            HashMap<String, BigDecimal> revenueData = calRevenueData(year, month);
            // Create a sorted list of keys
            List<String> sortedDates = new ArrayList<>(revenueData.keySet());
            sortedDates.sort(Comparator.comparingInt(Integer::parseInt));
            // Add data in sorted order
            for (String date : sortedDates) {
                BigDecimal value = revenueData.getOrDefault(date, BigDecimal.ZERO);
                series.getData().add(new XYChart.Data<>(date, value));
            }
        } else if (search.containsKey("year")) {
            int year = Integer.parseInt(search.get("year"));
            HashMap<String, BigDecimal> revenueData = calRevenueData(year);
            // Create a sorted list of keys
            List<String> sortedYears = new ArrayList<>(revenueData.keySet());
            sortedYears.sort(Comparator.comparingInt(Integer::parseInt));
            // Add data in sorted order
                    for (String month : sortedYears) {
                        BigDecimal value = revenueData.getOrDefault(month, BigDecimal.ZERO);
                        series.getData().add(new XYChart.Data<>(month, value));
                    }

        }
        data.add(series);
        return data;
    }

    public static ArrayList<Product> getTopSellingProduct(LocalDate startDate, LocalDate endDate) {
        HashMap<String, Integer> productQuantity = new HashMap<>();
        HashMap<String, BigDecimal> productTotal = new HashMap<>();
        for (Invoice invoice : arrInvoice) {
            LocalDate issueDate = invoice.getIssueDate();
            if (!issueDate.isBefore(startDate) && !issueDate.isAfter(endDate)) {
                for (InvoiceDetail invoiceDetail : InvoiceDetail_BUS.getInvoiceDetailByInvoiceId(invoice.getInvoiceId())) {
                    String productId = invoiceDetail.getProductId();
                    int quantity = invoiceDetail.getQuantity();
                    BigDecimal total = invoiceDetail.getTotalPrice();
                    productQuantity.put(productId, productQuantity.getOrDefault(productId, 0) + quantity);
                    productTotal.put(productId, productTotal.getOrDefault(productId, BigDecimal.ZERO).add(total));
                }
            }
        }
        ArrayList<Product> products = new ArrayList<>();
        for (String productId : productQuantity.keySet()) {
            Product product = Product_BUS.quickGetProductById(productId);
            if (product != null) {
                product.setQuantity(productQuantity.get(productId));
                product.setTotal(productTotal.get(productId));
                products.add(product);
            }
        }
        products.sort((p1, p2) -> p2.getQuantity() - p1.getQuantity());
        return products;
    }
    public static Set<String> getYearAvailable() {
        Set<String> years = new HashSet<>();
        for (Invoice invoice : arrInvoice) {
            years.add(String.valueOf(invoice.getIssueDate().getYear()));
        }
        return years;
    }

    public static HashMap<String ,BigDecimal> RevenueInfo(HashMap<String , String> search){
        HashMap<String,BigDecimal> data = new HashMap<>();
        data.put("total",BigDecimal.ZERO);
        data.put("discount",BigDecimal.ZERO);

        if (search.containsKey("startDate") && search.containsKey("endDate")) {
            LocalDate startDate = LocalDate.parse(search.get("startDate"));
            LocalDate endDate = LocalDate.parse(search.get("endDate"));
            for (Invoice invoice : arrInvoice){
                if (invoice.getIssueDate().isAfter(startDate) && invoice.getIssueDate().isBefore(endDate)){
                    data.put("total",data.get("total").add(invoice.getTotal()));
                    data.put("discount",data.get("discount").add(invoice.getDiscount()));
                }
            }
        }else if (search.containsKey("year") && search.containsKey("month")) {
            int year = Integer.parseInt(search.get("year"));
            int month = Integer.parseInt(search.get("month"));
            for (Invoice invoice : arrInvoice) {
                if (invoice.getIssueDate().getYear() == year && invoice.getIssueDate().getMonthValue() == month) {
                    data.put("total", data.get("total").add(invoice.getTotal()));
                    data.put("discount", data.get("discount").add(invoice.getDiscount()));
                }
            }
        }else if (search.containsKey("year")) {
            int year = Integer.parseInt(search.get("year"));
            for (Invoice invoice : arrInvoice) {
                if (invoice.getIssueDate().getYear() == year) {
                    data.put("total", data.get("total").add(invoice.getTotal()));
                    data.put("discount", data.get("discount").add(invoice.getDiscount()));
                }
            }
        }
        return data;
    }
}
