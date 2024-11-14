package milktea.milktea.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import milktea.milktea.BUS.Role_BUS;
import milktea.milktea.DTO.Role;
import milktea.milktea.Util.ValidationUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RoleGUI {
    @FXML
    private ComboBox<Role> cbRole;
    @FXML
    private TextField txtRoleName;
    @FXML
    private Pane paneRole;


    @FXML
    private Button btnApply;
    @FXML
    private Button btnCancel;

    private Role role ;
    private final HashMap<CheckBox,Integer> mapCheckBox = new HashMap<>();

    private int accessLevel = 0;

    private boolean isEdited = false;
    @Getter
    @Setter
    private static boolean isSaved = false;
    public void initialize() {
        setMapCheckBox();
        addListenerCheckBox();
        addSpecificListeners();
        paneRole.getChildren().add(getPaneRole());
        paneRole.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
        paneRole.autosize();
        loadRoleComboBoxDate();
        txtRoleName.setDisable(true);
        btnApply.setDisable(true);
        cbRole.addEventHandler(javafx.event.ActionEvent.ACTION, event -> {
            if (!isEdited){
                if (!btnApply.isDisable()) {
                    isEdited = true;
                    if (ValidationUtil.showConfirmAlert("Bạn có muốn hủy bỏ thay đổi không?")) {
                        role = cbRole.getSelectionModel().getSelectedItem();
                        txtRoleName.setText(role.getRoleName());
                        txtRoleName.setDisable(false);
                        accessLevel = role.getAccess();
                        loadAccessData();
                    } else {
                        cbRole.getSelectionModel().select(role);
                    }
                    isEdited = false;
                } else {
                    role = cbRole.getSelectionModel().getSelectedItem();
                    txtRoleName.setText(role.getRoleName());
                    txtRoleName.setDisable(false);
                    accessLevel = role.getAccess();
                    loadAccessData();
                }
            }
        });
        txtRoleName.textProperty().addListener((observable, oldValue, newValue) -> {
            btnApply.setDisable(newValue.equals(role.getRoleName()));
        });
        btnApply.setOnAction(event -> {
            role.setAccess(accessLevel);
            if (Role_BUS.updateRole(role)){
                ValidationUtil.showConfirmAlert("Cập nhật thành công");
                Role_BUS.updateRoleLocal(role);
                btnApply.setDisable(true);
                isSaved = true;
            }else {
                ValidationUtil.showErrorAlert("Cập nhật thất bại");
            }
        });
        btnCancel.setOnAction(event -> {
            if (!btnApply.isDisable()) {
                if (ValidationUtil.showConfirmAlert("Bạn có muốn lưu thay đổi không?")) {
                    role.setAccess(accessLevel);
                    if (Role_BUS.updateRole(role)) {
                        ValidationUtil.showConfirmAlert("Cập nhật thành công");
                        Role_BUS.updateRoleLocal(role);
                        btnApply.setDisable(true);
                        isSaved = true;
                        Stage stage = (Stage) btnCancel.getScene().getWindow();
                        stage.close();
                    } else {
                        ValidationUtil.showErrorAlert("Cập nhật thất bại");
                    }
                } else {
                    Stage stage = (Stage) btnCancel.getScene().getWindow();
                    stage.close();
                }
            }else {
                Stage stage = (Stage) btnCancel.getScene().getWindow();
                stage.close();
            }
        });
    }

    private void loadRoleComboBoxDate() {
        ArrayList<Role> roles = new ArrayList<>(Role_BUS.getAllRole());
        Iterator<Role> iterator = roles.iterator();

        while (iterator.hasNext()) {
            Role role = iterator.next();
            if (role.getAccess() >= Login_Controller.getAccount().getPermission()) {
                if (!Login_Controller.getAccount().getRole().equals("R01") && role.getRoleId().equals("R01")) {
                    iterator.remove();
                }else if (!role.getRoleId().equals("R01"))
                    iterator.remove();
            }
        }
        cbRole.getItems().addAll(roles);
    }

    private void loadAccessData() {
        for (CheckBox checkBox : mapCheckBox.keySet()) {
            if (role.getRoleId().equals("R01")){
                checkBox.setDisable(mapCheckBox.get(checkBox) == 12 || mapCheckBox.get(checkBox) == 8);
            }else if (role.getRoleId().equals("R03")){
                checkBox.setDisable(mapCheckBox.get(checkBox) == 12 || mapCheckBox.get(checkBox) == 8);
            }else {
                checkBox.setDisable(false);
            }
            checkBox.setSelected((accessLevel & (1 << mapCheckBox.get(checkBox))) != 0);
        }
    }

    public void addListenerCheckBox() {
        for (CheckBox checkBox : mapCheckBox.keySet()) {
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    accessLevel |= (1 << mapCheckBox.get(checkBox));
                } else {
                    accessLevel &= ~(1 << mapCheckBox.get(checkBox));
                }
                if (accessLevel==role.getAccess()){
                    btnApply.setDisable(true);
                }else {
                    btnApply.setDisable(false);
                }
            });
        }
    }
    public void addSpecificListeners(){
        Optional<CheckBox> optionalCheckBox2 = mapCheckBox.keySet().stream()
                .filter(checkBox -> mapCheckBox.get(checkBox) == 2)
                .findFirst();

        if (optionalCheckBox2.isPresent()) {
            CheckBox checkBox2 = optionalCheckBox2.get();
            checkBox2.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    for (CheckBox checkBox : mapCheckBox.keySet()) {
                        if (mapCheckBox.get(checkBox) == 9) {
                            checkBox.setSelected(false);
                        }
                    }
                }
            });
        }

        Optional<CheckBox> optionalCheckBox3 = mapCheckBox.keySet().stream()
                .filter(checkBox -> mapCheckBox.get(checkBox) == 3)
                .findFirst();
        if (optionalCheckBox3.isPresent()) {
            CheckBox checkBox3 = optionalCheckBox3.get();
            checkBox3.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    for (CheckBox checkBox : mapCheckBox.keySet()) {
                        if (mapCheckBox.get(checkBox) == 10) {
                            checkBox.setSelected(false);
                        }
                    }
                }
            });
        }

        Optional<CheckBox> optionalCheckBox5 = mapCheckBox.keySet().stream()
                .filter(checkBox -> mapCheckBox.get(checkBox) == 5)
                .findFirst();
        if (optionalCheckBox5.isPresent()) {
            CheckBox checkBox5 = optionalCheckBox5.get();
            checkBox5.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    for (CheckBox checkBox : mapCheckBox.keySet()) {
                        if (mapCheckBox.get(checkBox) == 11) {
                            checkBox.setSelected(false);
                        }
                    }
                }
            });
        }

        Optional<CheckBox> optionalCheckBox6 = mapCheckBox.keySet().stream()
                .filter(checkBox -> mapCheckBox.get(checkBox) == 6)
                .findFirst();
        if (optionalCheckBox6.isPresent()) {
            CheckBox checkBox6 = optionalCheckBox6.get();
            checkBox6.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    for (CheckBox checkBox : mapCheckBox.keySet()) {
                        if (mapCheckBox.get(checkBox) == 13) {
                            checkBox.setSelected(false);
                        }
                    }
                }
            });
        }

        Optional<CheckBox> optionalCheckBox9 = mapCheckBox.keySet().stream()
                .filter(checkBox -> mapCheckBox.get(checkBox) == 9)
                .findFirst();
        if (optionalCheckBox9.isPresent()) {
            CheckBox checkBox9 = optionalCheckBox9.get();
            checkBox9.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    for (CheckBox checkBox : mapCheckBox.keySet()) {
                        if (mapCheckBox.get(checkBox) == 2) {
                            checkBox.setSelected(true);
                        }
                    }
                }
            });
        }

        Optional<CheckBox> optionalCheckBox10 = mapCheckBox.keySet().stream()
                .filter(checkBox -> mapCheckBox.get(checkBox) == 10)
                .findFirst();
        if (optionalCheckBox10.isPresent()) {
            CheckBox checkBox10 = optionalCheckBox10.get();
            checkBox10.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    for (CheckBox checkBox : mapCheckBox.keySet()) {
                        if (mapCheckBox.get(checkBox) == 3) {
                            checkBox.setSelected(true);
                        }
                    }
                }
            });
        }

        Optional<CheckBox> optionalCheckBox11 = mapCheckBox.keySet().stream()
                .filter(checkBox -> mapCheckBox.get(checkBox) == 11)
                .findFirst();
        if (optionalCheckBox11.isPresent()) {
            CheckBox checkBox11 = optionalCheckBox11.get();
            checkBox11.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    for (CheckBox checkBox : mapCheckBox.keySet()) {
                        if (mapCheckBox.get(checkBox) == 5) {
                            checkBox.setSelected(true);
                        }
                    }
                }
            });
        }

        Optional<CheckBox> optionalCheckBox13 = mapCheckBox.keySet().stream()
                .filter(checkBox -> mapCheckBox.get(checkBox) == 13)
                .findFirst();
        if (optionalCheckBox13.isPresent()) {
            CheckBox checkBox13 = optionalCheckBox13.get();
            checkBox13.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    for (CheckBox checkBox : mapCheckBox.keySet()) {
                        if (mapCheckBox.get(checkBox) == 6) {
                            checkBox.setSelected(true);
                        }
                    }
                }
            });
        }
    }

    public void setMapCheckBox() {
        mapCheckBox.put(new CheckBox("Quản lý hóa đơn"), 0);
        mapCheckBox.put(new CheckBox("Quản lý khách hàng"), 1);
        mapCheckBox.put(new CheckBox("Xem sản phẩm"), 2);
        mapCheckBox.put(new CheckBox("Xem nguyên liệu"), 3);
        mapCheckBox.put(new CheckBox("Quản lý nhập hàng"), 4);
        mapCheckBox.put(new CheckBox("Xem nhà cung cấp"), 5);
        mapCheckBox.put(new CheckBox("Xem Khuyến mãi"), 6);
        mapCheckBox.put(new CheckBox("Thống kê"), 7);
        mapCheckBox.put(new CheckBox("Quản lý nhân viên"), 8);
        mapCheckBox.put(new CheckBox("Quản lý sản phẩm"), 9);
        mapCheckBox.put(new CheckBox("Quản lý nguyên liệu"), 10);
        mapCheckBox.put(new CheckBox("Quản lý nhà cung cấp"), 11);
        mapCheckBox.put(new CheckBox("Quản lý phân quyền"), 12);
        mapCheckBox.put(new CheckBox("Quản lý khuyến mãi"), 13);
    }

    public GridPane getPaneRole() {
        GridPane gridPane = new GridPane();
        AtomicInteger i = new AtomicInteger(0);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        mapCheckBox.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> {
                    CheckBox checkBox = entry.getKey();
                    checkBox.setFont(new Font("Arial", 15));
                    checkBox.setDisable(true);
                    gridPane.add(checkBox, i.get() % 2, i.get() / 2);
                    i.getAndIncrement();

                });
        return gridPane;
    }
}
