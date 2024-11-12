package milktea.milktea.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import milktea.milktea.BUS.Customer_BUS;
import milktea.milktea.BUS.Employee_BUS;
import milktea.milktea.BUS.PromotionProgram_BUS;
import milktea.milktea.BUS.Promotion_BUS;
import milktea.milktea.DTO.Person;
import milktea.milktea.DTO.Promotion;
import milktea.milktea.Util.ValidationUtil;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SubGUIListInvoiceDetail {
    @FXML
    Label lblTitle;
    @FXML
    TextField txtSearch;
    @FXML
    Button btnSearch;
    @FXML
    TableView<Person> tableMain;
    @FXML
    TableColumn<Object, String> colID;
    @FXML
    TableColumn<Object, String> colFirstName;
    @FXML
    TableColumn<Object, String> colLastName;
    @FXML
    TableColumn<Object, String> colPhone;
    @FXML
    TableColumn<Object, String> colGender;
    @FXML
    Button btnAdd;

    TableView<Promotion> tablePromotion = new TableView<>();

    @FXML
    public void initialize() {
        switch (InvoiceGUI.getGlobalListFlag()) {
            case "Nhân Viên":
                lblTitle.setText("Danh sách nhân viên");
                createEmployeeTable();
                break;
            case "Khách Hàng":
                lblTitle.setText("Danh sách khách hàng");
                createCustomerTable();
                break;
            case "Khuyến Mãi":
                lblTitle.setText("Danh sách khuyến mãi");
                createPromotionTable();
                break;

        }
        btnAdd.setOnAction(this::btnAdd);
        btnSearch.setOnAction(this::btnSearch);
    }

    private void btnSearch(ActionEvent actionEvent) {
        if (InvoiceGUI.getGlobalListFlag().equals("Khách Hàng")){
            if (!ValidationUtil.isInvalidSearch(txtSearch)){
                ObservableList<Person> data = FXCollections.observableList(Customer_BUS.searchCustomer(txtSearch.getText()).stream()
                        .map(customer -> (Person) customer)
                        .collect(Collectors.toList()));
                tableMain.setItems(data);
            }
        }
    }

    private void btnAdd(ActionEvent actionEvent) {
        if (tableMain.getSelectionModel().getSelectedItem() != null || tablePromotion.getSelectionModel().getSelectedItem() != null) {
            if (InvoiceGUI.getGlobalListFlag().equals("Khuyến Mãi")) {
                if (PromotionProgram_BUS.checkInvalidDate(tablePromotion.getSelectionModel().getSelectedItem().getPromotionProgramId())) {
                    InvoiceGUI.setSelectedObject(tablePromotion.getSelectionModel().getSelectedItem());
                    Stage stage = (Stage) btnAdd.getScene().getWindow();
                    stage.close();
                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText("Khuyến mãi không hợp lệ");
                    alert.setContentText("Khuyến mãi đã hết hạn hoặc chưa đến thời gian áp dụng");
                    alert.showAndWait();
                }
            } else {
                InvoiceGUI.setSelectedObject(tableMain.getSelectionModel().getSelectedItem());
                Stage stage = (Stage) btnAdd.getScene().getWindow();
                stage.close();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Chưa chọn dữ liệu");
            alert.setContentText("Vui lòng chọn dữ liệu trước khi thêm");
            alert.showAndWait();
        }
    }

    public void createEmployeeTable() {
        initTable();
        ObservableList<Person> data = FXCollections.observableList(Employee_BUS.getAllEmployee().stream()
            .map(employee -> (Person) employee)
            .collect(Collectors.toList()));
            tableMain.setItems(data);
    }
    public void createCustomerTable() {
        initTable();
        ObservableList<Person> data = FXCollections.observableList(Customer_BUS.getAllCustomer().stream()
                .map(customer -> (Person) customer)
                .collect(Collectors.toList()));
        tableMain.setItems(data);
    }

    private void initTable() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
    }
    @SuppressWarnings("unchecked")
    public void createPromotionTable(){
        tablePromotion.setLayoutX(59);
        tablePromotion.setLayoutY(107);
        tablePromotion.setPrefHeight(521);
        tablePromotion.setPrefWidth(482);
        TableColumn<Promotion, String> colPromotionId = new TableColumn<>("Mã khuyến mãi");
        colPromotionId.setPrefWidth(160);
        TableColumn<Promotion, String> colPromotionDiscount = new TableColumn<>("Giảm giá");
        colPromotionDiscount.setPrefWidth(160);
        TableColumn<Promotion, String> colPromotionMinimum = new TableColumn<>("Tối thiểu");
        colPromotionMinimum.setPrefWidth(160);
        tablePromotion.getColumns().addAll(colPromotionId, colPromotionDiscount, colPromotionMinimum);

        colPromotionId.setCellValueFactory(new PropertyValueFactory<>("promotionId"));
        colPromotionDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colPromotionMinimum.setCellValueFactory(new PropertyValueFactory<>("minimumPrice"));

        ArrayList<Promotion> arrPromotion = Promotion_BUS.getAllPromotion();
        arrPromotion.removeIf(promotion -> promotion.getPromotionId().equals("NoPromotion"));
        ObservableList<Promotion> data = FXCollections.observableList(arrPromotion);
        tablePromotion.setItems(data);
        switchTable(tablePromotion);
    }
    public void switchTable(TableView<Promotion> table){
        AnchorPane parent = (AnchorPane) tableMain.getParent();
        int index = parent.getChildrenUnmodifiable().indexOf(tableMain);
        parent.getChildren().set(index, table);
    }
}
