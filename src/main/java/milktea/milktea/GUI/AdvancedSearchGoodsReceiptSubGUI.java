package milktea.milktea.GUI;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import milktea.milktea.BUS.GoodsReceipt_BUS;
import milktea.milktea.DTO.GoodsReceipt;
import milktea.milktea.Util.ValidationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class AdvancedSearchGoodsReceiptSubGUI {

    @FXML
    private TextField txtGoodsReceiptID;
    @FXML
    private DatePicker dtpGoodsReceiptStartDate;
    @FXML
    private DatePicker dtpGoodsReceiptEndDate;
    @FXML
    private TextField txtGoodsReceiptMinTotal;
    @FXML
    private TextField txtGoodsReceiptMaxTotal;
    @FXML
    private TextField txtGoodsReceiptProviderID;
    @FXML
    private TextField txtGoodsReceiptEmployeeID;

    @FXML
    CheckBox chkGoodsReceiptID;
    @FXML
    CheckBox chkGoodsReceiptDate;
    @FXML
    CheckBox chkGoodsReceiptTotal;
    @FXML
    CheckBox chkGoodsReceiptProviderID;
    @FXML
    CheckBox chkGoodsReceiptEmployeeID;

    @FXML
    Button btnSearch;

    @Getter
    @Setter
    private static boolean isDone = false;
    private boolean isSearch = true;
    @Getter
    @Setter
    private static ArrayList<GoodsReceipt> arrGoodsReceipt;
    @FXML
    public void initialize() {
        chkGoodsReceiptDate.setOnAction(this::checkBoxAction);
        chkGoodsReceiptEmployeeID.setOnAction(this::checkBoxAction);
        chkGoodsReceiptID.setOnAction(this::checkBoxAction);
        chkGoodsReceiptProviderID.setOnAction(this::checkBoxAction);
        chkGoodsReceiptTotal.setOnAction(this::checkBoxAction);
        btnSearch.setOnAction(this::search);
    }
    private void search(ActionEvent actionEvent) {
        HashMap<String, String> searchParams = getSearchParams();
        if (isSearch) {
            if (!searchParams.isEmpty()) {
                    arrGoodsReceipt = GoodsReceipt_BUS.advancedSearchGoodsReceipt(searchParams);
                if (arrGoodsReceipt.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Không tìm thấy kết quả phù hợp");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Tìm thấy " + arrGoodsReceipt.size() + " kết quả phù hợp");
                    alert.showAndWait();
                    isDone = true;
                    Stage stage = (Stage) btnSearch.getScene().getWindow();
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
        if(chkGoodsReceiptID.isSelected()){
            if (!ValidationUtil.isEmpty(txtGoodsReceiptID)){
                if (!ValidationUtil.isInvalidSearch(txtGoodsReceiptID)) {
                    if (!ValidationUtil.isFirstCharNotSpace(txtGoodsReceiptID)) {
                        System.out.println(txtGoodsReceiptID.getText());
                        searchParams.put("goodsReceiptID", txtGoodsReceiptID.getText());
                    }else {
                        isSearch = false;
                    }
                }else {
                    isSearch = false;
                }
            }else {
                isSearch = false;
            }
        }if (chkGoodsReceiptDate.isSelected()){
            if (!ValidationUtil.isEmptyDp(dtpGoodsReceiptStartDate,"bắt đầu") && ValidationUtil.isEmptyDp(dtpGoodsReceiptEndDate,"kết thúc")){
                searchParams.put("startDate", dtpGoodsReceiptStartDate.getValue().toString());
                searchParams.put("endDate", dtpGoodsReceiptEndDate.getValue().toString());
            }else {
                isSearch = false;
            }
        }if (chkGoodsReceiptTotal.isSelected()){
            if (!ValidationUtil.isEmpty(txtGoodsReceiptMinTotal,txtGoodsReceiptMaxTotal)){
                if (!ValidationUtil.isNotPrice(txtGoodsReceiptMinTotal,txtGoodsReceiptMaxTotal)) {
                    searchParams.put("minTotal", txtGoodsReceiptMinTotal.getText());
                    searchParams.put("maxTotal", txtGoodsReceiptMaxTotal.getText());
                }else {
                    isSearch = false;
                }
            }else {
                isSearch = false;
            }
        }if (chkGoodsReceiptProviderID.isSelected()){
            if (!ValidationUtil.isEmpty(txtGoodsReceiptProviderID)) {
                if (!ValidationUtil.isInvalidSearch(txtGoodsReceiptProviderID)) {
                    if (!ValidationUtil.isFirstCharNotSpace(txtGoodsReceiptProviderID)) {
                        searchParams.put("providerId", txtGoodsReceiptProviderID.getText());
                    }else {
                        isSearch = false;
                    }
                }else {
                    isSearch = false;
                }
            }else {
                isSearch = false;
            }
        }if (chkGoodsReceiptEmployeeID.isSelected()){
            if (!ValidationUtil.isEmpty(txtGoodsReceiptEmployeeID)){
                if (!ValidationUtil.isInvalidSearch(txtGoodsReceiptEmployeeID)) {
                    if (!ValidationUtil.isFirstCharNotSpace(txtGoodsReceiptEmployeeID)) {
                        searchParams.put("employeeId", txtGoodsReceiptEmployeeID.getText());
                    }else {
                        isSearch = false;
                    }
                }else {
                    isSearch = false;
                }
            }else {
                isSearch = false;
            }
        }
        return searchParams;
    }
    public void checkBoxAction(Event event) {
        CheckBox checkBox = (CheckBox) event.getSource();
            switch (checkBox.getId()) {
                case "chkGoodsReceiptID":
                    txtGoodsReceiptID.setDisable(!checkBox.isSelected());
                    break;
                case "chkGoodsReceiptDate":
                    dtpGoodsReceiptStartDate.setDisable(!checkBox.isSelected());
                    dtpGoodsReceiptEndDate.setDisable(!checkBox.isSelected());
                    break;
                case "chkGoodsReceiptTotal":
                    txtGoodsReceiptMinTotal.setDisable(!checkBox.isSelected());
                    txtGoodsReceiptMaxTotal.setDisable(!checkBox.isSelected());
                    break;
                case "chkGoodsReceiptProviderID":
                    txtGoodsReceiptProviderID.setDisable(!checkBox.isSelected());
                    break;
                case "chkGoodsReceiptEmployeeID":
                    txtGoodsReceiptEmployeeID.setDisable(!checkBox.isSelected());
                    break;
            }
    }
}