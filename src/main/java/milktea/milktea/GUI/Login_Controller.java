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
import milktea.milktea.BUS.Role_BUS;
import milktea.milktea.DTO.Employee;

import java.io.IOException;
import java.util.Objects;

import milktea.milktea.BUS.Employee_BUS;
import javafx.stage.Stage;
import milktea.milktea.Util.ValidationUtil;

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
        txtUsername.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("TAB")) {
                txtPassword.requestFocus();
            }
        });
        txtPassword.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                btnLogin(new ActionEvent());
            }
        });
        Employee_BUS.getLocalData();
    }
    public void btnLogin(ActionEvent actionEvent) {
        if (validate()) {
            ValidationUtil.showInfoAlert("Login successful");
            System.out.println("Login successful");
            account = Employee_BUS.getEmployeeByUsername(txtUsername.getText());
            assert account != null;
            account.setPermission(Role_BUS.getAccessById(account.getRole()));
            openStage("Main.fxml");
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
            ValidationUtil.showErrorAlert("Username and Password are required");
            alert = true;
        valid = false;
    }
    if (Objects.isNull(txtUsername.getText()) || txtUsername.getText().isEmpty()) {
        txtUsername.getStyleClass().add("error");
        if (!alert) {
        ValidationUtil.showErrorAlert("Username is required");
            alert = true;
        }
        valid = false;
    }
    if (Objects.isNull(txtPassword.getText()) || txtPassword.getText().isEmpty()) {
        txtPassword.getStyleClass().add("error");
        if (!alert) {
        ValidationUtil.showErrorAlert("Password is required");
            alert = true;
        }
        valid = false;
    }
    if (!Employee_BUS.checkInvalidUsername(txtUsername.getText())) {
        txtUsername.getStyleClass().add("error");
        if (!alert) {
        ValidationUtil.showErrorAlert("Username is invalid");
            alert = true;
        }
        valid = false;
    }
    if (!Employee_BUS.checkLogin(txtUsername.getText(), txtPassword.getText())) {
        txtPassword.getStyleClass().add("error");
        if (!alert) {
        ValidationUtil.showErrorAlert("Password is invalid");
        }
        valid = false;
    }
    return valid;
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
