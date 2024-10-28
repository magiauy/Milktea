package milktea.milktea.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import milktea.milktea.BUS.Customer_BUS;
import milktea.milktea.DTO.Customer;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public void initialize() {
        btnAdd.setOnMouseClicked(this::btnAdd);
        btnEdit.setOnMouseClicked(this::btnEdit);
        btnSearch.setOnAction(this::btnSearch);
        initTable();

    }
    public void btnAdd(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("CustomerGUI_Add.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Add Customer");
            stage.setScene(scene);
            stage.setOnHidden(e -> {
                initTable();
            });
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void btnEdit(MouseEvent event){
        if(tableMain.getSelectionModel().getSelectedItem() != null){
            selectedCustomer = tableMain.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("CustomerGUI_Edit.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("Edit Customer");
                stage.setScene(scene);
                stage.setOnHidden(e -> {
                    initTable();
                });
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            errorAlert("Please select a customer");
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
    public void errorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void infoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
