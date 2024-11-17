package milktea.milktea.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import milktea.milktea.BUS.GoodsReceiptDetail_BUS;
import milktea.milktea.BUS.GoodsReceipt_BUS;
import milktea.milktea.BUS.InvoiceDetail_BUS;
import milktea.milktea.BUS.Invoice_BUS;
import milktea.milktea.Util.ValidationUtil;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class StatisticsGUI {
    @FXML
    private BarChart<String,Number> revenueChart;
    @FXML
    private LineChart<String,Number> profitChart;
    @FXML
    private BarChart<String,Number> capitalChart;




    @FXML
    private ComboBox<String> yearComboBox;
    @FXML
    private ComboBox<String> monthComboBox;

    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Button statisticButton;

    @FXML
    private ImageView imgRefresh;

    @FXML
    private ComboBox<String> yearComboBox1;
    @FXML
    private ComboBox<String> monthComboBox1;

    @FXML
    private DatePicker startDatePicker1;
    @FXML
    private DatePicker endDatePicker1;

    @FXML
    private Button statisticButton1;

    @FXML
    private ImageView imgRefresh1;

    @FXML
    private ComboBox<String> yearComboBox2;
    @FXML
    private ComboBox<String> monthComboBox2;

    @FXML
    private DatePicker startDatePicker2;
    @FXML
    private DatePicker endDatePicker2;

    @FXML
    private Button statisticButton2;

    @FXML
    private ImageView imgRefresh2;

    @FXML
            private Label lbRevenue;
    @FXML
            private Label lbCapital;
    @FXML
            private Label lbProfit;
    @FXML
            private Label lbDiscount;


    HashMap<String,String> search = new HashMap<>();

    public void initialize() {
        setUpStatisticsRevenueChart();
        initStatistic();
        statisticButton.setOnAction(event -> statisticButtonClicked());
        imgRefresh.setOnMouseClicked(event -> {
            refresh();
            initStatistic();
        });
        
        setUpStatisticsCapitalChart();
        initStatistic1();
        statisticButton1.setOnAction(event -> capitalizeButtonClicked());
        imgRefresh1.setOnMouseClicked(event -> {
            refresh();
            initStatistic1();
        });

        setUpProfitChart();
        initStatistic2();
        statisticButton2.setOnAction(event -> profitButtonClicked());
        imgRefresh2.setOnMouseClicked(event -> {
            refresh();
            initStatistic2();
        });

    }

    public void statisticButtonClicked(){
        if (ValidationUtil.isEmptyDp(startDatePicker,"Ngày bắt đầu") || ValidationUtil.isEmptyDp(endDatePicker,"Ngày kết thúc")){
            return;
        }
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        if (!ValidationUtil.isValidDateRange(start, end)) {
            return;
        }
        // Không được vượt quá 31 ngày
        if (ChronoUnit.DAYS.between(start, end) > 31) {
            ValidationUtil.showErrorAlert("Khoảng thời gian không được vượt quá 31 ngày");

            return;
        }
        search.clear();
        search.put("startDate",start.toString());
        search.put("endDate",end.toString());
        revenueChart.getData().clear();
        revenueChart.setData(Invoice_BUS.getRevenueData(search));
        revenueChart.setAnimated(false);
        revenueChart.setStyle("-fx-font-size: 16px;");
        revenueChart.setTitle("Doanh thu từ " + start + " đến " + end);
        revenueChart.getXAxis().setLabel("Thời gian");
        revenueChart.getXAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
        revenueChart.getXAxis().setTickLabelFont(Font.font(12));

        // Set font size for Y axis label and tick labels
        revenueChart.getYAxis().setLabel("Doanh thu");
        revenueChart.getYAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
        revenueChart.getYAxis().setTickLabelFont(Font.font(12));

    }
    public void setUpStatisticsRevenueChart() {
        // TODO
        yearComboBox.getItems().addAll(Invoice_BUS.getYearAvailable());
        yearComboBox.getSelectionModel().selectFirst();
        monthComboBox.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12");
        yearComboBox.setOnAction(event -> {
            if (!yearComboBox.getValue().isEmpty()) {
                search.clear();
                search.put("year", yearComboBox.getValue());
                monthComboBox.getSelectionModel().clearSelection();
                revenueChart.getData().clear();
                revenueChart.setData(Invoice_BUS.getRevenueData(search));
                revenueChart.setAnimated(false);
                revenueChart.setStyle("-fx-font-size: 16px;");
                revenueChart.setTitle("Doanh thu năm " + yearComboBox.getValue());
                revenueChart.getXAxis().setLabel("Tháng");
                revenueChart.getXAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
                revenueChart.getXAxis().setTickLabelFont(Font.font(12));

                // Set font size for Y axis label and tick labels
                revenueChart.getYAxis().setLabel("Doanh thu");
                revenueChart.getYAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
                revenueChart.getYAxis().setTickLabelFont(Font.font(12));
            }
        });
        monthComboBox.setOnAction(event -> {
            if (monthComboBox.getValue() != null) {
                search.clear();
                search.put("year", yearComboBox.getValue());
                search.put("month", monthComboBox.getValue());
                revenueChart.getData().clear();
                revenueChart.setData(Invoice_BUS.getRevenueData(search));
                revenueChart.setAnimated(false);
                revenueChart.setStyle("-fx-font-size: 16px;");
                revenueChart.setTitle("Doanh thu tháng " + monthComboBox.getValue() + " năm " + yearComboBox.getValue());
                revenueChart.getXAxis().setLabel("Ngày");
                revenueChart.getXAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");

                revenueChart.getXAxis().setTickLabelFont(Font.font(12));

                // Set font size for Y axis label and tick labels
                revenueChart.getYAxis().setLabel("Doanh thu");
                revenueChart.getYAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
                revenueChart.getYAxis().setTickLabelFont(Font.font(12));
            }
        });

    }
    public void initStatistic(){
        revenueChart.getData().clear();
        search.clear();
        monthComboBox.getSelectionModel().clearSelection();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        yearComboBox.getSelectionModel().select(LocalDate.now().getYear()+"");
        search.put("year", LocalDate.now().getYear()+"");
        revenueChart.setData(Invoice_BUS.getRevenueData(search));
        revenueChart.setAnimated(false);
        revenueChart.setTitle("Doanh thu năm 2024");
        revenueChart.setStyle("-fx-font-size: 16px;");
        revenueChart.getXAxis().setLabel("Thời gian");
        revenueChart.getXAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
        revenueChart.getXAxis().setTickLabelFont(Font.font(12));

        // Set font size for Y axis label and tick labels
        revenueChart.getYAxis().setLabel("Doanh thu");
        revenueChart.getYAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
        revenueChart.getYAxis().setTickLabelFont(Font.font(12));
    }

    public void capitalizeButtonClicked(){
        if (ValidationUtil.isEmptyDp(startDatePicker1,"Ngày bắt đầu") || ValidationUtil.isEmptyDp(endDatePicker1,"Ngày kết thúc")){
            return;
        }
        LocalDate start = startDatePicker1.getValue();
        LocalDate end = endDatePicker1.getValue();
        if (!ValidationUtil.isValidDateRange(start, end)) {
            return;
        }
        // Không được vượt quá 31 ngày
        if (ChronoUnit.DAYS.between(start, end) > 31) {
            ValidationUtil.showErrorAlert("Khoảng thời gian không được vượt quá 31 ngày");

            return;
        }
        search.clear();
        search.put("startDate",start.toString());
        search.put("endDate",end.toString());
        capitalChart.getData().clear();
        capitalChart.setData(GoodsReceipt_BUS.getCapitalData(search));
        capitalChart.setAnimated(false);
        capitalChart.setStyle("-fx-font-size: 16px;");
        capitalChart.setTitle("Vốn từ " + start + " đến " + end);
        capitalChart.getXAxis().setLabel("Thời gian");
        capitalChart.getXAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
        capitalChart.getXAxis().setTickLabelFont(Font.font(12));

        // Set font size for Y axis label and tick labels
        capitalChart.getYAxis().setLabel("Vốn");
        capitalChart.getYAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
        capitalChart.getYAxis().setTickLabelFont(Font.font(12));

    }
    public void setUpStatisticsCapitalChart(){
        yearComboBox1.getItems().addAll(GoodsReceipt_BUS.getYearAvailable());
        yearComboBox1.getSelectionModel().selectFirst();
        monthComboBox1.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12");
        yearComboBox1.setOnAction(event -> {
            if (!yearComboBox1.getValue().isEmpty()) {
                search.clear();
                search.put("year", yearComboBox1.getValue());
                monthComboBox1.getSelectionModel().clearSelection();
                capitalChart.getData().clear();
                capitalChart.setData(GoodsReceipt_BUS.getCapitalData(search));
                capitalChart.setAnimated(false);
                capitalChart.setStyle("-fx-font-size: 16px;");
                capitalChart.setTitle("Vốn năm " + yearComboBox1.getValue());
                capitalChart.getXAxis().setLabel("Tháng");
                capitalChart.getXAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
                capitalChart.getXAxis().setTickLabelFont(Font.font(12));

                // Set font size for Y axis label and tick labels
                capitalChart.getYAxis().setLabel("Vốn");
                capitalChart.getYAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
                capitalChart.getYAxis().setTickLabelFont(Font.font(12));
            }
        });

        monthComboBox1.setOnAction(event -> {
            if (monthComboBox1.getValue() != null) {
                search.clear();
                search.put("year", yearComboBox1.getValue());
                search.put("month", monthComboBox1.getValue());
                capitalChart.getData().clear();
                capitalChart.setData(GoodsReceipt_BUS.getCapitalData(search));
                capitalChart.setAnimated(false);
                capitalChart.setStyle("-fx-font-size: 16px;");
                capitalChart.setTitle("Vốn tháng " + monthComboBox1.getValue() + " năm " + yearComboBox1.getValue());
                capitalChart.getXAxis().setLabel("Ngày");
                capitalChart.getXAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");

                capitalChart.getXAxis().setTickLabelFont(Font.font(12));

                // Set font size for Y axis label and tick labels
                capitalChart.getYAxis().setLabel("Vốn");
                capitalChart.getYAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
                capitalChart.getYAxis().setTickLabelFont(Font.font(12));
            }
        });

    }
    public void initStatistic1(){
        capitalChart.getData().clear();
        search.clear();
        monthComboBox1.getSelectionModel().clearSelection();
        startDatePicker1.setValue(null);
        endDatePicker1.setValue(null);
        yearComboBox1.getSelectionModel().select(LocalDate.now().getYear()+"");
        search.put("year", LocalDate.now().getYear()+"");
        capitalChart.setData(GoodsReceipt_BUS.getCapitalData(search));
        capitalChart.setAnimated(false);
        capitalChart.setTitle("Vốn năm 2024");
        capitalChart.setStyle("-fx-font-size: 16px;");
        capitalChart.getXAxis().setLabel("Thời gian");
        capitalChart.getXAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
        capitalChart.getXAxis().setTickLabelFont(Font.font(12));

        // Set font size for Y axis label and tick labels
        capitalChart.getYAxis().setLabel("Vốn");
        capitalChart.getYAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
        capitalChart.getYAxis().setTickLabelFont(Font.font(12));
    }

    public void initStatistic2(){
        search.clear();
        monthComboBox2.getSelectionModel().clearSelection();
        startDatePicker2.setValue(null);
        endDatePicker2.setValue(null);
        yearComboBox2.getSelectionModel().select(LocalDate.now().getYear()+"");
        search.put("year", LocalDate.now().getYear()+"");
        ObservableList<XYChart.Series<String, Number>> combinedData = FXCollections.observableArrayList();
        combinedData.addAll(Invoice_BUS.getRevenueData(search));
        combinedData.addAll(GoodsReceipt_BUS.getCapitalData(search));
        profitChart.setData(combinedData);
        profitChart.setAnimated(false);
        profitChart.setTitle("Doanh thu và vốn năm 2024");
        profitChart.setStyle("-fx-font-size: 16px;");
        profitChart.getXAxis().setLabel("Thời gian");
        profitChart.getXAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
        profitChart.getXAxis().setTickLabelFont(Font.font(12));
        lbRevenue.setText(priceFormat(Invoice_BUS.RevenueInfo(search).get("total").toString()));
        lbCapital.setText(priceFormat(GoodsReceipt_BUS.CapitalInfo(search).get("total").toString()));
        lbDiscount.setText(priceFormat(Invoice_BUS.RevenueInfo(search).get("discount").toString()));
        lbProfit.setText(priceFormat(Invoice_BUS.RevenueInfo(search).get("total").subtract(GoodsReceipt_BUS.CapitalInfo(search).get("total")).subtract(Invoice_BUS.RevenueInfo(search).get("discount")).toString()));
        lbRevenue.textAlignmentProperty().set(javafx.scene.text.TextAlignment.RIGHT);
        lbCapital.textAlignmentProperty().set(javafx.scene.text.TextAlignment.RIGHT);
        lbDiscount.textAlignmentProperty().set(javafx.scene.text.TextAlignment.RIGHT);
        lbProfit.textAlignmentProperty().set(javafx.scene.text.TextAlignment.RIGHT);
    }

    public String priceFormat(String price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(Double.parseDouble(price));
    }

    public void setUpProfitChart() {
        // TODO
        Set<String> years = new HashSet<>();
        years.addAll(Invoice_BUS.getYearAvailable());
        years.addAll(GoodsReceipt_BUS.getYearAvailable());

        List<String> uniqueYears = years.stream().sorted().toList();

        yearComboBox2.getItems().addAll(uniqueYears);
        yearComboBox2.getSelectionModel().selectFirst();
        monthComboBox2.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12");
        yearComboBox2.setOnAction(event -> {
            if (!yearComboBox2.getValue().isEmpty()) {
                search.clear();
                search.put("year", yearComboBox2.getValue());
                monthComboBox2.getSelectionModel().clearSelection();
                profitChart.getData().clear();
                ObservableList<XYChart.Series<String, Number>> combinedData = FXCollections.observableArrayList();
                combinedData.addAll(Invoice_BUS.getRevenueData(search));
                combinedData.addAll(GoodsReceipt_BUS.getCapitalData(search));
                profitChart.setData(combinedData);
                profitChart.setAnimated(false);
                profitChart.setStyle("-fx-font-size: 16px;");
                profitChart.setTitle("Doanh thu và vốn năm " + yearComboBox2.getValue());
                profitChart.getXAxis().setLabel("Tháng");
                profitChart.getXAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
                profitChart.getXAxis().setTickLabelFont(Font.font(12));

                // Set font size for Y axis label and tick labels
                profitChart.getYAxis().setLabel("Doanh thu và vốn");
                profitChart.getYAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
                profitChart.getYAxis().setTickLabelFont(Font.font(12));

                lbRevenue.setText(priceFormat(Invoice_BUS.RevenueInfo(search).get("total").toString()));
                lbCapital.setText(priceFormat(GoodsReceipt_BUS.CapitalInfo(search).get("total").toString()));
                lbDiscount.setText(priceFormat(Invoice_BUS.RevenueInfo(search).get("discount").toString()));
                lbProfit.setText(priceFormat(Invoice_BUS.RevenueInfo(search).get("total").subtract(GoodsReceipt_BUS.CapitalInfo(search).get("total")).subtract(Invoice_BUS.RevenueInfo(search).get("discount")).toString()));
            }
        });

        monthComboBox2.setOnAction(event -> {
            if (monthComboBox2.getValue() != null) {
                search.clear();
                search.put("year", yearComboBox2.getValue());
                search.put("month", monthComboBox2.getValue());
                profitChart.getData().clear();
                ObservableList<XYChart.Series<String, Number>> combinedData = FXCollections.observableArrayList();
                combinedData.addAll(Invoice_BUS.getRevenueData(search));
                combinedData.addAll(GoodsReceipt_BUS.getCapitalData(search));
                profitChart.setData(combinedData);
                profitChart.setAnimated(false);
                profitChart.setStyle("-fx-font-size: 16px;");
                profitChart.setTitle("Doanh thu và vốn tháng " + monthComboBox2.getValue() + " năm " + yearComboBox2.getValue());
                profitChart.getXAxis().setLabel("Ngày");
                profitChart.getXAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
                profitChart.getXAxis().setTickLabelFont(Font.font(12));

                // Set font size for Y axis label and tick labels
                profitChart.getYAxis().setLabel("Doanh thu và vốn");
                profitChart.getYAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
                profitChart.getYAxis().setTickLabelFont(Font.font(12));

                lbRevenue.setText(priceFormat(Invoice_BUS.RevenueInfo(search).get("total").toString()));
                lbCapital.setText(priceFormat(GoodsReceipt_BUS.CapitalInfo(search).get("total").toString()));
                lbDiscount.setText(priceFormat(Invoice_BUS.RevenueInfo(search).get("discount").toString()));
                lbProfit.setText(priceFormat(Invoice_BUS.RevenueInfo(search).get("total").subtract(GoodsReceipt_BUS.CapitalInfo(search).get("total")).subtract(Invoice_BUS.RevenueInfo(search).get("discount")).toString()));
            }
        });
    }
    public void profitButtonClicked(){
        if (ValidationUtil.isEmptyDp(startDatePicker2,"Ngày bắt đầu") || ValidationUtil.isEmptyDp(endDatePicker2,"Ngày kết thúc")){
            return;
        }
        LocalDate start = startDatePicker2.getValue();
        LocalDate end = endDatePicker2.getValue();
        if (!ValidationUtil.isValidDateRange(start, end)) {
            return;
        }
        // Không được vượt quá 31 ngày
        if (ChronoUnit.DAYS.between(start, end) > 31) {
            ValidationUtil.showErrorAlert("Khoảng thời gian không được vượt quá 31 ngày");

            return;
        }
        search.clear();
        search.put("startDate",start.toString());
        search.put("endDate",end.toString());
        profitChart.getData().clear();
        ObservableList<XYChart.Series<String, Number>> combinedData = FXCollections.observableArrayList();
        combinedData.addAll(Invoice_BUS.getRevenueData(search));
        combinedData.addAll(GoodsReceipt_BUS.getCapitalData(search));
        profitChart.setData(combinedData);
        profitChart.setAnimated(false);
        profitChart.setStyle("-fx-font-size: 16px;");
        profitChart.setTitle("Doanh thu và vốn từ " + start + " đến " + end);
        profitChart.getXAxis().setLabel("Thời gian");
        profitChart.getXAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
        profitChart.getXAxis().setTickLabelFont(Font.font(12));

        // Set font size for Y axis label and tick labels
        profitChart.getYAxis().setLabel("Doanh thu và vốn");
        profitChart.getYAxis().lookup(".axis-label").setStyle("-fx-font-size: 14px;");
        profitChart.getYAxis().setTickLabelFont(Font.font(12));

        lbRevenue.setText(priceFormat(Invoice_BUS.RevenueInfo(search).get("total").toString()));
        lbCapital.setText(priceFormat(GoodsReceipt_BUS.CapitalInfo(search).get("total").toString()));
        lbDiscount.setText(priceFormat(Invoice_BUS.RevenueInfo(search).get("discount").toString()));
        lbProfit.setText(priceFormat(Invoice_BUS.RevenueInfo(search).get("total").subtract(GoodsReceipt_BUS.CapitalInfo(search).get("total")).subtract(Invoice_BUS.RevenueInfo(search).get("discount")).toString()));


    }
    
    public void refresh(){
        Invoice_BUS.getLocalData();
        InvoiceDetail_BUS.getLocalData();
        GoodsReceipt_BUS.getLocalData();
        GoodsReceiptDetail_BUS.getLocalData();
    }
}
