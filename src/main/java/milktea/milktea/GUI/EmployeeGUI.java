package milktea.milktea.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import milktea.milktea.BUS.*;
import milktea.milktea.DTO.Employee;

import milktea.milktea.DTO.Status;
import milktea.milktea.Util.ValidationUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import static milktea.milktea.Util.UI_Util.openStage;
@Slf4j
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
    @FXML
    private ImageView imgRefresh;
    @FXML
    private ImageView imgLock;

    @FXML
    private Button btnRole;


    @Getter
    @Setter
    private static boolean isEdited = false;

    @Getter
    @Setter
    private static Employee selectedEmployee = new Employee();
    @FXML
    private void initialize() {
        loadTable();
        btnSearch.setOnAction(event -> btnSearch());
        imgDelete.setOnMouseClicked(event -> imgDelete());
        imgAdd.setOnMouseClicked(event -> imgAdd());
        imgEdit.setOnMouseClicked(event -> imgEdit());
        btnRole.setOnAction(event -> btnRole());
        hideButtonWithoutPermission();
        ImageView imgRoles = new ImageView(Objects.requireNonNull(getClass().getClassLoader().getResource("img/Settings.png")).toString());
        imgRoles.setFitHeight(25);
        imgRoles.setFitWidth(25);
        btnRole.setGraphic(imgRoles);
        btnRole.setStyle(" -fx-padding: 0");
        imgRefresh.setOnMouseClicked(event -> {
            Employee_BUS.getLocalData();
            Role_BUS.getLocalData();
            txtSearch.clear();
            loadTable();
            tableMain.getSelectionModel().clearSelection();
            selectedEmployee = null;
            ValidationUtil.showInfoAlert("Làm mới dữ liệu thành công");


        });
        imgLock.setOnMouseClicked(event -> {
            Employee employee = tableMain.getSelectionModel().getSelectedItem();
            if (employee == null) {
                ValidationUtil.showErrorAlert("Vui lòng chọn nhân viên cần khóa");
                return;
            }
            if (employee.getId().equals(Login_Controller.getAccount().getId())) {
                ValidationUtil.showErrorAlert("Không thể khóa tài khoản đang đăng nhập");
                return;
            }

            Status newStatus = employee.getStatus().equals(Status.ACTIVE) ? Status.INACTIVE : Status.ACTIVE;
            String confirmMessage = newStatus.equals(Status.INACTIVE) ? "Bạn có chắc chắn muốn khóa nhân viên này không?" : "Bạn có chắc chắn muốn mở khóa nhân viên này không?";
            String lockImage = newStatus.equals(Status.INACTIVE) ?
                    Objects.requireNonNull(getClass().getClassLoader().getResource("img/Lock.png")).toString() :
                    Objects.requireNonNull(getClass().getClassLoader().getResource("img/Padlock.png")).toString();

            if (!ValidationUtil.showConfirmAlert(confirmMessage)) return;
            if (!Employee_BUS.updateStatus(employee.getId(), newStatus)) {
                ValidationUtil.showInfoAlert("Khóa nhân viên thất bại");
                return;
            }
            if (!Employee_BUS.updateStatusLocal(employee.getId(), newStatus)) {
                ValidationUtil.showInfoAlert("Khóa nhân viên thất bại");
                return;
            }

            employee.setStatus(newStatus);
            imgLock.setImage(new ImageView(lockImage).getImage());
            ObservableList<Employee> data = FXCollections.observableArrayList(Employee_BUS.getAllEmployee());
            tableMain.setItems(data);
            tableMain.refresh();
        });
    }

    private void btnRole() {
        openStage("RoleGUI.fxml",()->{
            if (RoleGUI.isSaved()){
                try {
                    ValidationUtil.showInfoAlert("Vui lòng đăng nhập lại để cập nhật quyền");
                    Stage stage = (Stage) tableMain.getScene().getWindow();
                    stage.close();
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("LoginGUI.fxml"));

                    Scene scene = new Scene(fxmlLoader.load());
                    stage.setTitle("MilkTea_Store!");
                    stage.setScene(scene);
                    stage.show();
                    RoleGUI.setSaved(false);
                }catch (IOException e){
                    log.error("Error: ", e);
                }
            }
        });
    }
    private void btnSearch() {
        if (!Login_Controller.getAccount().getRole().equals("R01")){
            ArrayList<Employee> arrEmployee = new ArrayList<>(Employee_BUS.searchEmployee(txtSearch.getText()));
            if (arrEmployee.isEmpty()) {
                ValidationUtil.showErrorAlert("Không tìm thấy nhân viên");
                return;
            }
            arrEmployee.removeIf(employee -> employee.getRole().equals("R01"));
            if (!txtSearch.getText().isEmpty()) {
                tableMain.setItems(FXCollections.observableArrayList(FXCollections.observableArrayList(arrEmployee)));
            }else {
                loadTable();
            }
        }else {
            if (!txtSearch.getText().isEmpty()) {
                ArrayList<Employee> arrEmployee = new ArrayList<>(Employee_BUS.searchEmployee(txtSearch.getText()));
                if (arrEmployee.isEmpty()) {
                    ValidationUtil.showErrorAlert("Không tìm thấy nhân viên");
                    return;
                }
                ObservableList<Employee> data = FXCollections.observableArrayList(arrEmployee);
                tableMain.setItems(data);
            }else {
                loadTable();
            }
        }
    }

    private void imgDelete() {
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
    }

    private void imgAdd() {
        isEdited = false;
        openStage("Employee_SubGUI.fxml", () -> {
            if (EmployeeSubGUI.isSaved()) {
                EmployeeSubGUI.setSaved(false);
                loadTable();
            }
        });
    }

    private void imgEdit() {
        if (tableMain.getSelectionModel().getSelectedItem() != null) {
            selectedEmployee = tableMain.getSelectionModel().getSelectedItem();
            isEdited = true;
            openStage("Employee_SubGUI.fxml",()->{
                tableMain.refresh();
                if (EmployeeSubGUI.isSaved()){
                    isEdited = false;
                    EmployeeSubGUI.setSaved(false);
                    System.out.println(Employee_BUS.getAllEmployee());
                    loadTable();
                }
            });
        }else {
            ValidationUtil.showErrorAlert("Chọn nhân viên cần sửa");
        }
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
        tableMain.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                imgLock.setImage(newValue.getStatus().equals(Status.ACTIVE)?
                        new ImageView(Objects.requireNonNull(getClass().getClassLoader().getResource("img/Lock.png")).toString()).getImage()
                        :new ImageView(Objects.requireNonNull(getClass().getClassLoader().getResource("img/Padlock.png")).toString()).getImage());
            }
        });
        ArrayList<Employee> arrEmployee = new ArrayList<>(Employee_BUS.getAllEmployee());
        Iterator<Employee> iterator = arrEmployee.iterator();

        while (iterator.hasNext()) {
            Employee employee = iterator.next();
            if (Objects.requireNonNull(Role_BUS.getRoleByID(employee.getRole())).getAccess() >= Login_Controller.getAccount().getPermission()) {
                if (Login_Controller.getAccount().getId().equals(employee.getId())) {
                    continue;
                }
                if (!Login_Controller.getAccount().getRole().equals("R01") && employee.getRole().equals("R01")) {
                    iterator.remove();
                } else if (!employee.getRole().equals("R01")) {
                    iterator.remove();
                }
            }
        }
        tableMain.setItems(FXCollections.observableArrayList(arrEmployee));
        tableMain.refresh();
    }

    public void hideButtonWithoutPermission(){
        int permission = Login_Controller.getAccount().getPermission();
        if (!Main.checkRolePermission(permission,14)){
            btnRole.setVisible(false);
        }
    }
}
