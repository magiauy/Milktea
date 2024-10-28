package milktea.milktea.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class EmployeeMain {
    @FXML
    private Pane mainPane;
    @FXML
    private Label Userlabel;
    @FXML
    private Button btnInvoice;
    @FXML
    private Button btnProduct;
    @FXML
    private Button btnCustomer;
    @FXML
    private Button btnIngredient;
    @FXML
    private Button btnGoodsReceipt;
    @FXML
    private Button btnProvider;
    @FXML
    private Button btnInventory;
    @FXML
    private Button btnWasteReport;

    @FXML
    public void initialize() {
        Userlabel.setText("User: " + Login_Controller.account.getUsername());
        btnInvoice.setOnAction(this::btnInvoice);
        btnProduct.setOnAction(this::btnProduct);
        btnCustomer.setOnAction(this::btnCustomer);
        btnIngredient.setOnAction(this::btnIngredient);
        btnGoodsReceipt.setOnAction(this::btnGoodsReceipt);
        btnProvider.setOnAction(this::btnProvider);


    }
    public void btnInvoice(javafx.event.ActionEvent actionEvent) {
        loadFXML("Invoice_GUI.fxml");
    }
    public void btnProduct(javafx.event.ActionEvent actionEvent) {
        loadFXML("Product_GUI.fxml");
    }
    
    public void btnCustomer(javafx.event.ActionEvent actionEvent) {
        loadFXML("Customer_GUI.fxml");
    }
    
    public void btnIngredient(javafx.event.ActionEvent actionEvent) {
        loadFXML("Ingredient_GUI.fxml");
    }
    
    public void btnGoodsReceipt(javafx.event.ActionEvent actionEvent) {
        loadFXML("GoodsReceipt_GUI.fxml");
    }
    
    public void btnProvider(javafx.event.ActionEvent actionEvent) {
        loadFXML("Provider_GUI.fxml");
    }

    
    public void logout(){
        try {
            Stage stage = (Stage) Userlabel.getScene().getWindow();
            stage.close();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("LoginGUI.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Milktea_Store!");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            log.error("Error: ", e);
        }
    }














    public void loadFXML(String fxmlFile) {
    try {
        // Create a new FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        // Set the URL for the FXMLLoader to point to the new FXML file
        loader.setLocation(getClass().getResource(fxmlFile));
        System.out.println("Load FXML: " + fxmlFile);
        // Load the FXML content
        Pane newContent = loader.load();
        // Clear the main pane and add the new content
        mainPane.getChildren().clear();
        mainPane.getChildren().add(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
