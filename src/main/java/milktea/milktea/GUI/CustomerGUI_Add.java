package milktea.milktea.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.extern.slf4j.Slf4j;
import milktea.milktea.BUS.Customer_BUS;
import milktea.milktea.DTO.Customer;
import milktea.milktea.DTO.Gender;

import java.math.BigDecimal;
@Slf4j
public class CustomerGUI_Add {
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtPhone;
    @FXML
    private ComboBox<String> cbGender;
    @FXML
    private Button btnSave;
    @FXML
    public void initialize() {
    btnSave.setOnAction(this::btnSave);
    txtID.setText(Customer_BUS.autoId());
    cbGender.getItems().add("Nam");
    cbGender.getItems().add("Nữ");
    }

    public void btnSave(ActionEvent event) {
            if (validate()) {
                Customer customer = Customer.builder()
                        .id(txtID.getText())
                        .firstName(txtFirstName.getText())
                        .lastName(txtLastName.getText())
                        .gender(cbGender.getValue().equals("Nam") ? Gender.MALE : Gender.FEMALE)
                        .phoneNumber(txtPhone.getText())
                        .point(BigDecimal.valueOf(0))
                        .build();
                if(Customer_BUS.addCustomer(customer)){
                    infoAlert("Add customer successful");
                    CustomerGUI.selectedCustomer = null;
                    ((Stage) btnSave.getScene().getWindow()).close();

                }else{
                    errorAlert("Add customer failed");
                }
            }

    }
    public boolean validate(){
        boolean valid = true;
        boolean alert = false;
        if (txtID.getText().isEmpty()|| txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty() || txtPhone.getText().isEmpty() || cbGender.getSelectionModel().isEmpty()){
            errorAlert("Please fill all the fields");
            valid = false;
            alert = true;
        }

        if (!txtFirstName.getText().matches("^[^\\s\\d\\W][^\\d\\W]*$") ){
            if (!alert) {
                errorAlert("FirstName must not contain number or special character");
                valid = false;
                alert = true;
            }
        }if(!txtLastName.getText().matches("^[^\\s\\d\\W][^\\d\\W]*$")){
            if(!alert){
                errorAlert("LastName must not contain number or special character");
                alert = true;
                valid = false;
            }
        }
        if (!txtPhone.getText().matches("^0.*")){
            if (!alert) {
                errorAlert("PhoneNumber must start with 0");
                alert = true;
                valid = false;
            }
        }if(!txtPhone.getText().matches("[0-9]+")){
            if (!alert){
                errorAlert("PhoneNumber must be a number");
                alert = true;
                valid = false;
            }
        }

        if (txtPhone.getText().length() != 10) {
            if (!alert) {
                errorAlert("Phone must have 10 digits");
                alert = true;
                valid = false;
            }
        }if (Customer_BUS.checkAvailablePhone(txtPhone.getText(),txtID.getText())){
            if (!alert){
                errorAlert("Phone already exists");
                alert = true;
                valid = false;
            }
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




}
