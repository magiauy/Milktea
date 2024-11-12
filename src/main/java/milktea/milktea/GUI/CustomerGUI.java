package milktea.milktea.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import lombok.extern.slf4j.Slf4j;
import milktea.milktea.BUS.Customer_BUS;
import milktea.milktea.BUS.Invoice_BUS;
import milktea.milktea.DTO.Customer;
import milktea.milktea.Util.UI_Util;
import milktea.milktea.Util.ValidationUtil;

import java.util.ArrayList;
import java.util.Comparator;

@Slf4j
public class CustomerGUI {
    @FXML
    private ImageView btnAdd;
    @FXML
    private ImageView btnEdit;
    @FXML
    private Button btnSearch;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<Customer> tableMain;
    @FXML
    private TableColumn<Customer, String> colID;
    @FXML
    private TableColumn<Customer, String> colFirstName;
    @FXML
    private TableColumn<Customer, String> colLastName;
    @FXML
    private TableColumn<Customer, String> colPhone;
    @FXML
    private TableColumn<Customer, String> colGender;
    @FXML
    private TableColumn<Customer, String> colPoint;

    @FXML
    ImageView btnDelete;

    public static Customer selectedCustomer = null;
    public static boolean isEditable = false;
    public void initialize() {
        btnAdd.setOnMouseClicked(this::btnAdd);
        btnEdit.setOnMouseClicked(this::btnEdit);
        btnSearch.setOnAction(this::btnSearch);
        initTable();
        btnDelete.setOnMouseClicked(this::btnDelete);
    }

    private void btnDelete(MouseEvent event) {
        if(tableMain.getSelectionModel().getSelectedItem() != null){
            if (ValidationUtil.showConfirmAlert("Bạn có chắc chắn muốn xóa khách hàng này không?")) {
                Customer selectedCustomer = tableMain.getSelectionModel().getSelectedItem();
                if (!Invoice_BUS.isCustomerExist(selectedCustomer.getId())) {
                    if (Customer_BUS.deleteCustomer(selectedCustomer.getId())) {
                        Customer_BUS.deleteCustomerLocal(selectedCustomer.getId());
                        ArrayList<Customer> customers = new ArrayList<>(Customer_BUS.getAllCustomer());
                        customers.sort(Comparator.comparing(Customer::getId));
                        ObservableList<Customer> sortedData = FXCollections.observableList(customers);
                        if (!sortedData.isEmpty()) {
                            sortedData.removeIf(customer -> customer.getId().equals("KH000"));
                        }
                        tableMain.setItems(sortedData);
                        tableMain.getSelectionModel().clearSelection();
                        tableMain.refresh();
                    } else {
                        ValidationUtil.showErrorAlert("Xóa không thành công");
                    }
                } else {
                    ValidationUtil.showErrorAlert("Không thể xóa khách hàng này vì đã mua hàng");
                }
            }
        }else {
            ValidationUtil.showErrorAlert("Vui lòng chọn khách hàng cần xóa");
        }
    }

    public void btnAdd(MouseEvent event) {
        isEditable = false;
        UI_Util.openStage("CustomerSubGUI.fxml", () -> {
            ArrayList<Customer> customers = new ArrayList<>(Customer_BUS.getAllCustomer());
            customers.sort(Comparator.comparing(Customer::getId));
            ObservableList<Customer> sortedData = FXCollections.observableList(customers);
            if (!sortedData.isEmpty()) {
                sortedData.removeIf(customer -> customer.getId().equals("KH000"));
            }
            tableMain.setItems(sortedData);
            tableMain.refresh();
        });
    }
    public void btnEdit(MouseEvent event){
        isEditable = true;
        if(tableMain.getSelectionModel().getSelectedItem() != null){
            selectedCustomer = tableMain.getSelectionModel().getSelectedItem();
            UI_Util.openStage("CustomerSubGUI.fxml",() -> {
                ArrayList<Customer> customers = new ArrayList<>(Customer_BUS.getAllCustomer());
                customers.sort(Comparator.comparing(Customer::getId));
                ObservableList<Customer> sortedData = FXCollections.observableList(customers);
                if (!sortedData.isEmpty()) {
                    sortedData.removeIf(customer -> customer.getId().equals("KH000"));
                }
                tableMain.setItems(sortedData);
                tableMain.refresh();
            });
        }else {
            ValidationUtil.showErrorAlert("Vui lòng chọn khách hàng cần sửa");
        }
    }
    public void btnSearch(ActionEvent event){
        if (!ValidationUtil.isInvalidSearch(txtSearch)) {
            String search = txtSearch.getText();
            ArrayList<Customer> customers = new ArrayList<>(Customer_BUS.searchCustomer(search));
            customers.sort(Comparator.comparing(Customer::getId));
            ObservableList<Customer> sortedData = FXCollections.observableList(customers);
            if (!sortedData.isEmpty()) {
                sortedData.removeIf(customer -> customer.getId().equals("KH000"));
            }
            tableMain.setItems(sortedData);
        }
    }
    public void initTable(){
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colPoint.setCellValueFactory(new PropertyValueFactory<>("point"));
        ArrayList<Customer> customers = new ArrayList<>(Customer_BUS.getAllCustomer());
        customers.sort(Comparator.comparing(Customer::getId));
        ObservableList<Customer> sortedData = FXCollections.observableList(customers);
        if (!sortedData.isEmpty()) {
            sortedData.removeIf(customer -> customer.getId().equals("KH000"));
        }
        tableMain.setItems(sortedData);
    }



}

