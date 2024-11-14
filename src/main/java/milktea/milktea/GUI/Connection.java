package milktea.milktea.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import milktea.milktea.BUS.Connect_BUS;
import milktea.milktea.DTO.MySQLConfig;
import milktea.milktea.Util.ValidationUtil;

import java.io.IOException;

@Slf4j
public class Connection {
    @FXML
    private TextField txtHost;
    @FXML
    private TextField txtPort;
    @FXML
    private TextField txtUser;
    @FXML
    private TextField txtPassword;
    @FXML
    private CheckBox cbPassword;
    @FXML
    private Label lblNotification;
    @FXML
    private Button btnCheckConnection;
    @FXML
    private Button btnSave;

    @FXML
    public void initialize() {
        btnCheckConnection.setOnAction(this::btnCheckConnection);
        btnSave.setOnAction(this::btnSave);
        cbPassword.setOnAction(this::cbPassword);
        txtPassword.setDisable(true);
        btnSave.setDisable(true);
        txtHost.setText("localhost");
        txtPort.setText("3306");
        txtHost.textProperty().addListener((observable, oldValue, newValue) -> disableWithChange());
        txtPort.textProperty().addListener((observable, oldValue, newValue) -> disableWithChange());
        txtUser.textProperty().addListener((observable, oldValue, newValue) -> disableWithChange());
        txtPassword.textProperty().addListener((observable, oldValue, newValue) -> disableWithChange());
    }
    public void disableWithChange(){
        lblNotification.setText("Vui lòng kiểm tra kết nối!");
        lblNotification.setStyle("-fx-text-fill: black");
        btnSave.setDisable(true);
    }
    private void cbPassword(ActionEvent actionEvent) {
        txtPassword.setDisable(!cbPassword.isSelected());
    }

private void btnSave(ActionEvent actionEvent) {
    if (ValidationUtil.isEmpty(txtHost, txtPort, txtUser)) return;
    if (!ValidationUtil.isInt(txtPort)) return;

    MySQLConfig config = MySQLConfig.builder()
            .host(txtHost.getText())
            .port(Integer.parseInt(txtPort.getText()))
            .user(txtUser.getText())
            .password(txtPassword.getText())
            .build();

    Connect_BUS.updateConfig(config);
    ValidationUtil.showInfoAlert("Lưu cấu hình thành công!");
    Stage stage = (Stage) btnSave.getScene().getWindow();
    stage.close();
    showLogin();
}

    public void showLogin() {
        try {
            Stage stage = (Stage) btnSave.getScene().getWindow();
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

    private void btnCheckConnection(ActionEvent actionEvent) {
        if (ValidationUtil.isEmpty(txtHost,txtPort,txtUser)) return;
        if (!ValidationUtil.isInt(txtPort)) return;

        MySQLConfig config = MySQLConfig.builder()
                .host(txtHost.getText())
                .port(Integer.parseInt(txtPort.getText()))
                .user(txtUser.getText())
                .password(txtPassword.getText())
                .build();

        if (Connect_BUS.checkConnection(config)){
            lblNotification.setText("Kết nối thành công!");
            lblNotification.setStyle("-fx-text-fill: green");
            btnSave.setDisable(false);
        } else {
            lblNotification.setText("Kết nối thất bại!");
            lblNotification.setStyle("-fx-text-fill: red");
            ValidationUtil.showErrorAlert("Vui lòng kiểm tra lại thông tin kết nối, ứng dụng XAMPP/Server và MySQL đã được mở chưa?");
            btnSave.setDisable(true);
        }
    }
}
