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
import milktea.milktea.DTO.Customer;
import milktea.milktea.Util.UI_Util;
import milktea.milktea.Util.ValidationUtil;

import java.util.ArrayList;
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

    public static Customer selectedCustomer = null;
    public static boolean isEditable = false;
    public void initialize() {
        btnAdd.setOnMouseClicked(this::btnAdd);
        btnEdit.setOnMouseClicked(this::btnEdit);
        btnSearch.setOnAction(this::btnSearch);

        initTable();

    }
    public void btnAdd(MouseEvent event) {
        isEditable = false;
        UI_Util.openStage("CustomerSubGUI.fxml", () -> {
            ObservableList<Customer> updatedData = FXCollections.observableList(Customer_BUS.getAllCustomer());
            tableMain.setItems(updatedData);
            tableMain.refresh();
        });
    }
    public void btnEdit(MouseEvent event){
        isEditable = true;
        if(tableMain.getSelectionModel().getSelectedItem() != null){
            selectedCustomer = tableMain.getSelectionModel().getSelectedItem();
            UI_Util.openStage("CustomerSubGUI.fxml",() -> {
                ObservableList<Customer> updatedData = FXCollections.observableList(Customer_BUS.getAllCustomer());
                tableMain.setItems(updatedData);
                tableMain.refresh();
            });
        }else {
            ValidationUtil.showErrorAlert("Please select a customer");
        }
    }
    public void btnSearch(ActionEvent event){

    }
    public void initTable(){
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colPoint.setCellValueFactory(new PropertyValueFactory<>("point"));
        ObservableList<Customer> data = FXCollections.observableList(Customer_BUS.getAllCustomer());
        tableMain.setItems(data);
    }



}

