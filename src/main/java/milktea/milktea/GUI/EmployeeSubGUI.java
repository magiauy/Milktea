package milktea.milktea.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import milktea.milktea.BUS.Employee_BUS;
import milktea.milktea.BUS.Role_BUS;
import milktea.milktea.DTO.Employee;
import milktea.milktea.DTO.Gender;
import milktea.milktea.DTO.Role;
import milktea.milktea.DTO.Status;
import milktea.milktea.Util.ValidationUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class EmployeeSubGUI {
    @FXML
    Label lblTitle;
    @FXML
    TextField txtID;
    @FXML
    TextField txtFirstName;
    @FXML
    TextField txtLastName;
    @FXML
    TextField txtPhone;
    @FXML
    TextField txtUsername;
    @FXML
    TextField txtPassword;
    @FXML
    ComboBox<Role> cbRole;
    @FXML
    ComboBox<String> cbGender;
    @FXML
    Button btnSave;

    @Getter
    @Setter
    private static boolean isSaved = false;
    HashMap<TextField, String> textFieldInfo = new HashMap<>();

    public void initialize() {
        textFieldInfo.put(txtFirstName, "Họ đệm");
        textFieldInfo.put(txtLastName, "Tên");
        textFieldInfo.put(txtPhone, "Số điện thoại");
        textFieldInfo.put(txtUsername, "Username");
        textFieldInfo.put(txtPassword, "Password");

        if (EmployeeGUI.isEdited()) {
            lblTitle.setText("Sửa nhân viên");
            txtID.setText(EmployeeGUI.getSelectedEmployee().getId());
            txtFirstName.setText(EmployeeGUI.getSelectedEmployee().getFirstName());
            txtLastName.setText(EmployeeGUI.getSelectedEmployee().getLastName());
            txtPhone.setText(EmployeeGUI.getSelectedEmployee().getPhoneNumber());
            txtUsername.setText(EmployeeGUI.getSelectedEmployee().getUsername());
            txtUsername.setDisable(true);
            txtPassword.setText(EmployeeGUI.getSelectedEmployee().getPassword());
            disableRole();
            cbRole.setValue(Role_BUS.getRoleByID(EmployeeGUI.getSelectedEmployee().getRole()));
            cbGender.setValue(String.valueOf(EmployeeGUI.getSelectedEmployee().getGender()));
        } else {
            lblTitle.setText("Thêm nhân viên");
            txtID.setText(Employee_BUS.autoID());
            txtFirstName.setText("");
            txtLastName.setText("");
            txtPhone.setText("");
            txtUsername.setText(Employee_BUS.autoID());
            txtUsername.setDisable(true);
            txtPassword.setText("123456");
            txtPassword.setDisable(true);
            cbRole.setValue(Role_BUS.getRoleByID("R03"));
            cbRole.setDisable(!
                    Login_Controller.getAccount().getRole().equals("R01"));
            cbGender.setValue(pickGender(Gender.MALE));
        }
        setUpCb();
        btnSave.setOnAction(this::save);
    }

    public void disableRole(){
        if (!Login_Controller.getAccount().getRole().equals("R01")){
            cbRole.setDisable(true);
        }
        if (Login_Controller.getAccount().getRole().equals(EmployeeGUI.getSelectedEmployee().getRole())){
            cbRole.setDisable(true);
        }
    }

    private void save(ActionEvent actionEvent) {
        if (!isValidInput()) return;

        Employee employee = Employee.builder()
                .id(txtID.getText())
                .firstName(txtFirstName.getText())
                .lastName(txtLastName.getText())
                .phoneNumber(txtPhone.getText())
                .gender(cbGender.getValue().equals("Nam") ? Gender.MALE : Gender.FEMALE)
                .username(txtUsername.getText())
                .password(txtPassword.getText())
                .role(cbRole.getValue().getRoleId())
                .status(Status.ACTIVE)
                .build();

        if (EmployeeGUI.isEdited()) {
            handleEditEmployee(employee);
        } else {
            handleAddEmployee(employee);
        }
    }

    private boolean isValidInput() {
        if (ValidationUtil.isEmpty(txtFirstName, txtLastName, txtPhone, txtUsername, txtPassword)) return false;
        if (ValidationUtil.isEmptyComboBox(cbRole, cbGender)) return false;
        if (ValidationUtil.isNotPhoneNumber(textFieldInfo, txtPhone)) return false;
        if (Employee_BUS.checkAvailablePhone(txtPhone.getText(), txtID.getText())) {
            ValidationUtil.showErrorAlert("Số điện thoại đã tồn tại");
            return false;
        }
        if (ValidationUtil.isInValidChar(textFieldInfo, txtFirstName, txtLastName)) return false;
        if (ValidationUtil.isFirstCharNotSpace(textFieldInfo, txtFirstName, txtLastName, txtPassword)) return false;
        return true;
    }

    private void handleEditEmployee(Employee employee) {
        if (Employee_BUS.editEmployee(employee)) {
            ValidationUtil.showInfoAlert("Sửa thành công");
            Employee_BUS.editEmployeeLocal(employee);
            isSaved = true;
            closeWindow();
        } else {
            ValidationUtil.showErrorAlert("Sửa thất bại");
        }
    }

    private void handleAddEmployee(Employee employee) {
        if (Employee_BUS.addEmployee(employee)) {
            Employee_BUS.addEmployeeLocal(employee);
            ValidationUtil.showInfoAlert("Thêm thành công");
            isSaved = true;
            closeWindow();
        } else {
            ValidationUtil.showErrorAlert("Thêm thất bại");
        }
    }

    private void closeWindow() {
        ((javafx.stage.Stage) btnSave.getScene().getWindow()).close();
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
    public void setUpCb(){
        ArrayList<Role> arrRole = new ArrayList<>(Role_BUS.getAllRole());
        arrRole.removeIf(role -> role.getRoleId().equals("R01"));
        cbRole.getItems().addAll(arrRole);
        cbGender.getItems().addAll("Nam", "Nữ", "Khác");
    }
}
