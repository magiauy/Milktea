package milktea.milktea.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import milktea.milktea.BUS.*;
import milktea.milktea.Util.ValidationUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Slf4j
public class Main {
    @FXML
    private VBox btnGroup;
    @FXML
    private Pane mainPane;
    @FXML
    private Label Userlabel;
    @FXML
    private Button btnInvoice = new Button();
    @FXML
    private Button btnProduct = new Button();
    @FXML
    private Button btnCustomer  = new Button();
    @FXML
    private Button btnIngredient = new Button();
    @FXML
    private Button btnGoodsReceipt = new Button();
    @FXML
    private Button btnProvider = new Button();
    @FXML
    private Button btnPromotion = new Button();
    @FXML
    private Button btnStatistic = new Button();
    @FXML
    private Button btnEmployee = new Button();
    static boolean isLoaded = false;
    HashMap<Button, Integer> buttonHashMap = new LinkedHashMap<>();

    @FXML
    public void initialize() {
        btnGroup.getChildren().clear();
        Userlabel.setText("User: " + Login_Controller.getAccount().getUsername());
        configButton();
        loadButtonByPermission();
        loadLocalDate();

    }

    private void loadLocalDate() {
        if (!isLoaded) {
            Product_BUS.getLocalData();
            Customer_BUS.getLocalData();
            Invoice_BUS.getLocalData();
            InvoiceDetail_BUS.getLocalData();
            Promotion_BUS.getLocalData();
            PromotionProgram_BUS.getLocalData();
            Category_BUS.getLocalData();
            Ingredient_BUS.getLocalData();
            Recipe_BUS.getLocalData();
            GoodsReceipt_BUS.getLocalData();
            GoodsReceiptDetail_BUS.getLocalData();
            Provider_BUS.getLocalData();
            Employee_BUS.getLocalData();
            Role_BUS.getLocalData();
            isLoaded = true;
        }

    }

    public void btnInvoice(javafx.event.ActionEvent actionEvent) {
        loadTabFXML("Invoice_GUI.fxml");
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
    public void configButton(){
        btnInvoice.setOnAction(this::btnInvoice);
        btnProduct.setOnAction(this::btnProduct);
        btnCustomer.setOnAction(this::btnCustomer);
        btnIngredient.setOnAction(this::btnIngredient);
        btnGoodsReceipt.setOnAction(this::btnGoodsReceipt);
        btnProvider.setOnAction(this::btnProvider);
        btnPromotion.setOnAction(this::btnPromotion);
        btnStatistic.setOnAction(this::btnStatistic);
        btnEmployee.setOnAction(this::btnEmployee);

        btnInvoice.setText("Hoá đơn");
        btnProduct.setText("Sản phẩm");
        btnCustomer.setText("Khách hàng");
        btnIngredient.setText("Nguyên liệu");
        btnGoodsReceipt.setText("Phiếu nhập");
        btnProvider.setText("Nhà cung cấp");
        btnPromotion.setText("Khuyến mãi");
        btnStatistic.setText("Thống kê");
        btnEmployee.setText("Nhân viên");

        buttonHashMap.put(btnInvoice, 7);
        buttonHashMap.put(btnCustomer, 1);
        buttonHashMap.put(btnProduct, 2);
        buttonHashMap.put(btnIngredient, 3);
        buttonHashMap.put(btnGoodsReceipt, 8);
        buttonHashMap.put(btnProvider, 5);
        buttonHashMap.put(btnPromotion, 6);
        buttonHashMap.put(btnStatistic, 9);
        buttonHashMap.put(btnEmployee, 10);



    }

    private void btnEmployee(ActionEvent actionEvent) {
        loadFXML("Employee_GUI.fxml");
    }

    private void btnStatistic(ActionEvent actionEvent) {
        loadFXML("Statistics_GUI.fxml");
    }

    private void btnPromotion(ActionEvent actionEvent) {
        loadFXML("Promotion_GUI.fxml");
    }

    public void loadButtonByPermission() {
        Button[] buttons = {btnInvoice, btnCustomer, btnProduct,  btnIngredient, btnGoodsReceipt, btnProvider, btnPromotion, btnStatistic, btnEmployee};
        int permission = Login_Controller.getAccount().getPermission();
        int firstButton = 0;
        for (Button button : buttons) {
            if (checkRolePermission(permission, buttonHashMap.get(button))) {
                btnGroup.getChildren().add(button);
                if (firstButton == 0) {
                    formatFirstButton(button);
                    firstButton = 1;
                } else {
                    formatButton(button);
                }
            } else if (buttonHashMap.get(button) == 7) {
                if (checkRolePermission(permission,0)){
                    btnGroup.getChildren().add(button);
                    if (firstButton == 0) {
                        formatFirstButton(button);
                        firstButton = 1;
                    } else {
                        formatButton(button);
                    }
                }

            } else if (buttonHashMap.get(button) == 8) {
                if (checkRolePermission(permission,4)){
                    btnGroup.getChildren().add(button);
                    if (firstButton == 0) {
                        formatFirstButton(button);
                        firstButton = 1;
                    } else {
                        formatButton(button);
                    }
                }

            }
        }
    }
    public static boolean checkRolePermission(int permission,int bitPosition){
        return (permission & (1<<bitPosition)) != 0;
    }
    public void formatButton(Button button){
        button.setStyle("-fx-background-color: null;" +
                " -fx-text-fill: black; " +
                "-fx-font-size: 25px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 5px 10px; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 0 0 1 0;");
        button.setPrefSize(200, 60);
        button.setCursor(javafx.scene.Cursor.HAND);
        button.getStyleClass().add("custom-button");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #f0f0f0;" +
                " -fx-text-fill: black; " +
                "-fx-font-size: 25px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 5px 10px; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 0 0 1 0;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: null;" +
                " -fx-text-fill: black; " +
                "-fx-font-size: 25px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 5px 10px; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 0 0 1 0;"));
    }
    public void formatFirstButton(Button button){
        button.setStyle("-fx-background-color: null;" +
                " -fx-text-fill: black; " +
                "-fx-font-size: 25px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 5px 10px; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 1 0 1 0;");
        button.setPrefSize(200, 60);
        button.setCursor(javafx.scene.Cursor.HAND);
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #f0f0f0;" +
                " -fx-text-fill: black; " +
                "-fx-font-size: 25px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 5px 10px; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 1 0 1 0;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: null;" +
                " -fx-text-fill: black; " +
                "-fx-font-size: 25px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 5px 10px; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 1 0 1 0;"));
    }
    public void logout(){
        if (ValidationUtil.showConfirmAlert("Bạn có chắc chắn muốn đăng xuất?")) {
            try {
                Stage stage = (Stage) Userlabel.getScene().getWindow();
                stage.close();
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("LoginGUI.fxml"));

                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("MilkTea_Store!");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                log.error("Error: ", e);
            }
        }
    }
    public void loadTabFXML(String fxmlFile) {
    try {
        // Create a new FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        // Set the URL for the FXMLLoader to point to the new FXML file
        loader.setLocation(getClass().getResource(fxmlFile));
        // Load the FXML content
        TabPane newContent = loader.load();
        // Clear the main pane and add the new content
        mainPane.getChildren().clear();
        mainPane.getChildren().add(newContent);
    } catch (IOException e) {
        log.error("Error: ", e);
    } catch (ClassCastException e) {
        log.error("ClassCastException: ", e);
    }
}
    public void loadFXML(String fxmlFile) {
    try {
        // Create a new FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        // Set the URL for the FXMLLoader to point to the new FXML file
        loader.setLocation(getClass().getResource(fxmlFile));
        // Load the FXML content
        Pane newContent = loader.load();
        // Clear the main pane and add the new content
        mainPane.getChildren().clear();
        mainPane.getChildren().add(newContent);
        } catch (IOException e) {
            log.error("Error: ", e);
        }catch (ClassCastException e) {
        log.error("ClassCastException: ", e);
    }
    }
}
