package milktea.milktea.BUS;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import milktea.milktea.DTO.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;

class Invoice_BUSTest {

    @Test
    void calRevenueData() {
        Invoice_BUS.getLocalData();
        InvoiceDetail_BUS.getLocalData();
        Product_BUS.getLocalData();
        GoodsReceipt_BUS.getLocalData();
        InvoiceDetail_BUS.getMapInvoiceDetail();
        LocalDate start = LocalDate.of(2024,10,28);
        LocalDate end = LocalDate.of(2024,11,25);
        HashMap<String,String> search = new HashMap<>();
//        search.put("startDate",start.toString());
//        search.put("endDate",end.toString());
        search.put("year","2024");
        System.out.println(Invoice_BUS.RevenueInfo(search));
        System.out.println(GoodsReceipt_BUS.CapitalInfo(search));
        BigDecimal profit = Invoice_BUS.RevenueInfo(search).get("total").subtract(GoodsReceipt_BUS.CapitalInfo(search).get("total")).subtract(Invoice_BUS.RevenueInfo(search).get("discount"));
        System.out.println("profit="+profit);
    }
}