package milktea.milktea.GUI;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import milktea.milktea.BUS.Invoice_BUS;
import milktea.milktea.DTO.Invoice;
import milktea.milktea.Util.ValidationUtil;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class AdvancedSearchInvoiceSubGUI {
    @FXML
    private TextField txtSearchInvoiceID;
    @FXML
    private TextField txtSearchInvoiceStartDate;
    @FXML
    private TextField txtSearchInvoiceEndDate;
    @FXML
    private TextField txtSearchInvoiceMinTotal;
    @FXML
    private TextField txtSearchInvoiceMaxTotal;
    @FXML
    private TextField txtSearchInvoiceCustomerID;
    @FXML
    private TextField txtSearchInvoiceEmployeeID;
    @FXML
    private TextField txtSearchInvoicePromotionID;

    @FXML
    private CheckBox chkSearchInvoiceID;
    @FXML
    private CheckBox chkSearchInvoiceDate;
    @FXML
    private CheckBox chkSearchInvoiceTotal;
    @FXML
    private CheckBox chkSearchInvoiceCustomerID;
    @FXML
    private CheckBox chkSearchInvoiceEmployeeID;
    @FXML
    private CheckBox chkSearchInvoicePromotionID;

    @FXML
    private Button btnSearchInvoice;

    @Getter
    @Setter
    private static ArrayList<Invoice> arrInvoice;
    @Getter
    @Setter
    private static boolean isDone = false;
    private boolean isSearch = true;
    @FXML
    public void initialize() {
        btnSearchInvoice.setOnAction(this::searchInvoice);
        chkSearchInvoiceCustomerID.setOnAction(this::checkBoxAction);
        chkSearchInvoiceDate.setOnAction(this::checkBoxAction);
        chkSearchInvoiceEmployeeID.setOnAction(this::checkBoxAction);
        chkSearchInvoiceID.setOnAction(this::checkBoxAction);
        chkSearchInvoicePromotionID.setOnAction(this::checkBoxAction);
        chkSearchInvoiceTotal.setOnAction(this::checkBoxAction);

    }
    public void searchInvoice(Event event) {
        HashMap<String, String> searchParams = getSearchParams();
        if (isSearch) {
            if (!searchParams.isEmpty()) {
                arrInvoice = Invoice_BUS.advancedSearchInvoice(searchParams);
                if (arrInvoice.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Không tìm thấy kết quả phù hợp");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Tìm thấy " + arrInvoice.size() + " kết quả phù hợp");
                    alert.showAndWait();
                    isDone = true;
                    Stage stage = (Stage) btnSearchInvoice.getScene().getWindow();
                    stage.close();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn ít nhất một trường tìm kiếm");
                alert.showAndWait();
            }
        }
    }

    private @NotNull HashMap<String, String> getSearchParams() {
    HashMap<String, String> searchParams = new HashMap<>();
    if (chkSearchInvoiceID.isSelected() && isValidSearchField(txtSearchInvoiceID)) {
        searchParams.put("invoiceID", txtSearchInvoiceID.getText());
    }else {
        isSearch = false;
    }
    if (chkSearchInvoiceDate.isSelected() && isValidDateRange(txtSearchInvoiceStartDate, txtSearchInvoiceEndDate)) {
        searchParams.put("startDate", txtSearchInvoiceStartDate.getText());
        searchParams.put("endDate", txtSearchInvoiceEndDate.getText());
    }else {
        isSearch = false;
    }
    if (chkSearchInvoiceTotal.isSelected() && isValidTotalRange(txtSearchInvoiceMinTotal, txtSearchInvoiceMaxTotal)) {
        searchParams.put("minTotal", txtSearchInvoiceMinTotal.getText());
        searchParams.put("maxTotal", txtSearchInvoiceMaxTotal.getText());
    }else {
        isSearch = false;
    }
    if (chkSearchInvoiceCustomerID.isSelected() && isValidSearchField(txtSearchInvoiceCustomerID)) {
        searchParams.put("customerID", txtSearchInvoiceCustomerID.getText());
    }else {
        isSearch = false;
    }
    if (chkSearchInvoiceEmployeeID.isSelected() && isValidSearchField(txtSearchInvoiceEmployeeID)) {
        searchParams.put("employeeID", txtSearchInvoiceEmployeeID.getText());
    }else {
        isSearch = false;
    }
    if (chkSearchInvoicePromotionID.isSelected() && isValidSearchField(txtSearchInvoicePromotionID)) {
        searchParams.put("promotionID", txtSearchInvoicePromotionID.getText());
    }else {
        isSearch = false;
    }
    return searchParams;
}

    private boolean isValidSearchField(TextField textField) {
        return !ValidationUtil.isEmpty(textField) && !ValidationUtil.isInvalidSearch(textField) && !ValidationUtil.isFirstCharNotSpace(textField);
    }

    private boolean isValidDateRange(TextField startDate, TextField endDate) {
        return !ValidationUtil.isEmpty(startDate, endDate) && ValidationUtil.isValidDateRange(LocalDate.parse(startDate.getText()), LocalDate.parse(endDate.getText()));
    }

    private boolean isValidTotalRange(TextField minTotal, TextField maxTotal) {
        return !ValidationUtil.isEmpty(minTotal, maxTotal) && !ValidationUtil.isNotPrice(minTotal, maxTotal) && ValidationUtil.isValidTotalRange(minTotal, maxTotal);
    }
    public void checkBoxAction(Event event) {
        CheckBox checkBox = (CheckBox) event.getSource();
        switch (checkBox.getId()) {
            case "chkSearchInvoiceID":
                txtSearchInvoiceID.setDisable(!checkBox.isSelected());
                break;
            case "chkSearchInvoiceDate":
                    txtSearchInvoiceStartDate.setDisable(!checkBox.isSelected());
                    txtSearchInvoiceEndDate.setDisable(!checkBox.isSelected());
                break;
            case "chkSearchInvoiceTotal":
                txtSearchInvoiceMinTotal.setDisable(!checkBox.isSelected());
                txtSearchInvoiceMaxTotal.setDisable(!checkBox.isSelected());
                break;
            case "chkSearchInvoiceCustomerID":
                txtSearchInvoiceCustomerID.setDisable(!checkBox.isSelected());
                break;
            case "chkSearchInvoiceEmployeeID":
                txtSearchInvoiceEmployeeID.setDisable(!checkBox.isSelected());
                break;
            case "chkSearchInvoicePromotionID":
                txtSearchInvoicePromotionID.setDisable(!checkBox.isSelected());
                break;
        }
    }
}
