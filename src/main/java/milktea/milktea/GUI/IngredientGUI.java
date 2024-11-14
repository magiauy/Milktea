package milktea.milktea.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import milktea.milktea.BUS.GoodsReceiptDetail_BUS;
import milktea.milktea.BUS.Ingredient_BUS;
import milktea.milktea.BUS.Recipe_BUS;
import milktea.milktea.DTO.Ingredient;
import milktea.milktea.DTO.Status;
import milktea.milktea.Util.ValidationUtil;

import static milktea.milktea.Util.UI_Util.openStage;

public class IngredientGUI {
    @FXML
    private TableView<Ingredient> tableMain;
    @FXML
    private TableColumn<Ingredient, String> colId;
    @FXML
    private TableColumn<Ingredient, String> colName;
    @FXML
    private TableColumn<Ingredient, Float> colQuantity;
    @FXML
    private TableColumn<Ingredient, String> colUnit;
    @FXML
    private TableColumn<Ingredient, String> colStatus;

    @FXML
    Button btnSearch;

    @FXML
    ImageView imgAdd;
    @FXML
    ImageView imgEdit;
    @FXML
    ImageView imgDelete;
    @FXML
    ImageView imgLock;
    @FXML
    ImageView imgRefresh;

    @FXML
    TextField txtSearch;

    @Getter
    @Setter
    private static boolean isEditable = false;

    @Getter
    @Setter
    private static Ingredient selectedIngredient;

    public void initialize() {
        hideButtonWithoutPermission();
        createTable();
        imgAdd.setOnMouseClicked(event -> openStage("Ingredient_SubGUI.fxml", () -> {
            if (IngredientSubGUI.isEdited()) {
                    ObservableList<Ingredient> data = FXCollections.observableList(Ingredient_BUS.getAllIngredient());
                    tableMain.setItems(data);
                    tableMain.refresh();
            }
        }));
        imgEdit.setOnMouseClicked(event -> {
            if (tableMain.getSelectionModel().getSelectedItem() != null) {
                selectedIngredient = tableMain.getSelectionModel().getSelectedItem();
                isEditable = true;
                openStage("Ingredient_SubGUI.fxml", () -> {
                    isEditable = false;
                    if (IngredientSubGUI.isEdited()) {
                            ObservableList<Ingredient> data = FXCollections.observableList(Ingredient_BUS.getAllIngredient());
                            tableMain.setItems(data);
                            tableMain.refresh();
                    }
                });
            }else {
                ValidationUtil.showErrorAlert("Vui lòng chọn nguyên liệu cần sửa");
            }
        });

        imgDelete.setOnMouseClicked(event -> {
            Ingredient selectedItem = tableMain.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                ValidationUtil.showErrorAlert("Vui lòng chọn nguyên liệu cần xóa");
                return;
            }
            String ingredientId = selectedItem.getId();
            if (Recipe_BUS.hasIngredient(ingredientId)) {
                ValidationUtil.showErrorAlert("Nguyên liệu này đang được sử dụng trong công thức, không thể xóa");
                return;
            }
            if (GoodsReceiptDetail_BUS.hasIngredient(ingredientId)) {
                ValidationUtil.showErrorAlert("Nguyên liệu này đang được sử dụng trong phiếu nhập, không thể xóa");
                return;
            }
            if (selectedItem.getQuantity() > 0) {
                ValidationUtil.showErrorAlert("Nguyên liệu này đang có số lượng lớn hơn 0, không thể xóa");
                return;
            }
            if (ValidationUtil.showConfirmAlert("Bạn có chắc chắn muốn xóa nguyên liệu này không?")) {
                if (Ingredient_BUS.deleteIngredient(ingredientId)) {
                    Ingredient_BUS.deleteIngredientLocal(ingredientId);
                    ObservableList<Ingredient> data = FXCollections.observableList(Ingredient_BUS.getAllIngredient());
                    tableMain.setItems(data);
                    tableMain.refresh();
                    ValidationUtil.showInfoAlert("Xóa nguyên liệu thành công");
                } else {
                    ValidationUtil.showErrorAlert("Xóa nguyên liệu thất bại");
                }
            }
        });
        btnSearch.setOnMouseClicked(event -> {
            if (!ValidationUtil.isInvalidSearch(txtSearch)) {
                ObservableList<Ingredient> data = FXCollections.observableList(Ingredient_BUS.searchIngredient(txtSearch.getText()));
                tableMain.setItems(data);
                tableMain.refresh();
            }
        });
        imgLock.setOnMouseClicked(event -> {
            if (tableMain.getSelectionModel().getSelectedItem() != null) {
                if (tableMain.getSelectionModel().getSelectedItem().getStatus().equals(Status.ACTIVE)) {
                    if (Ingredient_BUS.changeStatus(tableMain.getSelectionModel().getSelectedItem().getId(),Status.INACTIVE)) {
                        Ingredient_BUS.changeStatusLocal(tableMain.getSelectionModel().getSelectedItem().getId(),Status.INACTIVE);
                        ObservableList<Ingredient> data = FXCollections.observableList(Ingredient_BUS.getAllIngredient());
                        tableMain.setItems(data);
                        tableMain.refresh();
                        ValidationUtil.showInfoAlert("Khóa nguyên liệu thành công");
                    } else {
                        ValidationUtil.showErrorAlert("Khóa nguyên liệu thất bại");
                    }
                } else {
                    if (Ingredient_BUS.changeStatus(tableMain.getSelectionModel().getSelectedItem().getId(),Status.ACTIVE)) {
                        Ingredient_BUS.changeStatusLocal(tableMain.getSelectionModel().getSelectedItem().getId(),Status.ACTIVE);
                        ObservableList<Ingredient> data = FXCollections.observableList(Ingredient_BUS.getAllIngredient());
                        tableMain.setItems(data);
                        tableMain.refresh();
                        ValidationUtil.showInfoAlert("Mở khóa nguyên liệu thành công");
                    } else {
                        ValidationUtil.showErrorAlert("Mở khóa nguyên liệu thất bại");
                    }
                }
            } else {
                ValidationUtil.showErrorAlert("Vui lòng chọn nguyên liệu cần khóa");
            }
        });
        imgRefresh.setOnMouseClicked(event -> {
            Ingredient_BUS.getLocalData();
            txtSearch.setText("");
            ObservableList<Ingredient> data = FXCollections.observableList(Ingredient_BUS.getAllIngredient());
            tableMain.setItems(data);
            tableMain.refresh();
        });
    }

    public void createTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        ObservableList<Ingredient> data = FXCollections.observableList(Ingredient_BUS.getAllIngredient());
        tableMain.setItems(data);
        tableMain.refresh();
    }
    public void hideButtonWithoutPermission(){
        int permission = Login_Controller.getAccount().getPermission();
        if (!Main.checkRolePermission(permission,10)){
            imgAdd.setVisible(false);
            imgDelete.setVisible(false);
            imgEdit.setVisible(false);
            imgLock.setVisible(false);
        }
    }
}
