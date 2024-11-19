package milktea.milktea.BUS;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.property.BorderRadius;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import milktea.milktea.DAO.InvoiceDetail_DAO;
import milktea.milktea.DAO.Invoice_DAO;
import milktea.milktea.DTO.Invoice;
import milktea.milktea.DTO.InvoiceDetail;
import milktea.milktea.DTO.Product;
import milktea.milktea.DTO.Promotion;
import milktea.milktea.DTO.TempInvoiceDetail;

import java.awt.*;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
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
            ArrayList<LocalDate> sortedDates = new ArrayList<>();
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
            ArrayList<String> sortedDates = new ArrayList<>(revenueData.keySet());
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
            ArrayList<String> sortedYears = new ArrayList<>(revenueData.keySet());
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
public static void printBill(ArrayList<TempInvoiceDetail> tempInvoiceDetails, Invoice invoice) {
    try {
        StringBuilder htmlSource = new StringBuilder();
        htmlSource.append("<html><head><meta charset='UTF-8'>")
                  .append("<style>")
                  .append("@font-face {")
                  .append("    font-family: 'CustomFont';")
                  .append("    src: url('fonts/arial-unicode-ms.ttf') format('truetype');")
                  .append("}")
                  .append("body { font-family: 'CustomFont'; }")
                  .append("table { width: 100%; border-collapse: collapse; }")
                  .append("th, td { border: 1px solid #ddd; padding: 8px; }")
                  .append("th { background-color: #f2f2f2; }")
                  .append(".text-right { text-align: right; }")
                  .append(".title { text-align: center; font-weight: bold; font-size: 24px; }")
                  .append("</style></head><body>");

        htmlSource.append("<div class='title'>HOÁ ĐƠN BÁN HÀNG</div>")
                  .append("<hr style='border-top: 3px double #8c8b8b;'>");

        htmlSource.append("<table>")
                  .append("<tr><td>Mã hoá đơn:</td><td>").append(invoice.getInvoiceId()).append("</td>")
                  .append("<td>Ngày lập:</td><td>").append(invoice.getIssueDate()).append("</td></tr>")
                  .append("<tr><td>Mã nhân viên:</td><td>").append(invoice.getEmployeeId()).append("</td>")
                  .append("<td>Mã khách hàng:</td><td>").append(invoice.getCustomerId()).append("</td></tr>")
                  .append("</table>");

        htmlSource.append("<hr style='border-top: 1px solid #8c8b8b;'>");

        htmlSource.append("<table>")
                  .append("<tr>")
                  .append("<th>Tên sản phẩm</th>")
                  .append("<th>Số lượng</th>")
                  .append("<th>Đơn giá</th>")
                  .append("<th>Thành tiền</th>")
                  .append("</tr>");

        for (TempInvoiceDetail tempDetail : tempInvoiceDetails) {
            InvoiceDetail invoiceDetail = tempDetail.getInvoiceDetail();
            String productName = Product_BUS.getProductById(invoiceDetail.getProductId()).getName();

            htmlSource.append("<tr>")
                      .append("<td>").append(productName).append("</td>")
                      .append("<td>").append(invoiceDetail.getQuantity()).append("</td>")
                      .append("<td>").append(invoiceDetail.getUnitPrice()).append("</td>")
                      .append("<td>").append(invoiceDetail.getTotalPrice()).append("</td>")
                      .append("</tr>");

            // Hiển thị thông tin Đường, Đá, Ghi chú nếu có
            if ((tempDetail.getSugar() >= 0 || tempDetail.getIce() >= 0) ||
                (tempDetail.getNote() != null && !tempDetail.getNote().isEmpty())) {
                htmlSource.append("<tr>")
                          .append("<td colspan='4' style='padding-left: 20px;'>+ ");

                if (tempDetail.getSugar() >= 0) {
                    htmlSource.append("Đường: ").append(tempDetail.getSugar()).append("% ");
                }
                if (tempDetail.getIce() >= 0) {
                    htmlSource.append("Đá: ").append(tempDetail.getIce()).append("% ");
                }
                if (tempDetail.getNote() != null && !tempDetail.getNote().isEmpty()) {
                    htmlSource.append("Ghi chú: ").append(tempDetail.getNote());
                }
                htmlSource.append("</td></tr>");
            }

            // Hiển thị Topping nếu có
            if (tempDetail.getTopping() != null && !tempDetail.getTopping().isEmpty()) {
                for (HashMap.Entry<String, Integer> entry : tempDetail.getTopping().entrySet()) {
                    String toppingName = Product_BUS.getProductById(entry.getKey()).getName();
                    Integer toppingQuantity = entry.getValue();

                    htmlSource.append("<tr>")
                              .append("<td style='padding-left: 20px;'>+ ").append(toppingName).append("</td>")
                              .append("<td>").append(toppingQuantity).append("</td>")
                              .append("<td>").append(Product_BUS.getProductById(entry.getKey()).getPrice()).append("</td>")
                              .append("<td>").append(Product_BUS.getProductById(entry.getKey()).getPrice().multiply(BigDecimal.valueOf(toppingQuantity))).append("</td>")
                              .append("</tr>");
                }
            }
        }

        htmlSource.append("</table>");

        BigDecimal discount = invoice.getDiscount() != null ? invoice.getDiscount() : BigDecimal.ZERO;
        BigDecimal subTotal = invoice.getTotal().subtract(discount);

        htmlSource.append("<hr style='border-top: 1px solid #8c8b8b;'>");
        htmlSource.append("<table class='text-right'>")
                  .append("<tr><td>Tạm tính:</td><td>").append(subTotal).append("</td></tr>")
                  .append("<tr><td>Giảm giá:</td><td>").append(discount).append("</td></tr>")
                  .append("<tr><th>Tổng tiền:</th><th>").append(invoice.getTotal()).append("</th></tr>")
                  .append("</table>");

        htmlSource.append("</body></html>");

        String finalHtmlSource = htmlSource.toString();
        File directory = new File("./HoaDon");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String pdfDest = "./HoaDon/" + invoice.getInvoiceId() + ".pdf";
        PdfWriter writer = new PdfWriter(pdfDest);

        ConverterProperties props = new ConverterProperties();
        props.setCharset("UTF-8");

        // Thiết lập FontProvider và thêm font tùy chỉnh
        FontProvider fontProvider = new DefaultFontProvider(false, false, false);
        String fontPath = "fonts/arial-unicode-ms.ttf"; // Đường dẫn đến file font
        fontProvider.addFont(fontPath);
        props.setFontProvider(fontProvider);

        HtmlConverter.convertToPdf(finalHtmlSource, writer, props);
        Desktop.getDesktop().open(new java.io.File(pdfDest));
    } catch (Exception e) {
        log.error("Error while printing bill: {}", e.getMessage());
    }
}
     public static void printCupLabels(ArrayList<TempInvoiceDetail> tempInvoiceDetails) {
        try {
            // Define the destination PDF file
            String dest = "CupLabels.pdf";
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A6); // Adjust page size as needed

            // Set uniform margins for all labels
            document.setMargins(10, 10, 10, 10);

            // Load a font that supports Vietnamese characters
            String fontPath = "fonts/arial-unicode-ms.ttf"; // Update the path to your font file
            PdfFont unicodeFont = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, true);

            for (TempInvoiceDetail tempDetail : tempInvoiceDetails) {
                // Start a new page for each label
                pdfDoc.addNewPage(PageSize.A6); // Adjust to desired label size

                // Create a Div to act as a container with border
                Div labelContainer = new Div()
                        .setBorder(new SolidBorder(ColorConstants.BLACK, 1)) // 1pt black border\
                        .setBorderRadius(new BorderRadius(5))
                        .setPadding(5)
                        .setMargin(5)
                        .setWidth(UnitValue.createPercentValue(100))
                        .setHeight(UnitValue.createPercentValue(100));


                // Add product name
                String productNameStr = Objects.requireNonNull(
                        Product_BUS.getProductById(tempDetail.getInvoiceDetail().getProductId()))
                        .getName();
                Paragraph productName = new Paragraph(productNameStr)
                        .setFont(unicodeFont)
                        .setFontSize(14)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER);
                labelContainer.add(productName);

                // Separator line
                labelContainer.add(new LineSeparator(new SolidLine()).setMarginTop(5).setMarginBottom(5));

                // Sugar and Ice details
                String sugarIceStr = "Đường: " + tempDetail.getSugar() + "%     " +
                        "Đá: " + tempDetail.getIce() + "%";
                Paragraph sugarIce = new Paragraph(sugarIceStr)
                        .setFont(unicodeFont)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER);
                labelContainer.add(sugarIce);

                // Toppings
                if (tempDetail.getTopping() != null && !tempDetail.getTopping().isEmpty()) {
                    // Topping title
                    Paragraph toppingTitle = new Paragraph("Topping:")
                            .setFont(unicodeFont)
                            .setBold()
                            .setFontSize(12)
                            .setTextAlignment(TextAlignment.LEFT);
                    labelContainer.add(toppingTitle);

                    // Toppings list
                    com.itextpdf.layout.element.List toppingList = new com.itextpdf.layout.element.List()
                            .setSymbolIndent(12)
                            .setListSymbol("•")
                            .setFont(unicodeFont)
                            .setFontSize(12)
                            .setMarginLeft(20);

                    for (HashMap.Entry<String, Integer> entry : tempDetail.getTopping().entrySet()) {
                        toppingList.add(new ListItem(Objects.requireNonNull(Product_BUS.getProductById(entry.getKey())).getName() + " x " + entry.getValue()));
                    }
                    labelContainer.add(toppingList);
                }

                // Separator line
                labelContainer.add(new LineSeparator(new SolidLine()).setMarginTop(5).setMarginBottom(5));

                // Note
                if (tempDetail.getNote() != null && !tempDetail.getNote().isEmpty()) {
                    String noteStr = "Ghi chú: " + tempDetail.getNote();
                    Paragraph note = new Paragraph(noteStr)
                            .setFont(unicodeFont)
                            .setFontSize(12)
                            .setTextAlignment(TextAlignment.LEFT);
                    labelContainer.add(note);

                    // Optional: Another separator after note
                    labelContainer.add(new LineSeparator(new SolidLine()).setMarginTop(5).setMarginBottom(5));
                }

                // Add the container to the document
                document.add(labelContainer);
            }

            document.close();

            // Open the generated PDF automatically
            Desktop.getDesktop().open(new File(dest));

        } catch (Exception e) {
            // Replace with your logging mechanism
             log.error("Error while printing cup labels: {}", e.getMessage());
        }
    }

}
