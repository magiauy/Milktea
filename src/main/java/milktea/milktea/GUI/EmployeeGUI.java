package milktea.milktea.GUI;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import milktea.milktea.BUS.Employee_BUS;
import milktea.milktea.BUS.GoodsReceipt_BUS;
import milktea.milktea.BUS.Invoice_BUS;
import milktea.milktea.DTO.Employee;
import milktea.milktea.Util.ValidationUtil;

import java.util.ArrayList;

import static milktea.milktea.Util.UI_Util.openStage;
public class EmployeeGUI {
    @FXML
    private TableView<Employee> tableMain;
    @FXML
    private TableColumn<Employee, String> colId;
    @FXML
    private TableColumn<Employee, String> colFirstName;
    @FXML
    private TableColumn<Employee, String> colLastName;
    @FXML
    private TableColumn<Employee, String> colGender;
    @FXML
    private TableColumn<Employee, String> colPhone;
    @FXML
    private TableColumn<Employee, String> colRole;
    @FXML
    private TableColumn<Employee, String> colUsername;
    @FXML
    private TableColumn<Employee, String> colPassword;
    @FXML
    private TableColumn<Employee, String> colStatus;

    @FXML
    private TextField txtSearch;
    @FXML
    private Button btnSearch;
    @FXML
    private ImageView imgAdd;
    @FXML
    private ImageView imgEdit;
    @FXML
    private ImageView imgDelete;

    @Getter
    @Setter
    private static boolean isEdited = false;

    @Getter
    @Setter
    private static Employee selectedEmployee = new Employee();
    @FXML
    private void initialize() {
        loadTable();
        btnSearch.setOnAction(event -> {
            if (!Login_Controller.getAccount().getRole().equals("R01")){
                ArrayList<Employee> arrEmployee = new ArrayList<>(Employee_BUS.searchEmployee(txtSearch.getText()));
                arrEmployee.removeIf(employee -> employee.getRole().equals("R01"));
                if (!txtSearch.getText().isEmpty()) {
                    tableMain.setItems(FXCollections.observableArrayList(FXCollections.observableArrayList(arrEmployee)));
                }else {
                    loadTable();
                }
            }else {
                if (!txtSearch.getText().isEmpty()) {
                    tableMain.setItems(FXCollections.observableArrayList(Employee_BUS.searchEmployee(txtSearch.getText())));
                }else {
                    loadTable();
                }
            }
        });
        imgDelete.setOnMouseClicked(event -> {
            if (!isEmployeeSelected()) return;

            selectedEmployee = tableMain.getSelectionModel().getSelectedItem();

            if (!ValidationUtil.showConfirmAlert("Xác nhận xóa nhân viên")) return;

            if (isDeletingLoggedInUser()) return;

            if (!canDeleteEmployee(selectedEmployee.getId())) return;

            if (Employee_BUS.deleteEmployee(selectedEmployee.getId())) {
                loadTable();
            } else {
                Employee_BUS.callBackIfFail(selectedEmployee);
                ValidationUtil.showErrorAlert("Xoá nhân viên thất bại");
            }
        });
        imgAdd.setOnMouseClicked(event -> {
            isEdited = false;
            openStage("Employee_SubGUI.fxml", () -> {
                if (EmployeeSubGUI.isSaved()) {
                    EmployeeSubGUI.setSaved(false);
                    loadTable();
                }
            });
        });
        imgEdit.setOnMouseClicked(event -> {
            if (tableMain.getSelectionModel().getSelectedItem() != null) {
                selectedEmployee = tableMain.getSelectionModel().getSelectedItem();
                isEdited = true;
                openStage("Employee_SubGUI.fxml",()->{
                    if (EmployeeSubGUI.isSaved()){
                        isEdited = false;
                        EmployeeSubGUI.setSaved(false);
                        loadTable();
                    }
                });
            }else {
                ValidationUtil.showErrorAlert("Chọn nhân viên cần sửa");
            }
        });
    }
    private boolean isEmployeeSelected() {
        if (tableMain.getSelectionModel().getSelectedItem() == null) {
            ValidationUtil.showErrorAlert("Chọn nhân viên cần xóa");
            return false;
        }
        return true;
    }

    private boolean isDeletingLoggedInUser() {
        if (selectedEmployee.getId().equals(Login_Controller.getAccount().getId())) {
            ValidationUtil.showErrorAlert("Không thể xóa tài khoản đang đăng nhập");
            return true;
        }
        return false;
    }

    private boolean canDeleteEmployee(String employeeId) {
        if (GoodsReceipt_BUS.isEmployeeExist(employeeId)) {
            ValidationUtil.showErrorAlert("Nhân viên đã tạo phiếu nhập không thể xóa");
            return false;
        }
        if (Invoice_BUS.isEmployeeExist(employeeId)) {
            ValidationUtil.showErrorAlert("Nhân viên đã tạo hóa đơn không thể xóa");
            return false;
        }
        return true;
    }
    public void loadTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        if (!Login_Controller.getAccount().getRole().equals("R01")){
            ArrayList<Employee> arrEmployee = new ArrayList<>(Employee_BUS.getAllEmployee());
            arrEmployee.removeIf(employee -> employee.getRole().equals("R01"));
            tableMain.setItems(FXCollections.observableArrayList(FXCollections.observableArrayList(arrEmployee)));
        }else {
            tableMain.setItems(FXCollections.observableArrayList(Employee_BUS.getAllEmployee()));
        }
    }
}
