package milktea.milktea.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import milktea.milktea.BUS.Provider_BUS;
import milktea.milktea.Util.ValidationUtil;

import milktea.milktea.DTO.Provider;
import java.util.HashMap;

public class ProviderSubGUI {
    @FXML
    private Label lblTitle;
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtEmail;

    @FXML
    private Button btnSave;

    @Getter
    @Setter
    private static boolean isSaved = false;
    public void initialize() {
        HashMap<TextField, String> map = new HashMap<>();
        map.put(txtName, "Tên nhà cung cấp");
        map.put(txtAddress, "Địa chỉ");
        map.put(txtPhone, "Số điện thoại");
        map.put(txtEmail, "Email");

        if (!ProviderGUI.isEditable()) {
            lblTitle.setText("Thêm nhà cung cấp");
            txtID.setText(Provider_BUS.autoID());
        }else {
            lblTitle.setText("Sửa nhà cung cấp");
            txtID.setText(ProviderGUI.getSelectedProvider().getId());
            txtName.setText(ProviderGUI.getSelectedProvider().getName());
            txtAddress.setText(ProviderGUI.getSelectedProvider().getAddress());
            txtPhone.setText(ProviderGUI.getSelectedProvider().getPhone());
            txtEmail.setText(ProviderGUI.getSelectedProvider().getEmail());
        }
        btnSave.setOnAction(e -> {
            if (!ValidationUtil.isEmpty(txtName, txtAddress, txtPhone, txtEmail)) {
                if (!ValidationUtil.isFirstCharNotSpace(map,txtName, txtAddress, txtPhone, txtEmail)) {
                    if (!ValidationUtil.isInValidChar(map, txtName)) {
                        if (!ValidationUtil.isNotPhoneNumber(map, txtPhone)) {
                            if (!ValidationUtil.isNotEmail(map, txtEmail)) {
                                Provider provider = Provider.builder()
                                        .id(txtID.getText())
                                        .name(txtName.getText())
                                        .address(txtAddress.getText())
                                        .phone(txtPhone.getText())
                                        .email(txtEmail.getText())
                                        .build();
                                if (!ProviderGUI.isEditable()) {
                                    if (Provider_BUS.addProvider(provider)) {
                                        Provider_BUS.addProviderLocal(provider);
                                        isSaved = true;
                                        Stage stage = (Stage) btnSave.getScene().getWindow();
                                        stage.close();
                                    }else {
                                        ValidationUtil.showErrorAlert("Thêm nhà cung cấp thất bại");
                                    }
                                } else {
                                    if (Provider_BUS.editProvider(provider)){
                                        Provider_BUS.editProviderLocal(provider);
                                        isSaved = true;
                                        Stage stage = (Stage) btnSave.getScene().getWindow();
                                        stage.close();
                                    }else {
                                        ValidationUtil.showErrorAlert("Sửa nhà cung cấp thất bại");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}