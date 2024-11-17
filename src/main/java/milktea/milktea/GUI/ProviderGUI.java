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
import milktea.milktea.BUS.GoodsReceipt_BUS;
import milktea.milktea.BUS.Provider_BUS;
import milktea.milktea.DTO.Provider;
import milktea.milktea.Util.ValidationUtil;

import java.util.ArrayList;

import static milktea.milktea.Util.UI_Util.openStage;
public class ProviderGUI {
    @FXML
    private TableView<Provider> tableMain;
    @FXML
    private TableColumn<Provider, String> colID;
    @FXML
    private TableColumn<Provider, String> colName;
    @FXML
    private TableColumn<Provider, String> colAddress;
    @FXML
    private TableColumn<Provider, String> colPhone;
    @FXML
    private TableColumn<Provider, String> colEmail;

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

    @Getter
    @Setter
    private static boolean isEditable = false;
    @Getter
    @Setter
    private static Provider selectedProvider;
    public void initialize() {
        hideButtonWithoutPermission();
        loadProvider();
        btnSearch.setOnAction(e -> {
            if (!ValidationUtil.isInvalidSearch(txtSearch)) {
                ArrayList<Provider> providers = Provider_BUS.searchProvider(txtSearch.getText());
                if (providers.isEmpty()) {
                    ValidationUtil.showErrorAlert("Không tìm thấy kết quả");
                    return;
                }
                tableMain.setItems(FXCollections.observableArrayList(providers));
                tableMain.refresh();
            }
        });
        imgDelete.setOnMouseClicked(e -> {
            Provider provider = tableMain.getSelectionModel().getSelectedItem();
            if (provider == null) {
                ValidationUtil.showErrorAlert("Vui lòng chọn nhà cung cấp cần xóa");
                return;
            }
            if (!ValidationUtil.showConfirmAlert("Bạn có chắc chắn muốn xóa nhà cung cấp này không?")) return;
            if (GoodsReceipt_BUS.isProviderExist(provider.getId())) {
                ValidationUtil.showErrorAlert("Nhà cung cấp đã tồn tại trong phiếu nhập hàng, không thể xóa");
                return;
            }
            if (!Provider_BUS.deleteProvider(provider.getId())) {
                ValidationUtil.showErrorAlert("Xóa nhà cung cấp thất bại");
                return;
            }
            Provider_BUS.deleteProviderLocal(provider.getId());
            loadProvider();
        });
        imgAdd.setOnMouseClicked(e -> openStage("Provider_SubGUI.fxml", () -> {
            if (ProviderSubGUI.isSaved()) {
                loadProvider();
            }
        }));
        imgEdit.setOnMouseClicked(e -> {
            if (tableMain.getSelectionModel().getSelectedItem() != null) {
                selectedProvider = tableMain.getSelectionModel().getSelectedItem();
                isEditable = true;
                openStage("Provider_SubGUI.fxml", () -> {
                    if (ProviderSubGUI.isSaved()) {
                        loadProvider();
                    }
                    isEditable = false;
                });
            }else {
                ValidationUtil.showErrorAlert("Vui lòng chọn nhà cung cấp cần sửa");
            }
        });
        imgRefresh.setOnMouseClicked(e -> {
            Provider_BUS.getLocalData();
            txtSearch.clear();
            loadProvider();
            tableMain.getSelectionModel().clearSelection();
            selectedProvider = null;
            ValidationUtil.showInfoAlert("Làm mới dữ liệu thành công");

        });
    }
    public void loadProvider() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableMain.setItems(FXCollections.observableArrayList(Provider_BUS.getAllProvider()));
    }

    public void hideButtonWithoutPermission(){
        int permission = Login_Controller.getAccount().getPermission();
        if (!Main.checkRolePermission(permission,13)){
            imgAdd.setVisible(false);
            imgDelete.setVisible(false);
            imgEdit.setVisible(false);
        }
    }
}
