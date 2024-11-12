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
    TextField txtSearch;

    @Getter
    @Setter
    private static boolean isEditable = false;

    @Getter
    @Setter
    private static Ingredient selectedIngredient;

    public void initialize() {
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
            if (tableMain.getSelectionModel().getSelectedItem() != null) {
                if (!Recipe_BUS.hasIngredient(tableMain.getSelectionModel().getSelectedItem().getId())) {
                    if (!GoodsReceiptDetail_BUS.hasIngredient(tableMain.getSelectionModel().getSelectedItem().getId())) {
                        if (ValidationUtil.showConfirmAlert("Bạn có chắc chắn muốn xóa nguyên liệu này không?")) {
                            if (Ingredient_BUS.deleteIngredient(tableMain.getSelectionModel().getSelectedItem().getId())) {
                                Ingredient_BUS.deleteIngredientLocal(tableMain.getSelectionModel().getSelectedItem().getId());
                                ObservableList<Ingredient> data = FXCollections.observableList(Ingredient_BUS.getAllIngredient());
                                tableMain.setItems(data);
                                tableMain.refresh();
                                ValidationUtil.showInfoAlert("Xóa nguyên liệu thành công");
                            } else {
                                ValidationUtil.showErrorAlert("Xóa nguyên liệu thất bại");
                            }
                        }
                    }else {
                        ValidationUtil.showErrorAlert("Nguyên liệu này đang được sử dụng trong phiếu nhập, không thể xóa");
                    }
                } else {
                    ValidationUtil.showErrorAlert("Nguyên liệu này đang được sử dụng trong công thức, không thể xóa");
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
    }

    public void createTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        ObservableList<Ingredient> data = FXCollections.observableList(Ingredient_BUS.getAllIngredient());
        tableMain.setItems(data);
    }
}
