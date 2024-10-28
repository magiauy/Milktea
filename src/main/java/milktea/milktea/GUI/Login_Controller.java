package milktea.milktea.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lombok.extern.slf4j.Slf4j;
import milktea.milktea.DTO.Employee;
import milktea.milktea.DTO.Role;
import org.controlsfx.validation.Validator;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import milktea.milktea.BUS.Employee_BUS;
import javafx.stage.Stage;

@Slf4j
public class Login_Controller {
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnLogin;

    public static Employee account = null;

    @FXML
    public void initialize() {
        btnLogin.setOnAction(this::btnLogin);
    }



    public void tab(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("TAB")) {
            txtPassword.requestFocus();
        }
    }

    public void btnLogin(ActionEvent actionEvent) {
        if (validate()) {
            infoAlert("Login successful");
            System.out.println("Login successful");
            account = Employee_BUS.getEmployeeByUsername(txtUsername.getText());
            selectedScreen();
        }

    }
    public void loginenter(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            btnLogin(null);
        }    }

    public void enter(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            btnLogin(null);
        }
    }

public boolean validate() {
    boolean valid = true;
    boolean alert = false;
    // Clear previous error styles and tooltips
        txtUsername.getStyleClass().remove("error");
    txtPassword.getStyleClass().remove("error");
    txtUsername.applyCss();
    txtPassword.applyCss();
    if ((Objects.isNull(txtUsername.getText()) || txtUsername.getText().isEmpty())&&(Objects.isNull(txtPassword.getText()) || txtPassword.getText().isEmpty())) {
        txtUsername.getStyleClass().add("error");
        txtPassword.getStyleClass().add("error");
            errorAlert("Username and Password are required");
            alert = true;
        valid = false;
    }
    if (Objects.isNull(txtUsername.getText()) || txtUsername.getText().isEmpty()) {
        txtUsername.getStyleClass().add("error");
        if (!alert) {
        errorAlert("Username is required");
            alert = true;
        }
        valid = false;
    }
    if (Objects.isNull(txtPassword.getText()) || txtPassword.getText().isEmpty()) {
        txtPassword.getStyleClass().add("error");
        if (!alert) {
        errorAlert("Password is required");
            alert = true;
        }
        valid = false;
    }
    if (!Employee_BUS.checkInvalidUsername(txtUsername.getText())) {
        txtUsername.getStyleClass().add("error");
        if (!alert) {
        errorAlert("Username is invalid");
            alert = true;
        }
        valid = false;
    }
    if (!Employee_BUS.checkLogin(txtUsername.getText(), txtPassword.getText())) {
        txtPassword.getStyleClass().add("error");
        if (!alert) {
        errorAlert("Password is invalid");
        }
        valid = false;
    }
    return valid;
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
    public void selectedScreen() {
        switch (account.getRole()) {
            case Role.ADMIN:
                openStage("Admin_Main.fxml");
                break;
            case Role.MANAGER:
                openStage("Manager_Main.fxml");
                break;
            case Role.EMPLOYEE:
                openStage("Employee_Main.fxml");
                break;
            default:
                break;
        }
    }
    public void openStage(String fxmlFile) {
    try {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Login_Controller.class.getResource(fxmlFile));
        Parent root = loader.load();
        AnchorPane anchorPane = (AnchorPane) root;

        Stage stage = (Stage) btnLogin.getScene().getWindow();stage.close();
        stage.setTitle("MilkTea_Store!"); // Set an appropriate title
        stage.setScene(new Scene(anchorPane));

        stage.show(); // Display the stage

    } catch (IOException e) {
        log.error("Error",e);
    }
    }
}
