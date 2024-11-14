package milktea.milktea.GUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import milktea.milktea.BUS.Customer_BUS;
import milktea.milktea.DTO.Customer;
import milktea.milktea.DTO.Gender;
import milktea.milktea.Util.ValidationUtil;

import java.math.BigDecimal;
import java.util.HashMap;

@Slf4j
public class CustomerSubGUI {
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
    private Label lblTitle;

    @Getter
    @Setter
    private static boolean isAdded = false;
    private final HashMap<TextField, String> textFieldInfo = new HashMap<>();
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.setTitle("Thông tin khách hàng");
        });
        mapTextField();
        if (CustomerGUI.isEditable){
            lblTitle.setText("Sửa thông tin");
            btnSave.setOnAction(this::btnSave);
            cbGender.getItems().add("Nam");
            cbGender.getItems().add("Nữ");
            txtID.setText(CustomerGUI.selectedCustomer.getId());
            txtFirstName.setText(CustomerGUI.selectedCustomer.getFirstName());
            txtLastName.setText(CustomerGUI.selectedCustomer.getLastName());
            txtPhone.setText(CustomerGUI.selectedCustomer.getPhoneNumber());
            cbGender.setValue(pickGender(CustomerGUI.selectedCustomer.getGender()));
        }else{
            lblTitle.setText("Thêm khách hàng");
            btnSave.setOnAction(this::btnSave);
            txtID.setText(Customer_BUS.autoId());
            cbGender.getItems().add("Nam");
            cbGender.getItems().add("Nữ");
            cbGender.setValue("Nam");
        }
    }
    boolean isEditable = false;
    public void btnSave(ActionEvent event){
        isEditable = CustomerGUI.isEditable;
        boolean isValid = validInput();
        System.out.println(isValid);
        if(isValid){
            if (!Customer_BUS.checkAvailablePhone(txtPhone.getText(), txtID.getText())){
                Customer customer = Customer.builder()
                        .id(txtID.getText())
                        .firstName(txtFirstName.getText())
                        .lastName(txtLastName.getText())
                        .gender(cbGender.getValue().equals("Nam") ? Gender.MALE : Gender.FEMALE)
                        .phoneNumber(txtPhone.getText())
                        .point(BigDecimal.valueOf(0))
                        .build();
                if (isEditable) {
                    if (Customer_BUS.editCustomer(customer)) {
                        ValidationUtil.showInfoAlert("Edited");
                        Customer_BUS.editCustomerLocal(customer);
                        ((Stage) btnSave.getScene().getWindow()).close();
                    } else {
                        ValidationUtil.showErrorAlert("Edit failed");
                    }
                } else {
                    if (Customer_BUS.addCustomer(customer)) {
                        Customer_BUS.addCustomerLocal(customer);
                        ValidationUtil.showInfoAlert("Thêm khách hàng thành công");
                        CustomerGUI.selectedCustomer = null;
                        isAdded = true;
                        ((Stage) btnSave.getScene().getWindow()).close();

                    } else {
                        ValidationUtil.showErrorAlert("Thêm khách hàng thất bại");
                    }
                }
            }else {
                ValidationUtil.showErrorAlert("Số điện thoại đã tồn tại");
            }
        }
    }
    public String pickGender(@NonNull Gender gender){
        switch (gender){
            case MALE -> {
                return "Nam";
            }
            case FEMALE -> {
                return "Nữ";
            }
            case OTHER -> {
                return "Khác";
            }
            default -> {
                return null;
            }
        }
    }

    public void mapTextField(){
        textFieldInfo.put(txtID, "ID");
        textFieldInfo.put(txtFirstName, "Họ đệm");
        textFieldInfo.put(txtLastName, "Tên");
        textFieldInfo.put(txtPhone, "SĐT");

        txtPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtPhone.setText(newValue.replaceAll("\\D", ""));
            }   if (newValue.length() > 10) {
                txtPhone.setText(oldValue);
            }
        });
    }
    public boolean validInput(){
        boolean isEmpty = ValidationUtil.isEmpty(txtFirstName, txtID, txtLastName, txtPhone);
        boolean isFirstCharNotSpace = ValidationUtil.isFirstCharNotSpace(textFieldInfo,txtFirstName, txtLastName);
        boolean isInValidChar = ValidationUtil.isInValidChar(textFieldInfo,txtFirstName, txtLastName);
        boolean isNotPhoneNumber = ValidationUtil.isNotPhoneNumber(textFieldInfo,txtPhone);
        return !isEmpty && !isFirstCharNotSpace && !isInValidChar && !isNotPhoneNumber;
    }



}
